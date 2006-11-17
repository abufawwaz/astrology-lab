package astrolab.web.component;

import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentSelectChoice {

  public static void fill(LocalizedStringBuffer buffer, String[] enumeration, String selected, String choice_id) {
    buffer.append("\r\n<select name='" + choice_id + "'>");
    for (int i = 0; i < enumeration.length; i++) {
      buffer.append("\r\n\t<option value='" + enumeration[i] + "'");
      if (enumeration[i].equals(selected)) {
        buffer.append(" SELECTED");
      }
      buffer.append(" />");
      buffer.localize(enumeration[i]);
    }
    buffer.append("\r\n</select>");
  }

  public static void fill(LocalizedStringBuffer buffer, String[] enumeration, String[] values, String selected, String choice_id) {
    buffer.append("\r\n<select name='" + choice_id + "'>");
    for (int i = 0; i < enumeration.length; i++) {
      buffer.append("\r\n\t<option value='" + values[i] + "'");
      if (enumeration[i].equalsIgnoreCase(selected)) {
        buffer.append(" SELECTED");
      }
      buffer.append(" />");
      buffer.localize(enumeration[i]);
    }
    buffer.append("\r\n</select>");
  }

  public static String retrieve(Request request, String choice_id) {
    return request.get(choice_id);
  }
}
