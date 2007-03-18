package astrolab.web.component.chart;

import astrolab.db.Text;
import astrolab.web.SVGDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ChartPartZodiac extends SVGDisplay {

	private final static String[] COLOR = { "red", "green", "yellow", "blue" };
	private final static double OFFSET_SIGN = Math.PI / 6;
	private final static double OFFSET_HALFSIGN = Math.PI / 12;
  private final static double OFFSET_DELTA = Math.PI / 180;
  private final static int ICON_INDEX = Text.getId("Aries");

	private double radius, x, y;

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
		fillContent(request, buffer, 0.0);
	}

  public void fillContent(Request request, LocalizedStringBuffer buffer, double offset) {
		radius = request.getConstraints().getRadius() - 50;
		x = request.getConstraints().getWidth() / 2;
		y = request.getConstraints().getHeight() / 2;

		for (int i = 0; i < 12; i++) {
			fillSign((i * 30 - offset) * OFFSET_DELTA, i, buffer);
		}
	}

	private void fillSign(double offset, int index, LocalizedStringBuffer buffer) {
		buffer.append("\r\n\t<path d=\"M");

		int x1 = (int) (x - radius * Math.cos(offset));
		int y1 = (int) (y + radius * Math.sin(offset));
		int x2 = (int) (x - radius * Math.cos(offset + OFFSET_SIGN));
		int y2 = (int) (y + radius * Math.sin(offset + OFFSET_SIGN));

		buffer.append("" + x1 + " " + y1 + " ");
		buffer.append("A" + radius + " " + radius + " 0 "); // rx ry x-axis-rotation
		buffer.append("0 0 ");                              // large-arc-flag sweep-flag
		buffer.append("" + x2 + " " + y2);                  // x y
		buffer.append("\" style=\"fill:none;stroke:" + COLOR[index % 4] + ";stroke-width:50;stroke-opacity:0.3\" />");

		int iconx = (int) (x - radius * Math.cos(offset + OFFSET_HALFSIGN));
		int icony = (int) (y + radius * Math.sin(offset + OFFSET_HALFSIGN));

		buffer.append("<g transform='translate(" + iconx + ", " + icony + ") scale(0.1)'>");
    buffer.append(Text.getCenteredSVGText(ICON_INDEX + index));
    buffer.append("</g>");
	}

}
