package astrolab.perspective.election;

import astrolab.astronom.planet.SolarSystem;
import astrolab.criteria.CriterionPositionDirection;
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
    fillDirectionComponents(buffer);
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

  private void fillDirectionComponents(LocalizedStringBuffer buffer) {
    int id;
    String controlId;

    buffer.append("<table><tr>");

    id = Text.getId(CriterionPositionDirection.DIRECTION_DIRECT);
    controlId = "controller_dir_d"; 
    ComponentController.fill(buffer, controlId, "Direction", String.valueOf(id));
    buffer.append("<td id='");
    buffer.append(controlId);
    buffer.append("'>");
    buffer.append(Text.getDescriptiveId(id));
    buffer.append("</td>");

    id = Text.getId(CriterionPositionDirection.DIRECTION_STATIONARY);
    controlId = "controller_dir_s";
    ComponentController.fill(buffer, controlId, "Direction", String.valueOf(id));
    buffer.append("<td id='");
    buffer.append(controlId);
    buffer.append("'>");
    buffer.append(Text.getDescriptiveId(id));
    buffer.append("</td>");

    id = Text.getId(CriterionPositionDirection.DIRECTION_RETROGRADE);
    controlId = "controller_dir_r";
    ComponentController.fill(buffer, controlId, "Direction", String.valueOf(id));
    buffer.append("<td id='");
    buffer.append(controlId);
    buffer.append("'>");
    buffer.append(Text.getDescriptiveId(id));
    buffer.append("</td>");

    buffer.append("</tr></table>");
  }

}