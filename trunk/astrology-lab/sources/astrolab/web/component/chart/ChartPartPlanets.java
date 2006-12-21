package astrolab.web.component.chart;

import java.util.Calendar;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.fiction.PointGamma;
import astrolab.astronom.planet.Planet;
import astrolab.astronom.planet.SolarSystem;
import astrolab.db.Event;
import astrolab.db.Text;
import astrolab.web.SVGDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

// TODO: turn into ChartPartActivePoints
public class ChartPartPlanets extends SVGDisplay {

  String[] PLANETS = new String[] { SolarSystem.JUPITER, SolarSystem.MARS, SolarSystem.MERCURY, SolarSystem.MOON, SolarSystem.NEPTUNE, SolarSystem.PLUTO, SolarSystem.SATURN, SolarSystem.SUN, SolarSystem.URANUS, SolarSystem.VENUS };
	private final static double OFFSET_DELTA = Math.PI / 180;

	private double radius, x, y;

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
		fillContent(request, buffer, 0.0, true);
	}

	public void fillContent(Request request, LocalizedStringBuffer buffer, double offset, boolean ownImage) {
		radius = request.getConstraints().getRadius();
		x = request.getConstraints().getWidth() / 2;
		y = request.getConstraints().getHeight() / 2;

		double position;
		SolarSystem solar = new SolarSystem();
    Planet earth = solar.getPlanet(SolarSystem.EARTH);
    solar.calculate(Event.getSelectedEvent());

		for (int i = 0; i < PLANETS.length; i++) {
      position = solar.getPlanet(PLANETS[i]).positionAround(earth) - offset;
			fillPlanet(solar.getPlanet(PLANETS[i]), position * OFFSET_DELTA, i, buffer);
		}

		PointGamma gamma = new PointGamma(60, PointGamma.START_FROM_ARIES);
		gamma.initialize(Event.getSelectedEvent());
		position = gamma.getPosition(Calendar.getInstance());
		fillPlanet(gamma, (position - offset) * OFFSET_DELTA, -1, buffer);
	}

	private void fillPlanet(ActivePoint point, double offset, int index, LocalizedStringBuffer buffer) {
		buffer.append("\r\n\t<g transform='translate(" + ((int) (x - (radius - 90) * Math.cos(offset))) + ", " + ((int) (y + (radius - 90) * Math.sin(offset))) + ") scale(0.1)'>");
    buffer.append(Text.getSVG(point.getName()));
		buffer.append("\r\n\t</g>");
	}

}
