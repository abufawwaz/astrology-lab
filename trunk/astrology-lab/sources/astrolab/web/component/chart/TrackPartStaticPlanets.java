package astrolab.web.component.chart;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.SpacetimeEvent;
import astrolab.astronom.planet.SolarSystem;
import astrolab.db.Event;
import astrolab.web.SVGDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class TrackPartStaticPlanets extends SVGDisplay {

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
		fillContent(request, buffer, true);
	}

	public void fillContent(Request request, LocalizedStringBuffer buffer, boolean ownImage) {
    SpacetimeEvent time = Event.getSelectedEvent();

		if (ownImage) {
			buffer.append("<svg:svg version='1.1' baseProfile='full' width='500px' height='500px'>");
		}
		for (String planet: SolarSystem.PLANETS) {
			fillPlanet(ActivePoint.getActivePoint(planet, time), buffer);
		}

		if (ownImage) {
			buffer.append("</svg:svg>");
		}
	}

	public void transform(Request request) {
		request.setDisplay(this);
	}

	private void fillPlanet(ActivePoint point, LocalizedStringBuffer buffer) {
    double position = point.getPosition();

    buffer.append("<svg:line x1='" + (int) position + "' y1='0' x2='" + (int) position + "' y2='" + Track.height + "' style='stroke:black;stroke-width:1' />");
		buffer.append("\r\n\t<svg:g transform='translate(" + ((int) position) + ", 20)'>");
		if (point.getIcon() != null) {
			buffer.append(point.getIcon());
		} else {
			buffer.append("\r\n\t<svg:text x='5' y='5'>" + point.getName() + "</svg:text>");
		}
		buffer.append("\r\n\t</svg:g>");
  }

}
