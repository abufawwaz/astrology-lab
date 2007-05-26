package astrolab.web.component.chart;

import astrolab.astronom.houses.HouseSystem;
import astrolab.astronom.houses.PlacidusSystem;
import astrolab.db.Event;
import astrolab.project.geography.Location;
import astrolab.web.Display;
import astrolab.web.SVGDisplay;
import astrolab.web.component.ComponentControllable;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class Chart extends SVGDisplay {

  private final static String KEY_CHART_EVENT = "Chart.event";
  private final static String KEY_CHART_TIMESTAMP = "Chart.timestamp";

  public final static int ID = Display.getId(Chart.class);

  public Chart() {
    super.addAction("event", "user.session.event.1");
    super.addAction("timestamp", KEY_CHART_TIMESTAMP);
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    Event event = getSelectedEvent();
		HouseSystem houses = new PlacidusSystem(event);
		double offset = houses.getHouse(1);

    String controlId = "chart"; 
    ComponentControllable.fill(buffer, controlId, "Event", "function(chartEvent) { window.location.href='chart.svg?_d=" + ID + "&amp;" + KEY_CHART_EVENT + "=' + chartEvent }");
		buffer.append("<svg id='" + controlId + "' width=\"100%\" height=\"100%\" viewBox=\"0 0 500 500\" preserveAspectRatio=\"xMidYMid meet\">");

    new ChartPartInfo().fillContent(request, buffer, event);
    new ChartPartZodiac().fillContent(request, buffer, offset);
		new ChartPartHouses().fillContent(request, buffer, event, offset, false);
    new ChartPartPlanets().fillContent(request, buffer, event, offset, false);
    new ChartPartAspects().fillContent(request, buffer, event, offset, false);

    buffer.append("</svg>");
	}

  private final static Event getSelectedEvent() {
    String chartEvent = Request.getCurrentRequest().get(KEY_CHART_EVENT);
    String chartTimestamp = Request.getCurrentRequest().get(KEY_CHART_TIMESTAMP);
    if ((chartEvent != null) && (chartEvent.trim().length() > 0)) {
      return new Event(Integer.parseInt(chartEvent));
    } else if ((chartTimestamp != null) && (chartTimestamp.trim().length() > 0)) {
      return new Event(Location.getLocation(1001003), Long.parseLong(chartTimestamp));
    } else {
      return Event.getSelectedEvent();
    }
  }

}
