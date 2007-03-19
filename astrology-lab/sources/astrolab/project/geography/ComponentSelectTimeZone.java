package astrolab.project.geography;

import java.util.TimeZone;

import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentSelectTimeZone {

  private final static String CHOICE_TIME_ZONE = "_time_zone";
  private final static String[] ENUMERATION = TimeZone.getAvailableIDs();

  public static void fill(LocalizedStringBuffer buffer) {
    buffer.append("\r\n<select id='" + CHOICE_TIME_ZONE + "' name='" + CHOICE_TIME_ZONE + "'>");
    for (int i = 0; i < ENUMERATION.length; i++) {
      buffer.append("\r\n\t<option value='" + ENUMERATION[i] + "'>");
      buffer.append(ENUMERATION[i]);
      buffer.append("</option>");
    }
    buffer.append("\r\n</select>");
  }

  public static void fill(LocalizedStringBuffer buffer, String selected) {
    buffer.append("\r\n<select id='" + CHOICE_TIME_ZONE + "' name='" + CHOICE_TIME_ZONE + "'>");
    for (int i = 0; i < ENUMERATION.length; i++) {
      buffer.append("\r\n\t<option value='" + ENUMERATION[i] + "'");
      if (ENUMERATION[i].equals(selected)) {
        buffer.append(" selected='true'");
      }
      buffer.append(">");
      buffer.append(ENUMERATION[i]);
      buffer.append("</option>");
    }
    buffer.append("\r\n</select>");
  }

  public static TimeZone retrieve(Request request) {
    return TimeZone.getTimeZone(request.get(CHOICE_TIME_ZONE));
  }
}
