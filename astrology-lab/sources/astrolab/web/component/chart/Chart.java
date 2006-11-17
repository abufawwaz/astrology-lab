package astrolab.web.component.chart;

import astrolab.astronom.houses.HouseSystem;
import astrolab.astronom.houses.PlacidusSystem;
import astrolab.db.Event;
import astrolab.web.SVGDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class Chart extends SVGDisplay {

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
		HouseSystem houses = new PlacidusSystem(Event.getSelectedEvent());
		double offset = houses.getHouse(1);

		buffer.append("<svg width=\"100%\" height=\"100%\" viewBox=\"0 0 500 500\" preserveAspectRatio=\"xMidYMid meet\">");
		new ChartPartZodiac().fillContent(request, buffer, offset, false);
		new ChartPartHouses().fillContent(request, buffer, offset, false);
    new ChartPartPlanets().fillContent(request, buffer, offset, false);
    new ChartPartAspects().fillContent(request, buffer, offset, false);
		buffer.append("</svg>");
	}

}
