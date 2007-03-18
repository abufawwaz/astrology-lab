package astrolab.web.component;

import astrolab.db.Event;
import astrolab.db.EventIterator;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentSelectEvent {

  public static void fill(LocalizedStringBuffer buffer, Request request, EventIterator events) {
    buffer.append("\r\n<table border='1'>");
    fillTableHeader(buffer);

    while (events.hasNext()) {
      buffer.append("\r\n");
      Event event = (Event) events.next();
      fill(buffer, request, event);
    }
    buffer.append("</table>");
  }

  private static void fillTableHeader(LocalizedStringBuffer buffer) {
    buffer.append("<tr>");

    buffer.append("<th>");
    buffer.localize("Name");
    buffer.append("</th>");

    buffer.append("<th>");
    buffer.localize("Time");
    buffer.append("</th>");

    buffer.append("<th>");
    buffer.localize("Location");
    buffer.append("</th>");

    buffer.append("</tr>");
  }

  private static void fill(LocalizedStringBuffer buffer, Request request, Event event) {
    if (event.getEvent() == null) {
      return;
    }

    buffer.append("<tr>");

    String controlId = "controller_" + event.getId(); 
    ComponentController.fill(buffer, controlId, "Event", String.valueOf(event.getId()));
    buffer.append("<td id='");
    buffer.append(controlId);
    buffer.append("'>");
    buffer.append(event.getEvent());
    buffer.append("</td>");

    buffer.append("<td>");
    buffer.append(event.getTime().toString());
    buffer.append("</td>");

    buffer.append("<td>");
    buffer.append(event.getLocation().toString());
    buffer.append("</td>");

    buffer.append("</tr>");
  }

}
