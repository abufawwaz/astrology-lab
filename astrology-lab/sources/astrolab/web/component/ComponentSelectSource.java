package astrolab.web.component;

import astrolab.db.Event;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentSelectSource {

  private final static String CHOICE_SOURCE = "_source";

  private final static String[] ENUMERATION = new String[] {
    Event.SOURCE_ACCURATE,
    Event.SOURCE_RECTIFIED,
    Event.SOURCE_RECALLED,
    Event.SOURCE_GUESSED,
    Event.SOURCE_PLANNED
  };

  public static void fill(LocalizedStringBuffer buffer) {
    buffer.append("\r\n<select id='" + CHOICE_SOURCE + "' name='" + CHOICE_SOURCE + "'>");
    for (int i = 0; i < ENUMERATION.length; i++) {
      buffer.append("\r\n\t<option value='" + ENUMERATION[i] + "'>");
      buffer.localize(ENUMERATION[i]);
      buffer.append("</option>");
    }
    buffer.append("\r\n</select>");
  }

  public static void fill(LocalizedStringBuffer buffer, String selected) {
    buffer.append("\r\n<select id=\"" + CHOICE_SOURCE + "\" name='" + CHOICE_SOURCE + "'>");
    for (int i = 0; i < ENUMERATION.length; i++) {
      buffer.append("\r\n\t<option value='" + ENUMERATION[i] + "'");
      if (ENUMERATION[i].equals(selected)) {
        buffer.append(" selected='true'");
      }
      buffer.append(">");
      buffer.localize(ENUMERATION[i]);
      buffer.append("</option>");
    }
    buffer.append("\r\n</select>");
  }

  public static String retrieve(Request request) {
    return request.get(CHOICE_SOURCE);
  }
}
