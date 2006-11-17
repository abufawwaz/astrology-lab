package astrolab.web.component.location;

import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentSelectLattitude {

  public static void fill(LocalizedStringBuffer buffer) {
    buffer.append("\r\n<script language='JavaScript'>");
    buffer.append("\r\nfunction recalculatelattitude() {");
    buffer.append("\r\n var lattitude = eval('' + document.getElementById('__lattitude_1').value + ' + (' + document.getElementById('__lattitude_3').value + ' + 0.001) / 60')");
    buffer.append("\r\n if ('S' == document.getElementById('__lattitude_2').value) {");
    buffer.append("\r\n  lattitude = -lattitude");
    buffer.append("\r\n }");
    buffer.append("\r\n document.getElementById('" + Request.CHOICE_LATTITUDE + "').value = lattitude");
    buffer.append("\r\n}");
    buffer.append("\r\n</script>");
    buffer.append("\r\n<input type='hidden' id='" + Request.CHOICE_LATTITUDE + "' name='" + Request.CHOICE_LATTITUDE + "' />");
    buffer.append("\r\n<select id='__lattitude_1' onchange='recalculatelattitude(); return false;'>");
    for (int i = 0; i < 90; i++) {
      buffer.append("\r\n\t<option value='" + i + "' />" + i);
    }
    buffer.append("\r\n</select>");
    buffer.append("\r\n<select id='__lattitude_2' onchange='recalculatelattitude(); return false;'>");
    buffer.append("\r\n\t<option value='N' />N");
    buffer.append("\r\n\t<option value='S' />S");
    buffer.append("\r\n</select>");
    buffer.append("\r\n<select id='__lattitude_3' onchange='recalculatelattitude(); return false;'>");
    for (int i = 0; i < 60; i++) {
      buffer.append("\r\n\t<option value='" + i + "' />" + i);
    }
    buffer.append("\r\n</select>");
  }

  public static double retrieve(Request request) {
    String value = request.get(Request.CHOICE_LATTITUDE);
    return (value.length() > 0) ? Double.parseDouble(request.get(Request.CHOICE_LATTITUDE)) : 0;
  }
}
