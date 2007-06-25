package astrolab.web.component.chart;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.SpacetimeEvent;
import astrolab.astronom.houses.HouseSystem;
import astrolab.db.Event;
import astrolab.web.SVGDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ChartPartHouses extends SVGDisplay {

	private final static String[] COLOR = { "red", "green", "yellow", "blue" };
  private final static double OFFSET_DELTA = Math.PI / 180;

  private final static String GRADIENT_RED = "<defs><linearGradient id=\"house_red\" gradientTransform=\"rotate(90)\"><stop offset=\"5%\" stop-color=\"red\" stop-opacity=\"0.3\" /><stop offset=\"100%\" stop-color=\"red\" stop-opacity=\"0\" /></linearGradient></defs>";
  private final static String GRADIENT_GREEN = "<defs><linearGradient id=\"house_green\" gradientTransform=\"rotate(90)\"><stop offset=\"5%\" stop-color=\"green\" stop-opacity=\"0.3\" /><stop offset=\"100%\" stop-color=\"green\" stop-opacity=\"0\" /></linearGradient></defs>";
  private final static String GRADIENT_YELLOW = "<defs><linearGradient id=\"house_yellow\" gradientTransform=\"rotate(90)\"><stop offset=\"5%\" stop-color=\"yellow\" stop-opacity=\"0.3\" /><stop offset=\"100%\" stop-color=\"yellow\" stop-opacity=\"0\" /></linearGradient></defs>";
  private final static String GRADIENT_BLUE = "<defs><linearGradient id=\"house_blue\" gradientTransform=\"rotate(90)\"><stop offset=\"5%\" stop-color=\"blue\" stop-opacity=\"0.3\" /><stop offset=\"100%\" stop-color=\"blue\" stop-opacity=\"0\" /></linearGradient></defs>";

	private double radius, x, y;

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
		fillContent(request, buffer, Event.getSelectedEvent(), 0.0, true);
	}

	public void fillContent(Request request, LocalizedStringBuffer buffer, SpacetimeEvent event, double offset, boolean ownImage) {
		radius = request.getConstraints().getRadius();
		x = request.getConstraints().getWidth() / 2;
		y = request.getConstraints().getHeight() / 2;

    double[] hpos = new double[14];

    for (int i = 1; i < 13; i++) {
      hpos[i] = ActivePoint.getActivePoint(HouseSystem.HOUSES[i - 1], event).getPosition();
    }
    hpos[13] = hpos[1];

    buffer.append(GRADIENT_RED);
    buffer.append(GRADIENT_GREEN);
    buffer.append(GRADIENT_YELLOW);
    buffer.append(GRADIENT_BLUE);
    for (int i = 1; i < 13; i++) {
      fillHouse((hpos[i] - offset) * OFFSET_DELTA, (hpos[i + 1] - hpos[i] - getPreHouseSize(i + 1) - 1) * OFFSET_DELTA, getPreHouseSize(i), i, buffer);
    }
	}

  private final static int getPreHouseSize(int house) {
    return (house == 1 || house == 10 || house == 13) ? 5 : 3;
  }

  private void fillHouse(double offset, double houseSize, double preHouseSize, int index, LocalizedStringBuffer buffer) {
    buffer.append("\r\n\t<g transform='translate(" + x + ", " + y + ")'>");
    buffer.append("\r\n\t<g transform='rotate(");
    buffer.append(String.valueOf(- offset / OFFSET_DELTA));
    buffer.append(")'>");

    buffer.append("\r\n\t<path d=\"M");
    buffer.append(String.valueOf((int) (-radius)));
    buffer.append(" 0");

    buffer.append(" L");
    buffer.append(String.valueOf((int) (-radius / 2 + 10)));
    buffer.append(" 0");

    buffer.append("\" style=\"stroke:" + COLOR[(index - 1) % 4] + ";stroke-width:3\" />");

    buffer.append("\r\n\t<path d=\"M");
    buffer.append(String.valueOf((int) (-(radius - 20) * Math.cos(- preHouseSize * OFFSET_DELTA))));
    buffer.append(" ");
    buffer.append(String.valueOf((int) ((radius - 20) * Math.sin(- preHouseSize * OFFSET_DELTA))));

    buffer.append(" L");
    buffer.append(String.valueOf((int) (-(radius / 2 + 10) * Math.cos(- preHouseSize * OFFSET_DELTA))));
    buffer.append(" ");
    buffer.append(String.valueOf((int) ((radius / 2 + 10) * Math.sin(- preHouseSize * OFFSET_DELTA))));

    buffer.append(" Q");
    buffer.append(String.valueOf((int) (-(radius / 2) * Math.cos(- preHouseSize * OFFSET_DELTA))));
    buffer.append(" ");
    buffer.append(String.valueOf((int) ((radius / 2) * Math.sin(- preHouseSize * OFFSET_DELTA))));
    buffer.append(" ");
    buffer.append(String.valueOf((int) (-radius / 2)));
    buffer.append(" 0");

    buffer.append(" A");
    buffer.append(String.valueOf((int) (radius / 2)));
    buffer.append(" ");
    buffer.append(String.valueOf((int) (radius / 2)));
    buffer.append(" 0 0 0 ");
    buffer.append(String.valueOf((int) (-(radius / 2) * Math.cos(houseSize - preHouseSize * OFFSET_DELTA))));
    buffer.append(" ");
    buffer.append(String.valueOf((int) ((radius / 2) * Math.sin(houseSize - preHouseSize * OFFSET_DELTA))));

    buffer.append(" Q");
    buffer.append(String.valueOf((int) (-(radius / 2) * Math.cos(houseSize))));
    buffer.append(" ");
    buffer.append(String.valueOf((int) ((radius / 2) * Math.sin(houseSize))));
    buffer.append(" ");
    buffer.append(String.valueOf((int) (-(radius / 2 + 10) * Math.cos(houseSize))));
    buffer.append(" ");
    buffer.append(String.valueOf((int) ((radius / 2 + 10) * Math.sin(houseSize))));

    buffer.append(" L");
    buffer.append(String.valueOf((int) (-(radius - 20) * Math.cos(houseSize))));
    buffer.append(" ");
    buffer.append(String.valueOf((int) ((radius - 20) * Math.sin(houseSize))));

    buffer.append(" Q");
    buffer.append(String.valueOf((int) (-(radius - 10) * Math.cos(houseSize))));
    buffer.append(" ");
    buffer.append(String.valueOf((int) ((radius - 10) * Math.sin(houseSize))));
    buffer.append(" ");
    buffer.append(String.valueOf((int) (-(radius - 10) * Math.cos(houseSize - preHouseSize * OFFSET_DELTA))));
    buffer.append(" ");
    buffer.append(String.valueOf((int) ((radius - 10) * Math.sin(houseSize - preHouseSize * OFFSET_DELTA))));

    buffer.append(" A");
    buffer.append(String.valueOf((int) (radius + 10)));
    buffer.append(" ");
    buffer.append(String.valueOf((int) (radius + 10)));
    buffer.append(" 0 0 1 ");
    buffer.append(String.valueOf((int) (-radius + 10)));
    buffer.append(" 0");

    buffer.append(" Q");
    buffer.append(String.valueOf((int) (-(radius - 10) * Math.cos(- preHouseSize * OFFSET_DELTA))));
    buffer.append(" ");
    buffer.append(String.valueOf((int) ((radius - 10) * Math.sin(- preHouseSize * OFFSET_DELTA))));
    buffer.append(" ");
    buffer.append(String.valueOf((int) (-(radius - 20) * Math.cos(- preHouseSize * OFFSET_DELTA))));
    buffer.append(" ");
    buffer.append(String.valueOf((int) ((radius - 20) * Math.sin(- preHouseSize * OFFSET_DELTA))));

    buffer.append(" Z");

    buffer.append("\" style=\"fill:url(#house_" + COLOR[(index - 1) % 4] + ");stroke:none\" />");

    buffer.append("\r\n\t</g>");
    buffer.append("\r\n\t</g>");
  }

}
