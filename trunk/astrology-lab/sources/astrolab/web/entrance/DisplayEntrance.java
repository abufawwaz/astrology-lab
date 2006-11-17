package astrolab.web.entrance;

import astrolab.web.HTMLDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayEntrance extends HTMLDisplay implements Entrance {

  private static Entrance[] entrance = new Entrance[] {
    new EntranceUnknownBrowser(),
    new EntranceUnknownName(),
    new EntranceUnknownEmail(),
    new EntranceCheckForSVGViewer()
  };

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    if (isApplicable(request)) {
      buffer.append("<form name='enter_data' method='get' action=''>");
      buffer.append("<input type='hidden' name='entrance.action' value='enter_data' />");

      for (int i = 0; i < entrance.length; i++) {
        if (entrance[i].isApplicable(request)) {
          ((HTMLDisplay) entrance[i]).fillBodyContent(request, buffer);
          buffer.append("<input type='hidden' name='entrance." + i + "' value='yes' />");
          buffer.append("<hr />");
        }
      }

      buffer.append("<br /><a href='javascript:document.forms[\"enter_data\"].submit()'>Submit</a>");
      buffer.append("</form>");
    }
  }

  public boolean isApplicable(Request request) {
    modify(request);

    for (int i = 0; i < entrance.length; i++) {
      if (entrance[i].isApplicable(request)) {
        return true;
      }
    }
    return false;
  }

  public void modify(Request request) {
    for (int i = 0; i < entrance.length; i++) {
      if ("yes".equals(request.get("entrance." + i))) {
        entrance[i].modify(request);
      }
    }
  }

}
