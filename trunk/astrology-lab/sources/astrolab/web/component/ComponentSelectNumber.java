package astrolab.web.component;

import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentSelectNumber {

  public static void fill(LocalizedStringBuffer buffer, String choice_id) {
    buffer.append("<input type='text' name='" + choice_id + "' value='0' size='5' />");
  }

  public static double retrieve(Request request, String choice_id) {
    try {
      return Double.parseDouble(request.get(choice_id));
    } catch (Exception e) {
      e.printStackTrace();
      return 0.0;
    }
  }
}
