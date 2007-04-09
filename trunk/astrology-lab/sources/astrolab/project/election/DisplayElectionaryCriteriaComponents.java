package astrolab.project.election;

import astrolab.astronom.planet.SolarSystem;
import astrolab.db.Text;
import astrolab.web.HTMLDisplay;
import astrolab.web.component.ComponentController;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayElectionaryCriteriaComponents extends HTMLDisplay {

  // get from database
  String[] PLANETS = new String[] {
      SolarSystem.MOON, SolarSystem.SUN,
      SolarSystem.MERCURY, SolarSystem.VENUS, SolarSystem.MARS,
      SolarSystem.JUPITER, SolarSystem.SATURN,
      SolarSystem.URANUS, SolarSystem.NEPTUNE, SolarSystem.PLUTO
  };

  public DisplayElectionaryCriteriaComponents() {
    super("Components");
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    buffer.localize("Position");
    buffer.append(" ");
    buffer.localize("Course");
    buffer.append(" ");
    buffer.localize("Aspect");
    buffer.append(" ");
    buffer.append("<hr />");
    buffer.append("<nobr>");
    for (int i = 0; i < PLANETS.length; i++) {
      int id = Text.getId(PLANETS[i]);
      String controlId = "controller_planet_" + id; 
      ComponentController.fill(buffer, controlId, "Planet", String.valueOf(id));
      buffer.append("<div id='");
      buffer.append(controlId);
      buffer.append("'>");
      buffer.localize(id);
      buffer.append("</div>");
    }
    buffer.append("</nobr>");
    buffer.append("<hr />");
  }

}