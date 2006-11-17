package astrolab.project.match;

import astrolab.astronom.util.Zodiac;
import astrolab.web.HTMLDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class GeneralSignMatchView extends HTMLDisplay {

  public GeneralSignMatchView() {
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("<table border='1'>");
    buffer.append("<tr><td></td>");
    for (int i = 0; i < 12; i++) {
      buffer.append("<td>");
      buffer.append(Zodiac.toString(i * 30, "ZZZ"));
      buffer.append("</td>");
    }
    buffer.append("</tr>");
    for (int i = 0; i < 12; i++) {
      buffer.append("<tr><td>");
      buffer.append(Zodiac.toString(i * 30, "ZZZ"));
      buffer.append("</td>");
      for (int j = 0; j < 12; j++) {
        buffer.append("<td>");
        buffer.append(String.valueOf(FactorMatch.getMatch(i, j)));
        buffer.append("</td>");
      }
      buffer.append("</tr>");
    }
    buffer.append("</table>");
  }
}