package astrolab.web.component;

import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentSubmitButton {

  public static void fill(LocalizedStringBuffer buffer) {
    buffer.append("<input type='submit' value='");
    buffer.localize("Save");
    buffer.append("' />");
  }

}
