package astrolab.project.geography;

import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentSelectLongitude {

  public final static String CHOICE_LONGITUDE = "_longitude";

  public static void fill(LocalizedStringBuffer buffer, double value) {
    buffer.append("\r\n<script language='JavaScript'>");
    buffer.append("\r\nfunction recalculateLongitude() {");
    buffer.append("\r\n var longitude = eval('' + document.getElementById('__longitude_1').value + ' + (' + document.getElementById('__longitude_3').value + ' + 0.001) / 60')");
    buffer.append("\r\n if ('E' == document.getElementById('__longitude_2').value) {");
    buffer.append("\r\n  longitude = -longitude");
    buffer.append("\r\n }");
    buffer.append("\r\n document.getElementById('" + CHOICE_LONGITUDE + "').value = longitude");
    buffer.append("\r\n}");
    buffer.append("\r\n</script>");
    buffer.append("\r\n<input type='hidden' id='" + CHOICE_LONGITUDE + "' name='" + CHOICE_LONGITUDE + "' />");

    int value1 = (int) Math.abs(value);
    buffer.append("\r\n<select id='__longitude_1' onchange='recalculateLongitude(); return false;'>");
    for (int i = 0; i < 180; i++) {
      buffer.append("\r\n\t<option value='" + i + "' " + ((value1 == i) ? "selected='true'" : "") + ">" + i + "</option>");
    }
    buffer.append("\r\n</select>");
    buffer.append("\r\n<select id='__longitude_2' onchange='recalculateLongitude(); return false;'>");
    buffer.append("\r\n\t<option value='E' " + ((value < 0) ? "selected='true'" : "") + ">E</option>");
    buffer.append("\r\n\t<option value='W' " + ((value >= 0) ? "selected='true'" : "") + ">W</option>");
    buffer.append("\r\n</select>");

    int value2 = (int) (Math.abs(value - (int) value) * 60);
    buffer.append("\r\n<select id='__longitude_3' onchange='recalculateLongitude(); return false;'>");
    for (int i = 0; i < 60; i++) {
      buffer.append("\r\n\t<option value='" + i + "' " + ((value2 == i) ? "selected='true'" : "") + ">" + i + "</option>");
    }
    buffer.append("\r\n</select>");
  }

  public static double retrieve(Request request) {
    String value = request.get(CHOICE_LONGITUDE);
    return (value.length() > 0) ? Double.parseDouble(request.get(CHOICE_LONGITUDE)) : 0;
  }
}
