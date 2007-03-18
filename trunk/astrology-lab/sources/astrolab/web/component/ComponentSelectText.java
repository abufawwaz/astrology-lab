package astrolab.web.component;

import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentSelectText {
  
  public static void fill(LocalizedStringBuffer buffer, String choice_id, String value, String label) {
    buffer.append("<input class='class_input' type='text' name='");
    buffer.append(choice_id);
    buffer.append("' ");

    if (value != null) {
      buffer.append("value='");
      buffer.localize(value);
      buffer.append("' ");
    }

    if (label != null) {
      buffer.append("title='");
      buffer.append(label);
      buffer.append("' ");
    }

    buffer.append("/>");
  }

  /**
   * @deprecated
   */
  public static void fill(LocalizedStringBuffer buffer, String choice_id) {
    ComponentSelectText.fill(buffer, choice_id, null, null);
  }

  public static String retrieve(Request request, String choice_id) {
    return request.get(choice_id);
  }
}
