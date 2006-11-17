package astrolab.web.component.chart;

import astrolab.astronom.util.Zodiac;
import astrolab.astronom.houses.HouseSystem;
import astrolab.astronom.houses.PlacidusSystem;
import astrolab.db.Event;
import astrolab.web.HTMLDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class TableHouses extends HTMLDisplay {

  private final static String[] HOUSES = new String[] {
    "ASC", "II", "III", "IC", "V", "VI", "DSC", "VIII", "IX", "MC", "XI", "XII"
  };

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
		HouseSystem houses = new PlacidusSystem(Event.getSelectedEvent());

		buffer.append("<table>");
    for (int i = 1; i < 13; i++) {
  		buffer.append("<tr><td>");
  		buffer.append(HOUSES[i - 1]);
  		buffer.append("</td><td>");
  		buffer.append(Zodiac.toString(houses.getHouse(i), "DD ZZZ MM SS"));
  		buffer.append("</td></tr>");
    }
		buffer.append("</table>");
	}

}
