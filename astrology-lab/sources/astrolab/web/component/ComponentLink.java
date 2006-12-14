package astrolab.web.component;

import astrolab.web.Display;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentLink {

  public static void fillLink(LocalizedStringBuffer buffer, Class display, String text) {
    if (Display.class.isAssignableFrom(display)) {
      buffer.append("<a href='display.html?_d="); // TODO: html or xhtml?
      buffer.append(Display.getId(display));
      buffer.append("'>");
      buffer.localize(text);
      buffer.append("</a>");
    }
  }

}