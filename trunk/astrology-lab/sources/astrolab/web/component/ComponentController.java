package astrolab.web.component;

import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentController {

  public static void fill(LocalizedStringBuffer buffer, String controllerId, String type, String command) {
    buffer.append("\r\n<script language='javascript'>");

    buffer.append("\r\n var controller = new top.Controller(document, '");
    buffer.append(type);
    buffer.append("', '");
    buffer.append(controllerId);
    buffer.append("', '");
    buffer.append(command);
    buffer.append("');");

    buffer.append("\r\n</script>");
  }

}