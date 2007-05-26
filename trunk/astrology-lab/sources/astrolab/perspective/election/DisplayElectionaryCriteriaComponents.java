package astrolab.perspective.election;

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
    buffer.append("<table><tr>");
    for (int i = 0; i < PLANETS.length; i++) {
      int id = Text.getId(PLANETS[i]);
      String controlId = "controller_planet_" + id; 
      ComponentController.fill(buffer, controlId, "Planet", String.valueOf(id));
      buffer.append("<td id='");
      buffer.append(controlId);
      buffer.append("'>");
      buffer.localize(id);
      buffer.append("</td>");

      if (i % 6 == 5) {
        buffer.append("</tr><tr>");
      }
    }
    buffer.append("</tr></table>");

    buffer.append("<hr />");
    buffer.append("<table><tr>");
    int id = Text.getId("Aries");
    for (int i = 0; i < 12; i++, id++) {
      String controlId = "controller_sign_" + id; 
      ComponentController.fill(buffer, controlId, "Sign", String.valueOf(id));
      buffer.append("<td id='");
      buffer.append(controlId);
      buffer.append("'>");
      buffer.localize(id);
      buffer.append("</td>");

      if (i % 6 == 5) {
        buffer.append("</tr><tr>");
      }
    }
    buffer.append("</tr></table>");

    buffer.append("<hr />");
  }

}