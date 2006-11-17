package astrolab.web.component.chart;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Vector;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.fiction.PointGamma;
import astrolab.astronom.planet.Planet;
import astrolab.astronom.planet.SolarSystem;
import astrolab.db.Event;
import astrolab.web.SVGDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

// TODO: turn into ChartPartActivePoints
public class ChartPartAspects extends SVGDisplay {

  String[] PLANETS = new String[] { SolarSystem.JUPITER, SolarSystem.MARS, SolarSystem.MERCURY, SolarSystem.MOON, SolarSystem.NEPTUNE, SolarSystem.PLUTO, SolarSystem.SATURN, SolarSystem.SUN, SolarSystem.URANUS, SolarSystem.VENUS };

	private double radius, x, y;

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
		fillContent(request, buffer, 0.0, true);
	}

	public void fillContent(Request request, LocalizedStringBuffer buffer, double offset, boolean ownImage) {
    Vector positions = new Vector();

    radius = request.getConstraints().getRadius();
		x = request.getConstraints().getWidth() / 2;
		y = request.getConstraints().getHeight() / 2;

		SolarSystem solar = new SolarSystem();
    Planet earth = solar.getPlanet(SolarSystem.EARTH);
    solar.calculate(Event.getSelectedEvent());

		for (int i = 0; i < PLANETS.length; i++) {
      positions.add(new Double(solar.getPlanet(PLANETS[i]).positionAround(earth)));
		}
    Object[] sorted = positions.toArray();
    Arrays.sort(sorted);

    positions.clear();

    PointsGroup group = new PointsGroup();
    for (int i = 0; i < sorted.length; i++) {
      if (!group.add((Double) sorted[i])) {
        positions.add(group);
        group = new PointsGroup();
        group.add((Double) sorted[i]);
      }
    }
    positions.add(group);

    for (int i = 0; i < positions.size(); i++) {
      for (int j = i + 1; j < positions.size(); j++) {
        ((PointsGroup) positions.get(i)).fillAspect((PointsGroup) positions.get(j), buffer, x, y, radius, offset);
      }
    }

    for (int i = 0; i < positions.size(); i++) {
      ((PointsGroup) positions.get(i)).fillGroup(buffer, x, y, radius, offset);
    }
  }

}

class PointsGroup {

  private final static double OFFSET_DELTA = Math.PI / 180;

  private double min = 0;
  private double max = 0;

  private Vector points = new Vector();

  public boolean add(Double point) {
    double newpoint = point.doubleValue();
    if (points.size() == 0) {
      points.add(point);
      min = max = newpoint;
      return true;
    } else if (Math.abs(min - newpoint) < 10 || Math.abs(max - newpoint) < 10) {
      points.add(point);
      if (newpoint < min) {
        min = newpoint;
      } else if (newpoint > max) {
        max = newpoint;
      }
      return true;
    } else {
      return false;
    }
  }

  public void fillGroup(LocalizedStringBuffer buffer, double x, double y, double radius, double raw_offset) {
    double x1 = (int) (x - (radius / 2) * Math.cos((raw_offset - min + 1) * OFFSET_DELTA));
    double y1 = (int) (y - (radius / 2) * Math.sin((raw_offset - min + 1) * OFFSET_DELTA));
    double x2 = (int) (x - (radius / 2) * Math.cos((raw_offset - max - 1) * OFFSET_DELTA));
    double y2 = (int) (y - (radius / 2) * Math.sin((raw_offset - max - 1) * OFFSET_DELTA));
    buffer.append("\r\n\t<line x1='" + x1 + "' y1='" + y1 + "' x2='" + x2 + "' y2='" + y2 + "' style='stroke:black;stroke-width:3' />");
  }

  public void fillAspect(PointsGroup group, LocalizedStringBuffer buffer, double x, double y, double radius, double raw_offset) {
    String color = isValidAspect(group);
    if (color == null) {
      return;
    }
    double x1 = (int) (x - (radius / 2) * Math.cos((raw_offset - ((max + min) / 2)) * OFFSET_DELTA));
    double y1 = (int) (y - (radius / 2) * Math.sin((raw_offset - ((max + min) / 2)) * OFFSET_DELTA));
    double x2 = (int) (x - (radius / 2) * Math.cos((raw_offset - ((group.max + group.min) / 2)) * OFFSET_DELTA));
    double y2 = (int) (y - (radius / 2) * Math.sin((raw_offset - ((group.max + group.min) / 2)) * OFFSET_DELTA));
    buffer.append("\r\n\t<line x1='" + x1 + "' y1='" + y1 + "' x2='" + x2 + "' y2='" + y2 + "' style='stroke:" + color + ";stroke-width:3' />");
  }

  public String isValidAspect(PointsGroup group) {
    if (isValidAspect(group, 90) || isValidAspect(group, 180) || isValidAspect(group, 270)) {
      return "red";
    } else if (isValidAspect(group, 60) || isValidAspect(group, 120) || 
        isValidAspect(group, 300) || isValidAspect(group, 240)) {
      return "green";
    } else {
      return null;
    }
  }

  public boolean isValidAspect(PointsGroup group, int degree) {
    if ((min + degree > group.max + 7) || (max + degree < group.min - 7)) {
      return false;
    } else {
      return true;
    }
  }

}
