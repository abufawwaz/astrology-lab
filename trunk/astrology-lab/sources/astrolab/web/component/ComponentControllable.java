package astrolab.web.component;

import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentControllable {

  public static void fill(LocalizedStringBuffer buffer, String controllableId, String type, String functionAction) {
    buffer.append("\r\n<script language='javascript'>");

    buffer.append("\r\n var controller = new top.Controllable(document, '");
    buffer.append(controllableId);
    buffer.append("', new Array('");
    buffer.append(type);
    buffer.append("'), ");
    buffer.append(functionAction); // control function
    buffer.append(", ");
    buffer.append("function() {}"); // action function
    buffer.append(");");

    buffer.append("\r\n</script>");
  }

}