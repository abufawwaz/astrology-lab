package astrolab.web.entrance;

import astrolab.db.Personalize;
import astrolab.web.HTMLDisplay;
import astrolab.web.component.ComponentLink;
import astrolab.web.component.help.FormSendInvitation;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

class EntranceCheckEmail extends HTMLDisplay {

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    String email = Personalize.getEmail();

    if (email != null) {
      buffer.append(email);
    } else {
      ComponentLink.fillLink(buffer, FormSendInvitation.class, "enter");
      buffer.append("<br />");
      buffer.append("<font color='red'>");
      buffer.localize("Enter your e-mail address to activate your account!");
      buffer.append("</font>");
      buffer.append("<br />");
      buffer.append("You have not provided valid e-mail address! Until you do so you will not be able to use your profile from another browser or after you clear your browser's cookies."); //TODO: localize
    }
  }

}
