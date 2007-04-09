package astrolab.project.election;

import astrolab.web.HTMLDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayElectionaryRestrictionList extends HTMLDisplay {

  public DisplayElectionaryRestrictionList() {
    super("Restriction Criteria");
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("From: 01.01.2007");
    buffer.append("<br />");
    buffer.append("To: 01.01.2007");
    buffer.append("<br />");

    final String[] DAY = new String[] { "m", "t", "w", "t", "f", "s", "s" };
    buffer.append("<table border='0' cellspacing='0' cellpadding='0'>");
    buffer.append("<tr>");
    for (int i = 0; i < 7; i++) {
      buffer.append("<th>");
      buffer.append(DAY[i]);
      buffer.append("</th>");
    }
    buffer.append("</tr>");
    buffer.append("<tr>");
    for (int j = 0; j < 7; j++) {
      buffer.append("<td>");
      buffer.append("<input type='checkbox' />");
      buffer.append("</td>");
    }
    buffer.append("</tr>");
    buffer.append("</table>");
    buffer.append("<br />");

    buffer.append("<table border='0' cellspacing='0' cellpadding='0'>");
    buffer.append("<tr>");
    buffer.append("<th>");
    buffer.append("</th>");
    for (int j = 0; j < 12; j++) {
      buffer.append("<th>");
      buffer.append(String.valueOf(j));
      buffer.append("</th>");
    }
    buffer.append("</tr>");
    for (int i = 0; i < 2; i++) {
      buffer.append("<tr>");
      buffer.append("<td>");
      buffer.append((i == 0) ? "AM" : "PM");
      buffer.append("</td>");
      for (int j = 0; j < 12; j++) {
        buffer.append("<td>");
        buffer.append("<input type='checkbox' />");
        buffer.append("</td>");
      }
      buffer.append("</tr>");
    }
    buffer.append("</table>");
    buffer.append("<br />");
  }

}