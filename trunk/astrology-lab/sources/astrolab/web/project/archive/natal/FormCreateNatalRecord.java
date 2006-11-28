package astrolab.web.project.archive.natal;

import astrolab.db.Action;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.Modify;
import astrolab.web.component.ComponentSelectAccuracy;
import astrolab.web.component.ComponentSelectChoice;
import astrolab.web.component.ComponentSelectSource;
import astrolab.web.component.location.ComponentSelectLocation;
import astrolab.web.component.time.ComponentSelectTime;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class FormCreateNatalRecord extends HTMLFormDisplay {

  public FormCreateNatalRecord() {
    super(Action.getAction(-1, -1, Modify.getId(ModifyCreateNatalRecord.class)));
  }

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("<table border='0'>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.append("<select id='_event_type' name='_event_type'>");
    buffer.append("<option value='" + NatalRecord.TYPE_MALE + "' selected='true'>");
    buffer.localize(NatalRecord.TYPE_MALE);
    buffer.append("</option>");
    buffer.append("<option value='" + NatalRecord.TYPE_FEMALE + "'>");
    buffer.localize(NatalRecord.TYPE_FEMALE);
    buffer.append("</option>");
    buffer.append("<option value='" + NatalRecord.TYPE_EVENT + "'>");
    buffer.localize(NatalRecord.TYPE_EVENT);
    buffer.append("</option>");
    buffer.append("</select>");
    buffer.append("</td>");
    buffer.append("<td><input id=\"" + Request.TEXT_NAME + "\" type='text' name='" + Request.TEXT_NAME + "' value='");
    buffer.localize("... not set ...");
    buffer.append("' /></td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Location");
    buffer.append(":</td>");
    buffer.append("<td>");
    ComponentSelectLocation.fill(buffer);
    buffer.append("</td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Time of occurance");
    buffer.append(":</td>");
    buffer.append("<td>");
    ComponentSelectTime.fill(buffer, Request.TEXT_DATE);
    buffer.append("</td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Accuracy");
    buffer.append(":</td>");
    buffer.append("<td>");
    ComponentSelectAccuracy.fill(buffer);
    buffer.append("</td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Source");
    buffer.append(":</td>");
    buffer.append("<td>");
    ComponentSelectSource.fill(buffer);
    buffer.append("</td>");
    buffer.append("</tr>");

    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Record is visible to");
    buffer.append(":</td>");
    buffer.append("<td>");
    ComponentSelectChoice.fill(buffer, new String[] { "everyone", "me only" }, new String[] { "0", "-1" }, "everyone", "_accessible_by");
    buffer.append("</td>");
    buffer.append("</tr>");

    buffer.append("</table>");
    buffer.append("<input type='submit' value='");
    buffer.localize("Save");
    buffer.append("' />");
	}

}