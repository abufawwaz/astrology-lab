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

  public TableHouses() {
    super("Houses");
    super.addAction("event", "user.session.event.1");
  }

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
		HouseSystem houses = new PlacidusSystem(Event.getSelectedEvent());

		buffer.append("<table cellspacing='0'>");
    for (int i = 1; i < 13; i++) {
      double position = houses.getHouse(i);
  		buffer.append("<tr title='");
      buffer.append(Zodiac.toString(position, "DD ZZZ MM SS"));
      buffer.append("'>");
      buffer.append("<td>");
  		buffer.append(HOUSES[i - 1]);
      buffer.append("</td><td width='3'></td><td align='right'>");
      buffer.append(Zodiac.toString(position, "DD"));
      buffer.append("</td><td>");
      buffer.append(Zodiac.toString(position, "Y"));
      buffer.append("</td><td>");
      buffer.append(Zodiac.toString(position, "MM"));
      buffer.append("</td></tr>");
    }
		buffer.append("</table>");
	}

}
