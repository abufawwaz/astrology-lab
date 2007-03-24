package astrolab.project.geography;

import astrolab.web.AJAXFormDisplay;
import astrolab.web.Modify;
import astrolab.web.component.ComponentSelectNumber;
import astrolab.web.server.Request;
import astrolab.web.server.RequestParameters;
import astrolab.web.server.content.LocalizedStringBuffer;

public class FormAddLocation extends AJAXFormDisplay {

  private final static String KEY_LOCATION = "user.session.location.1";

  public FormAddLocation() {
    super("Add Location", Modify.getId(ModifyLocation.class));
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

    super.addSubmit(buffer, "Save", "\r\ntop.fireEvent(\"location.edit\", function() { return document.forms[0].elements[\"_tree-33\"].value })\r\n");

    buffer.append("<input type='button' value='");
    buffer.localize("New");
    buffer.append("' onclick='");
    ComponentSelectNumber.fillReset(buffer, "_location_id");
    buffer.append("document.forms[0]." + RequestParameters.TEXT_NAME + ".value=\"\";");
    ComponentSelectLattitude.fillReset(buffer);
    ComponentSelectLongitude.fillReset(buffer);
    buffer.append("'/>");
  }

}