package astrolab.web.component;

import java.util.Enumeration;
import java.util.Properties;

import astrolab.web.Display;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentLink {

  public final static Properties NO_PARAMETERS = new Properties();

  public static void fillLink(LocalizedStringBuffer buffer, Class display, String text) {
    fillLink(buffer, display, NO_PARAMETERS, text);
  }

  public static void fillLink(LocalizedStringBuffer buffer, Class display, Properties parameters, String text) {
    if (Display.class.isAssignableFrom(display)) {
      buffer.append("<a href='display.html?_d="); // TODO: html or xhtml?
      buffer.append(Display.getId(display));

      Enumeration keys = parameters.keys();
      while (keys.hasMoreElements()) {
        String key = (String) keys.nextElement();
        buffer.append("&amp;");
        buffer.append(key);
        buffer.append("=");
        buffer.append(parameters.getProperty(key));
      }
      buffer.append("'>");
      buffer.localize(text);
      buffer.append("</a>");
    }
  }

}