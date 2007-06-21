package astrolab.web.component.chart;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.SpacetimeEvent;
import astrolab.astronom.planet.SolarSystem;
import astrolab.db.Event;
import astrolab.db.Text;
import astrolab.web.SVGDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

// TODO: turn into ChartPartActivePoints
public class ChartPartPlanets extends SVGDisplay {

  private final static double OFFSET_DELTA = Math.PI / 180;

	private double radius, x, y;

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
		fillContent(request, buffer, Event.getSelectedEvent(), 0.0, true);
	}

	public void fillContent(Request request, LocalizedStringBuffer buffer, SpacetimeEvent event, double offset, boolean ownImage) {
    radius = request.getConstraints().getRadius();
		x = request.getConstraints().getWidth() / 2;
		y = request.getConstraints().getHeight() / 2;

		for (String planet: SolarSystem.PLANETS) {
			fillPlanet(ActivePoint.getActivePoint(planet, event), offset, buffer);
		}

// TODO: restore gamma
//		PointGamma gamma = new PointGamma(60, PointGamma.START_FROM_ARIES);
//		gamma.initialize(event);
//		position = gamma.getPosition(Calendar.getInstance());
//		fillPlanet(gamma, (position - offset) * OFFSET_DELTA, -1, buffer);
	}

	private void fillPlanet(ActivePoint point, double globalOffset, LocalizedStringBuffer buffer) {
    double offset = (point.getPosition() - globalOffset) * OFFSET_DELTA;
		buffer.append("\r\n\t<g transform='translate(" + ((int) (x - (radius - 90) * Math.cos(offset))) + ", " + ((int) (y + (radius - 90) * Math.sin(offset))) + ") scale(0.1)'>");
    buffer.append(Text.getCenteredSVGText(Text.getId(point.getName())));
		buffer.append("\r\n\t</g>");
	}

}
