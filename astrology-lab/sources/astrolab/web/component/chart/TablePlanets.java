package astrolab.web.component.chart;

import astrolab.astronom.util.Zodiac;
import astrolab.astronom.planet.Planet;
import astrolab.astronom.planet.SolarSystem;
import astrolab.db.Event;
import astrolab.db.Text;
import astrolab.web.HTMLDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class TablePlanets extends HTMLDisplay {

  public TablePlanets() {
    super("Planets");
    super.addAction("event", "user.session.event.1");
  }

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
		SolarSystem solar = new SolarSystem();
    Planet center = solar.getPlanet(SolarSystem.EARTH);
    solar.calculate(Event.getSelectedEvent());

		buffer.append("<table cellspacing='0'>");
    append(buffer, solar.getPlanet(SolarSystem.SUN), center);
    append(buffer, solar.getPlanet(SolarSystem.MOON), center);
    append(buffer, solar.getPlanet(SolarSystem.MERCURY), center);
    append(buffer, solar.getPlanet(SolarSystem.VENUS), center);
    append(buffer, solar.getPlanet(SolarSystem.MARS), center);
    append(buffer, solar.getPlanet(SolarSystem.JUPITER), center);
    append(buffer, solar.getPlanet(SolarSystem.SATURN), center);
    append(buffer, solar.getPlanet(SolarSystem.URANUS), center);
    append(buffer, solar.getPlanet(SolarSystem.NEPTUNE), center);
    append(buffer, solar.getPlanet(SolarSystem.PLUTO), center);
		buffer.append("</table>");
	}

	private void append(LocalizedStringBuffer buffer, Planet planet, Planet center) {
    double position = planet.positionAround(center);
    buffer.newline();
    buffer.append("<tr title='");
    buffer.append(Text.getText(planet.getName()));
    buffer.append(" ");
    buffer.append(Zodiac.toString(position, "DD ZZZ MM SS"));
    buffer.append("'>");
    buffer.append("<td>");
		buffer.localize(planet.getName());
    buffer.append("</td><td width='3'></td><td align='right'>");
    buffer.append(Zodiac.toString(position, "DD"));
    buffer.append("</td><td>");
    buffer.append(Zodiac.toString(position, "Y"));
    buffer.append("</td><td>");
    buffer.append(Zodiac.toString(position, "MM"));
    buffer.append("</td></tr>");
	}

}
