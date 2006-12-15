package astrolab.web.component;

import astrolab.db.Event;
import astrolab.db.EventIterator;
import astrolab.web.Display;
import astrolab.web.Modify;
import astrolab.web.project.archive.SelectEvent;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentSelectEvent {

  public static void fill(LocalizedStringBuffer buffer, Request request, int[] events) {
    buffer.append("\r\n<table border=\"1\">");

    for (int i = 0; i < events.length; i++) {
      Event event = new Event(events[i]);
      fill(buffer, request, event);
    }
    buffer.append("</table>");
  }

  public static void fill(LocalizedStringBuffer buffer, Request request, EventIterator events) {
    buffer.append("\r\n<table border=\"1\">");

    while (events.hasNext()) {
      Event event = (Event) events.next();
      fill(buffer, request, event);
    }
    buffer.append("</table>");
  }

  private static void fill(LocalizedStringBuffer buffer, Request request, Event event) {
    if (event.getEvent() == null) {
      return;
    }

    boolean isSelected = false;
    buffer.append("\r\n<tr>");

    for (int s = 1; s < 3; s++) {
      isSelected |= fillButton(buffer, request, event.getEventId(), s);
    }

    buffer.append("<td>");
    if (isSelected) {
      buffer.append("<b><font color='green'>");
    }
    buffer.append(event.getEvent());
    if (isSelected) {
      buffer.append("</font></b>");
    }
    buffer.append("</td>");

    buffer.append("<td>");
    buffer.append(event.getLocation().toString());
    buffer.append("</td>");

    buffer.append("<td>");
    buffer.append(event.getTime().toString());
    buffer.append("</td>");

    buffer.append("<td>");
    buffer.localize(event.getAccuracy());
    buffer.append("</td>");

    buffer.append("<td>");
    buffer.localize(event.getSource());
    buffer.append("</td>");

    buffer.append("</tr>");
  }

  private static final boolean fillButton(LocalizedStringBuffer buffer, Request request, int event, int position) {
    int[] selection = Event.getSelectedEvents();
    boolean isSelected = (selection[position - 1] == event);

    buffer.append("<td>");
    if (isSelected) {
      buffer.append("<b>");
      buffer.append(position);
      buffer.append("</b>");
    } else {
      buffer.append("<a href='root.html?_d=");
      buffer.append(Display.getId(request.getDisplay().getClass()));

      buffer.append("&amp;_m=");
      buffer.append(Modify.getId(SelectEvent.class));
      buffer.append("&amp;_s=");

      for (int s = 0; s < position - 1; s++) {
        buffer.append(selection[s]);
        buffer.append(":");
      }
  
      buffer.append(event);

      for (int s = position; s < selection.length; s++) {
        buffer.append(":");
        buffer.append(selection[s]);
      }

      buffer.append("'>");
      buffer.append(position);
      buffer.append("</a>");
    }
    buffer.append("</td>");
    return isSelected;
  }

}
