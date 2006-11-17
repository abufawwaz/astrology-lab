package astrolab.web.project.archive;

import astrolab.db.Event;
import astrolab.web.HTMLDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class TransformEventView extends HTMLDisplay {

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
  	Event event = Event.getSelectedEvent();
    buffer.append("<table>");
    buffer.append("<tr><td>");
    buffer.localize(event.getType());
    buffer.append(":</td><td>");
    buffer.append(event.getSubject());
    buffer.append("</td></tr>");

    if (event.getSubjectId() != event.getEventId()) {
      buffer.append("<tr><td>");
      buffer.localize("Event");
      buffer.append(":</td><td>");
      buffer.append(event.getEvent());
      buffer.append("</td></tr>");
    }

    buffer.append("<tr><td>");
    buffer.localize("Time of occurance");
    buffer.append(":</td><td>");
    buffer.append(event.getTime().toString());
    buffer.append("</td></tr>");
    buffer.append("<tr><td>");
    buffer.localize("Accuracy");
    buffer.append(":</td><td>");
    buffer.localize(event.getAccuracy());
    buffer.append("</td></tr>");
    buffer.append("<tr><td>");
    buffer.localize("Source");
    buffer.append(":</td><td>");
    buffer.localize(event.getSource());
    buffer.append("</td></tr>");
    buffer.append("<tr><td>");
    buffer.localize("Location");
    buffer.append(":</td><td>");
    buffer.append(event.getLocation().toString());
    buffer.append("</td></tr>");
    buffer.append("</table>");
  }

}