package astrolab.web.component.chart;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.planet.Planet;
import astrolab.astronom.planet.SolarSystem;
import astrolab.db.Event;
import astrolab.web.SVGDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class TrackPartStaticPlanets extends SVGDisplay {

  String[] PLANETS = new String[] { SolarSystem.JUPITER, SolarSystem.MARS, SolarSystem.MERCURY, SolarSystem.MOON, SolarSystem.NEPTUNE, SolarSystem.PLUTO, SolarSystem.SATURN, SolarSystem.SUN, SolarSystem.URANUS, SolarSystem.VENUS };

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
		fillContent(request, buffer, true);
	}

	public void fillContent(Request request, LocalizedStringBuffer buffer, boolean ownImage) {
		double position;
		SolarSystem solar = new SolarSystem();
    Planet earth = solar.getPlanet(SolarSystem.EARTH);
    solar.calculate(Event.getSelectedEvent());

		if (ownImage) {
			buffer.append("<svg:svg version='1.1' baseProfile='full' width='500px' height='500px'>");
		}
		for (int i = 0; i < PLANETS.length; i++) {
			Planet point = solar.getPlanet(PLANETS[i]);
      position = point.positionAround(earth);
			fillPlanet(point, position, buffer);
		}

		if (ownImage) {
			buffer.append("</svg:svg>");
		}
	}

	public void transform(Request request) {
		request.setDisplay(this);
	}

	private void fillPlanet(ActivePoint point, double position, LocalizedStringBuffer buffer) {
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
