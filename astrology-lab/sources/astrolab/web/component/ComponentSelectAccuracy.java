package astrolab.web.component;

import astrolab.db.Event;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentSelectAccuracy {

  private final static String[] ENUMERATION = new String[] {
    Event.ACCURACY_SECOND,
    Event.ACCURACY_MINUTE,
    Event.ACCURACY_5_MINUTES,
    Event.ACCURACY_10_MINUTES,
    Event.ACCURACY_30_MINUTES,
    Event.ACCURACY_HOUR,
    Event.ACCURACY_FEW_HOURS,
    Event.ACCURACY_DAY,
    Event.ACCURACY_WEEK,
    Event.ACCURACY_MONTH,
    Event.ACCURACY_YEAR
  };

  public static void fill(LocalizedStringBuffer buffer) {
    buffer.append("\r\n<select id='" + Request.CHOICE_ACCURACY + "' name='" + Request.CHOICE_ACCURACY + "'>");
    for (int i = 0; i < ENUMERATION.length; i++) {
      buffer.append("\r\n\t<option value='" + ENUMERATION[i] + "'>");
      buffer.localize(ENUMERATION[i]);
      buffer.append("</option>");
    }
    buffer.append("\r\n</select>");
  }

  public static void fill(LocalizedStringBuffer buffer, String selected) {
    buffer.append("\r\n<select id='" + Request.CHOICE_ACCURACY + "' name='" + Request.CHOICE_ACCURACY + "'>");
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
    return request.get(Request.CHOICE_ACCURACY);
  }
}
