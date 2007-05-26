package astrolab.web.component.chart;

import astrolab.db.Event;
import astrolab.web.SVGDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ChartPartInfo extends SVGDisplay {

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    fillContent(request, buffer, Event.getSelectedEvent());
	}

  public void fillContent(Request request, LocalizedStringBuffer buffer, Event event) {
    if (event.getEventId() > 0) {
      buffer.append("<text y='20'>");
      buffer.localize(event.getEvent());
      buffer.append("</text>");
    }

    buffer.append("<text y='40'>");
    buffer.localize(event.getTime().toSimpleString());
    buffer.append("</text>");

    buffer.append("<text y='60'>");
    buffer.localize(event.getLocation().getId());
    buffer.append("</text>");
	}

}
