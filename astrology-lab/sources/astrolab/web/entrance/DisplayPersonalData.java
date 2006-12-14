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
    new EntranceCheckName().fillBodyContent(request, buffer);
    buffer.append("<hr />");

    buffer.localize("E-mail");
    buffer.append(":");
    new EntranceCheckEmail().fillBodyContent(request, buffer);
    buffer.append("<hr />");

    buffer.localize("SVG Viewer");
    buffer.append(":");
    new EntranceCheckSVGViewer().fillBodyContent(request, buffer);
    buffer.append("<hr />");
  }

}
