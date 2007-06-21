package astrolab.web.component.chart;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.SpacetimeEvent;
import astrolab.astronom.util.Zodiac;
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
		SpacetimeEvent time = Event.getSelectedEvent();

		buffer.append("<table cellspacing='0'>");
    for (String planet: SolarSystem.PLANETS) {
      append(buffer, ActivePoint.getActivePoint(planet, time));
    }
		buffer.append("</table>");
	}

	private void append(LocalizedStringBuffer buffer, ActivePoint planet) {
    double position = planet.getPosition();

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
