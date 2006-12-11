package astrolab.web.entrance;

import astrolab.db.Personalize;
import astrolab.db.Text;
import astrolab.web.HTMLDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayPersonalData extends HTMLDisplay {

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    int user = Personalize.getUser(true);
    String userName = Text.getText(user);
    boolean anonymous = ((userName == null) || userName.startsWith("User"));
    if (anonymous) {
      userName = Text.getText("Guest");
    } else {
      userName = userName.trim();
      int index = userName.indexOf(' ');
      if (index > 0) {
        userName = userName.substring(0, index);
      }
    }

    buffer.localize("Hello");
    buffer.append(", ");
    buffer.append(userName);
    buffer.append("!<hr />");

    buffer.localize("Name");
    buffer.append(":");
    if (!anonymous) {
      buffer.localize(user);
    } else {
      buffer.append("<a href='javascript:alert(\"Not implemented yet!\")'>");
      buffer.localize("enter");
      buffer.append("</a>");
      buffer.append("<br />");
      buffer.append("<font color='red'>");
      buffer.localize("Enter your name so that other participants can see it!");
      buffer.append("</font>");
    }
    buffer.append("<hr />");

    String email = Personalize.getEmail();
    buffer.localize("E-mail");
    buffer.append(":");
    if (email != null) {
      buffer.append(email);
    } else {
      buffer.append("<a href='javascript:alert(\"Not implemented yet!\")'>");
      buffer.localize("enter");
      buffer.append("</a>");
      buffer.append("<br />");
      buffer.append("<font color='red'>");
      buffer.localize("Enter your e-mail address to activate your account!");
      buffer.append("</font>");
    }
    buffer.append("<hr />");

    buffer.localize("SVG Viewer");
    buffer.append(":");
    buffer.append("<hr />");
  }

}
