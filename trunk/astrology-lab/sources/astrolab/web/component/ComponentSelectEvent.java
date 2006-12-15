package astrolab.web.component;

import java.util.Properties;

import astrolab.db.Event;
import astrolab.db.EventIterator;
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
      StringBuffer selectionBuffer = new StringBuffer();
      Properties parameters = new Properties();

      for (int s = 0; s < position - 1; s++) {
        selectionBuffer.append(selection[s]);
        selectionBuffer.append(":");
      }
  
      selectionBuffer.append(event);

      for (int s = position; s < selection.length; s++) {
        selectionBuffer.append(":");
        selectionBuffer.append(selection[s]);
      }

      parameters.put("_m", String.valueOf(Modify.getId(SelectEvent.class)));
      parameters.put("_s", selectionBuffer.toString());
      ComponentLink.fillLink(buffer, request.getDisplay().getClass(), parameters, String.valueOf(position));
    }
    buffer.append("</td>");
    return isSelected;
  }

}
