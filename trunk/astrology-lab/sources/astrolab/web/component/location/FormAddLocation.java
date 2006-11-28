package astrolab.web.component.location;

import astrolab.db.Action;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.Modify;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class FormAddLocation extends HTMLFormDisplay {

  public FormAddLocation() {
    super(Action.getAction(-1, -1, Modify.getId(ModifyLocation.class)));
  }

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("<table border='0'>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Location");
    buffer.append(":</td>");
    buffer.append("<td><input id=\"" + Request.TEXT_NAME + "\" type='text' name='" + Request.TEXT_NAME + "' value='");
    buffer.localize("... not set ...");
    buffer.append("' /></td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Region");
    buffer.append(":</td>");
    buffer.append("<td>");
    ComponentSelectLocation.fill(buffer);
    buffer.append("</td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Longitude");
    buffer.append(":</td>");
    buffer.append("<td>");
    ComponentSelectLongitude.fill(buffer);
    buffer.append("</td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Lattitude");
    buffer.append(":</td>");
    buffer.append("<td>");
    ComponentSelectLattitude.fill(buffer);
    buffer.append("</td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Time Zone");
    buffer.append(":</td>");
    buffer.append("<td>");
    ComponentSelectTimeZone.fill(buffer);
    buffer.append("</td>");
    buffer.append("</tr>");
    buffer.append("</table>");
    buffer.append("<input type='submit' value='");
    buffer.localize("Save");
    buffer.append("' />");
	}

}