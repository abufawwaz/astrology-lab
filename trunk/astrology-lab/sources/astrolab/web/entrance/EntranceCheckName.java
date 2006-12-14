package astrolab.web.entrance;

import astrolab.db.Event;
import astrolab.db.Personalize;
import astrolab.db.Text;
import astrolab.web.HTMLDisplay;
import astrolab.web.component.ComponentLink;
import astrolab.web.project.archive.natal.FormEditNatalRecord;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

class EntranceCheckName extends HTMLDisplay {

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    int user = Personalize.getUser(true);
    String userName = Text.getText(user);
    boolean anonymous = ((userName == null) || userName.startsWith("User"));

    if (!anonymous) {
      buffer.localize(user);
    } else {
      Event.setSelectedEvent(Personalize.getUser(false), 1);
      ComponentLink.fillLink(buffer, FormEditNatalRecord.class, "enter");
      buffer.append("<br />");
      buffer.append("<font color='red'>");
      buffer.localize("Enter your name so that other participants can see it!");
      buffer.append("</font>");
      buffer.append("<br />");
      buffer.append("If you already are registered, please, go to your mail inbox an use the entrance link.");
      buffer.append("<br />");
      buffer.append("If you don't have an entrance link you either are not registered or you have not provided a valid e-mail.");
      buffer.append("<br />");
    }
  }

}
