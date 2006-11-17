package astrolab.web.component.chart;

import java.util.Calendar;

import astrolab.web.SVGDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class Track extends SVGDisplay {

	//TODO: these should be changeable
  static Calendar trackStart;
  static Calendar trackEnd;
  static int height = 500;

	static {
		trackStart = Calendar.getInstance();
		trackStart.set(Calendar.YEAR, trackStart.get(Calendar.YEAR) - 1);
		trackEnd = Calendar.getInstance();
		trackEnd.set(Calendar.YEAR, trackEnd.get(Calendar.YEAR) + 1);
	}

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
		fillGrid(buffer);
		new TrackPartStaticPlanets().fillContent(request, buffer, false);
		new TrackPartActivePlanets().fillContent(request, buffer, false);
	}

	private void fillGrid(LocalizedStringBuffer buffer) {
	 final String[] COLOR = { "red", "green", "yellow", "blue" };
		for (int i = 0; i < 12; i++) {
			buffer.append("<rect x='" + (i * 30) + "' y='0' width='30' height='" + height + "' style='stroke:none; fill:" + COLOR[i % 4] + ";fill-opacity:0.3' />");
		}
		Calendar c = Calendar.getInstance();
		for (int i = 0; i < height; i += 50) {
			buffer.append("<line x1='0' y1='" + i + "' x2='360' y2='" + i + "' style='stroke:black;stroke-width:1;stroke-opacity:0.3' />");
			c.setTimeInMillis(trackStart.getTimeInMillis() + ((trackEnd.getTimeInMillis() - trackStart.getTimeInMillis()) * i / height));
			buffer.append("<text x='370' y='" + i + "'>");
			buffer.append(c.getTime().toString());
			buffer.append("</text>");
		}
	}

}
