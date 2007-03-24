package astrolab.web.component;

import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentSelectNumber {

  public static void fill(LocalizedStringBuffer buffer, String choice_id) {
    buffer.append("<input type='text' name='" + choice_id + "' value='0' size='5' />");
  }

  public static void fillHidden(LocalizedStringBuffer buffer, String choice_id, double value) {
    buffer.append("<input type='hidden' name='" + choice_id + "' value='" + value + "' size='5' />");
  }

  public static double retrieve(Request request, String choice_id) {
    try {
      String value = request.get(choice_id);
      return (value != null) ? Double.parseDouble(request.get(choice_id)) : 0.0;
    } catch (Exception e) {
      e.printStackTrace();
      return 0.0;
    }
  }

  public static void fillReset(LocalizedStringBuffer buffer, String choice_id) {
    buffer.append("document.forms[0]." + choice_id + ".value=\"\";");
  }
}
