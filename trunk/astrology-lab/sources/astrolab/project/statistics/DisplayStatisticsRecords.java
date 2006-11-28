package astrolab.project.statistics;

import astrolab.db.Action;
import astrolab.web.Display;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.component.time.ComponentSelectTime;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayStatisticsRecords extends HTMLFormDisplay {

	public final static int ID = Display.getId(DisplayStatisticsRecords.class);

  public DisplayStatisticsRecords() {
    super(Action.getAction(-1, ID, -1));
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    // Search bar
    buffer.localize("From");
    buffer.append(":");
    ComponentSelectTime.fill(buffer, "_select_from_time");
    buffer.localize("To");
    buffer.append(":");
    ComponentSelectTime.fill(buffer, "_select_to_time");
    buffer.append("<input type='submit' value='");
    buffer.localize("Search");
    buffer.append("' />");

//    buffer.append("<hr>");
//
//    ComponentTableStatistics table = new ComponentTableStatistics();
//    table.fillBodyContent(request, buffer);

    buffer.append("<hr />");

    ComponentChartStatistics chart = new ComponentChartStatistics();
    chart.fillBodyContent(request, buffer);
	}

}
