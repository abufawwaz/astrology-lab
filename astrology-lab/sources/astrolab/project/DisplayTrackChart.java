package astrolab.project;

import astrolab.astronom.planet.Planet;
import astrolab.astronom.planet.SolarSystem;
import astrolab.project.statistics.InMemoryEvent;
import astrolab.web.SVGDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayTrackChart extends SVGDisplay {

  private final static String[] PLANETS = new String[] { SolarSystem.JUPITER, SolarSystem.MARS, SolarSystem.MERCURY, SolarSystem.MOON, SolarSystem.NEPTUNE, SolarSystem.PLUTO, SolarSystem.SATURN, SolarSystem.SUN, SolarSystem.URANUS, SolarSystem.VENUS };

  private ProjectData data = null;

  protected int strokeWidth = 1;

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("<g style='fill:none;");
    buffer.append(decorateLine());
    buffer.append("'>");

    double position;
    SolarSystem solar = new SolarSystem();
    Planet earth = solar.getPlanet(SolarSystem.EARTH);

    int timespan = (int) (data.getToTime().getTimeInMillis() - data.getFromTime().getTimeInMillis());
    for (int i = 0; i < 6; i++) {
      buffer.append("<rect style='fill:blue;fill-opacity:0.1;stroke:none;' x='0' y='");
      buffer.append(i * 60);
      buffer.append("' width='");
      buffer.append(timespan);
      buffer.append("' height='30'");
      buffer.append(" />");
    }

    for (int p = 0; p < PLANETS.length; p++) {
      if (data.begin()) {
        try {
          solar.calculate(new InMemoryEvent(data.getTime())); // TODO: calculate based on time!
          position = solar.getPlanet(PLANETS[p]).positionAround(earth);
          buffer.append("<polyline points='0,");
          buffer.append(position);
          buffer.append(" ");
solar.dumpZodiac(new InMemoryEvent(data.getTime()));
  
          while (data.move()) {
            int x = (int) (data.getTime().getTimeInMillis() - data.getFromTime().getTimeInMillis());
            solar.calculate(new InMemoryEvent(data.getTime())); // TODO: calculate based on time!
            position = solar.getPlanet(PLANETS[p]).positionAround(earth);
            buffer.append(x);
            buffer.append(",");
            buffer.append(position);
            buffer.append(" ");
          }
          buffer.append("' />");
        } catch (NumberFormatException nfe) {
          // TODO: use the type of the key to determine this !
        }
      }
    }

    buffer.append("</g>");
  }

  public void fillViewBox(Request request, LocalizedStringBuffer buffer) {
    data = Projects.getProject().getData();

    int timespan = (int) (data.getToTime().getTimeInMillis() - data.getFromTime().getTimeInMillis());

    buffer.append("preserveAspectRatio='none' viewBox='0 0 " + timespan + " 360'");
  }

  protected String decorateLine() {
    return "stroke:green;stroke-width:" + strokeWidth;
  }

}
