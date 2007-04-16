package astrolab.project.classmates;

import java.security.MessageDigest;

import astrolab.db.Database;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class FormClassmatesLogin extends HTMLFormDisplay {

  static int ID = HTMLFormDisplay.getId(FormClassmatesLogin.class);
  static String CLASSMATE_ID = "classmate";

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
    if (error == null) {
      buffer.append("Мда, ти си! Сега може да видиш личните данни.");
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
        return "За да имаш достъп до личните данни въведи паролата си!";
      }

      MessageDigest digest = MessageDigest.getInstance("SHA-1");
      byte[] pass = digest.digest(passwordEntry.getBytes());
      String passtext = "";
      for (int i = 0; i < pass.length; i++) {
        passtext += Integer.toHexString((int) (pass[i] & 0xFF));
      }

      if (Database.query("SELECT * FROM project_classmates WHERE classmate_id = " + classmate + " AND password = '" + passtext + "'") != null) {
        request.getConnection().getOutput().setCookie(CLASSMATE_ID, classmate, false);
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "<font color='red'>Грешна парола!</font>";
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
//    buffer.append("<tr>");
//    buffer.append(" <td>Активационен код:</td>");
//    buffer.append(" <td><input name='login_activation_code' size='8' /></td>");
//    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append(" <td colspan='2'>");
    buffer.append("  <input type='submit' value='Давай' />");
    buffer.append(" </td>");
    buffer.append("</tr>");
    buffer.append("</table>");
  }

  private void fillRequestCode(Request request, LocalizedStringBuffer buffer) {
    buffer.append("Ако не си знаеш паролата, <a href='javascript:alert(\"Не е готово още това ...\")'>натисни тук</a>, за да получиш активационен код.");
  }

}
