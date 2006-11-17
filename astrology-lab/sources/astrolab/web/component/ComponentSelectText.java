package astrolab.web.component;

import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentSelectText {

  public static void fill(LocalizedStringBuffer buffer, String choice_id) {
    buffer.append("<input type='text' name='" + choice_id + "' value='");
    buffer.localize("... not set ...");
    buffer.append("' />");
  }

  public static String retrieve(Request request, String choice_id) {
    return request.get(choice_id);
  }
}
