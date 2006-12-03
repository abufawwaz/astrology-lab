package astrolab.web.component;

import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentSelectChoice {

  public static void fill(LocalizedStringBuffer buffer, Object[] enumeration, Object selected, String choice_id) {
    fill(buffer, enumeration, enumeration, selected, choice_id);
  }

  public static void fill(LocalizedStringBuffer buffer, Object[] enumeration, Object[] values, Object selected, String choice_id) {
    buffer.append("\r\n<select name='" + choice_id + "'>");
    for (int i = 0; i < enumeration.length; i++) {
      buffer.append("\r\n\t<option value='" + values[i].toString() + "'");
      if ((selected != null) && selected.toString().equalsIgnoreCase(enumeration[i].toString())) {
        buffer.append(" selected='true'");
      }
      buffer.append(">");
      buffer.localize(enumeration[i].toString());
      buffer.append("</option>");
    }
    buffer.append("\r\n</select>");
  }

  public static String retrieve(Request request, String choice_id) {
    return request.get(choice_id);
  }
}
