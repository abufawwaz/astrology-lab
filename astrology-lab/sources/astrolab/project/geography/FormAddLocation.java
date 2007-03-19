package astrolab.project.geography;

import astrolab.db.Action;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.Modify;
import astrolab.web.component.ComponentSelectNumber;
import astrolab.web.server.Request;
import astrolab.web.server.RequestParameters;
import astrolab.web.server.content.LocalizedStringBuffer;

public class FormAddLocation extends HTMLFormDisplay {

  private final static String KEY_LOCATION = "user.session.location.1";

  public FormAddLocation() {
    super(Action.getAction(-1, -1, Modify.getId(ModifyLocation.class)));

    super.addAction("location", KEY_LOCATION);
  }

  private final static Location getSelectedLocation(Request request) {
    try {
      String locationId = request.get(KEY_LOCATION);
      return Location.getLocation(Integer.parseInt(locationId));
    } catch (Exception e) {
      return Location.getLocation(0);
    }
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    Location location = getSelectedLocation(request);

    ComponentSelectNumber.fillHidden(buffer, "_location_id", location.getId());
    buffer.append("<table border='0'>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Location");
    buffer.append(":</td>");
    buffer.append("<td><input id=\"" + RequestParameters.TEXT_NAME + "\" type='text' name='" + RequestParameters.TEXT_NAME + "' value='");
    buffer.localize(location.getId());
    buffer.append("' /></td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Region");
    buffer.append(":</td>");
    buffer.append("<td>");
    ComponentSelectLocation.fill(buffer, location.getParent());
    buffer.append("</td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Longitude");
    buffer.append(":</td>");
    buffer.append("<td>");
    ComponentSelectLongitude.fill(buffer, location.getLongitude());
    buffer.append("</td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Lattitude");
    buffer.append(":</td>");
    buffer.append("<td>");
    ComponentSelectLattitude.fill(buffer, location.getLattitude());
    buffer.append("</td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Time Zone");
    buffer.append(":</td>");
    buffer.append("<td>");
    ComponentSelectTimeZone.fill(buffer, location.getTimeZone().getID());
    buffer.append("</td>");
    buffer.append("</tr>");
    buffer.append("</table>");

    buffer.append("<input type='submit' value='");
    buffer.localize("Save");
    buffer.append("' />");

    buffer.append("<input type='button' value='");
    buffer.localize("New");
    buffer.append("' onclick='document.forms[0]._location_id.value=0;document.forms[0]." + RequestParameters.TEXT_NAME + ".value=\"\"'/>");
  }

}