package astrolab.perspective.classmates;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import astrolab.db.Database;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class FormClassmatesLogin extends HTMLFormDisplay {

  static int ID = HTMLFormDisplay.getId(FormClassmatesLogin.class);
  static String CLASSMATE_ID = "classmate";

  private final static String LOGIN_NO_PASSWORD = "За да имаш достъп до личните данни въведи паролата си!";
  private final static String LOGIN_WRONG_PASSWORD = "<font color='red'>Грешна парола!</font>";
  private final static String LOGIN_CORRECT_PASSWORD = "Мда, ти си! Сега може да видиш личните данни.";
  private final static String LOGIN_CHANGE_PASSWORD = "Трябва да си смениш паролата!";
  private final static String LOGIN_DIFFERENT_PASSWORD = "Нещо не нацели паролите! Трябва да въведеш два пъти новата парола.";

  public FormClassmatesLogin() {
    super(FormClassmatesLogin.ID, true);
  }

  public void initialize(HTMLFormDisplay form) {
    form.setSubmitAction(-1);
    form.setSubmitDisplay(FormClassmatesLogin.ID);
    form.setSubmitModify(-1);
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    String error = authenticate(request);

    if (error == LOGIN_CORRECT_PASSWORD) {
      buffer.append(error);
    } else if (error == LOGIN_CHANGE_PASSWORD || error == LOGIN_DIFFERENT_PASSWORD) {
      buffer.append(error);
      buffer.append("<hr />");
      fillPasswordChange(request, buffer);
    } else {
      buffer.append(error);
      buffer.append("<hr />");
      fillLogin(request, buffer);
      buffer.append("<hr />");
      fillRequestCode(request, buffer);
    }
  }

  private String authenticate(Request request) {
    try {
      String classmate = request.get("login_id");
      String passwordEntry = request.get("login_password");

      if (passwordEntry == null || passwordEntry.length() == 0) {
        return LOGIN_NO_PASSWORD;
      }

      String passwordChange = Database.query("SELECT change_password FROM project_classmates WHERE classmate_id = " + classmate + " AND password = '" + digest(passwordEntry) + "'"); 
      if ("no".equals(passwordChange)) {
        request.getConnection().getOutput().setCookie(CLASSMATE_ID, classmate, false);
        return LOGIN_CORRECT_PASSWORD;
      } else if ("yes".equals(passwordChange)) {
        String newPasswordEntry1 = request.get("login_password_1");
        String newPasswordEntry2 = request.get("login_password_2");

        if (newPasswordEntry1 == null || newPasswordEntry2 == null) {
          return LOGIN_CHANGE_PASSWORD;
        } else if (!newPasswordEntry1.equals(newPasswordEntry2)) {
          return LOGIN_DIFFERENT_PASSWORD;
        } else {
          Database.execute("UPDATE project_classmates SET password = '" + digest(newPasswordEntry1) + "' WHERE classmate_id = " + classmate + "");
          Database.execute("UPDATE project_classmates SET change_password = 'no' WHERE classmate_id = " + classmate + "");
          request.getConnection().getOutput().setCookie(CLASSMATE_ID, classmate, false);
          return LOGIN_CORRECT_PASSWORD;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return LOGIN_WRONG_PASSWORD;
  }

  private String digest(String password) throws NoSuchAlgorithmException {
    String passtext = "";
    MessageDigest digest = MessageDigest.getInstance("SHA-1");
    byte[] pass = digest.digest(password.getBytes());
    for (int i = 0; i < pass.length; i++) {
      passtext += Integer.toHexString((int) (pass[i] & 0xFF));
    }
    return passtext;
  }

  private void fillLogin(Request request, LocalizedStringBuffer buffer) {
    buffer.append("<table>");
    buffer.append("<tr>");
    buffer.append(" <td>Име:</td>");
    buffer.append(" <td><select name='login_id'>");

    String[][] users = Database.queryList(2, "SELECT classmate_id, classmate_name FROM project_classmates");
    for (int i = 0; i < users.length; i++) {
      buffer.append("  <option value='");
      buffer.append(users[i][0]);
      buffer.append("'>");
      buffer.append(users[i][1]);
      buffer.append("</option>");
    }
    buffer.append(" </select></td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append(" <td>Парола:</td>");
    buffer.append(" <td><input name='login_password' type='password' size='8' /></td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append(" <td colspan='2'>");
    buffer.append("  <input type='submit' value='Давай' />");
    buffer.append(" </td>");
    buffer.append("</tr>");
    buffer.append("</table>");
  }

  private void fillPasswordChange(Request request, LocalizedStringBuffer buffer) {
    String classmate = request.get("login_id");

    buffer.append("<table>");
    buffer.append("<tr>");
    buffer.append(" <td>Име:</td>");
    buffer.append(" <td>");
    buffer.append(Database.query("SELECT classmate_name FROM project_classmates WHERE classmate_id = " + classmate));
    buffer.append(" </td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append(" <td>Парола:</td>");
    buffer.append(" <td><input name='login_password' type='password' size='8' /></td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append(" <td>Нова парола:</td>");
    buffer.append(" <td><input name='login_password_1' type='password' size='8' /></td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append(" <td>Повтори я:</td>");
    buffer.append(" <td><input name='login_password_2' type='password' size='8' /></td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append(" <td colspan='2'>");
    buffer.append("  <input type='submit' value='Давай' />");
    buffer.append(" </td>");
    buffer.append("</tr>");
    buffer.append("</table>");
    buffer.append("<input type='hidden' name='login_id' value='" + classmate + "' />");
  }

  private void fillRequestCode(Request request, LocalizedStringBuffer buffer) {
    buffer.append("Ако не си знаеш паролата, <a href='javascript:alert(\"Не е готово още това ...\")'>натисни тук</a>, за да получиш временна парола.");
  }

}
