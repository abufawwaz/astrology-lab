package astrolab.web.server.content;

import astrolab.perspective.Perspective;
import astrolab.web.HTMLDisplay;
import astrolab.web.server.Request;

public class MenuPage extends HTMLDisplay {

  public MenuPage() {
    super("Perspectives");
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    int[] ids = Perspective.listPerspectives();

    for (int i = 0; i < ids.length; i++) {
      buffer.append("<a href='' onclick=\"javascript:top.switchPerspective('");
      buffer.append(ids[i]);
      buffer.append("')\">");
      buffer.localize(ids[i]);
      buffer.append("</a>");
      buffer.append("\r\n<br />");
    }
  }

}
