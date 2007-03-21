package astrolab.project.geography;

import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentSelectLattitude {

  private final static String CHOICE_LATTITUDE = "_lattitude";

  public static void fill(LocalizedStringBuffer buffer, double value) {
    buffer.append("\r\n<script language='JavaScript'>");
    buffer.append("\r\nfunction recalculatelattitude() {");
    buffer.append("\r\n var lattitude = eval('' + document.getElementById('__lattitude_1').value + ' + (' + document.getElementById('__lattitude_3').value + ' + 0.001) / 60')");
    buffer.append("\r\n if ('S' == document.getElementById('__lattitude_2').value) {");
    buffer.append("\r\n  lattitude = -lattitude");
    buffer.append("\r\n }");
    buffer.append("\r\n document.getElementById('" + CHOICE_LATTITUDE + "').value = lattitude");
    buffer.append("\r\n}");
    buffer.append("\r\n</script>");
    buffer.append("\r\n<input type='hidden' id='" + CHOICE_LATTITUDE + "' name='" + CHOICE_LATTITUDE + "' />");

    int value1 = (int) Math.abs(value);
    buffer.append("\r\n<select id='__lattitude_1' onchange='recalculatelattitude(); return false;'>");
    for (int i = 0; i < 90; i++) {
      buffer.append("\r\n\t<option value='" + i + "' " + ((value1 == i) ? "selected='true'" : "") + ">" + i + "</option>");
    }
    buffer.append("\r\n</select>");
    buffer.append("\r\n<select id='__lattitude_2' onchange='recalculatelattitude(); return false;'>");
    buffer.append("\r\n\t<option value='N' " + ((value >= 0) ? "selected='true'" : "") + ">N</option>");
    buffer.append("\r\n\t<option value='S' " + ((value < 0) ? "selected='true'" : "") + ">S</option>");
    buffer.append("\r\n</select>");

    int value2 = (int) (Math.abs(value - (int) value) * 60);
    buffer.append("\r\n<select id='__lattitude_3' onchange='recalculatelattitude(); return false;'>");
    for (int i = 0; i < 60; i++) {
      buffer.append("\r\n\t<option value='" + i + "' " + ((value2 == i) ? "selected='true'" : "") + ">" + i + "</option>");
    }
    buffer.append("\r\n</select>");
  }

  public static double retrieve(Request request) {
    return request.getParameters().getDouble(CHOICE_LATTITUDE);
  }
}
