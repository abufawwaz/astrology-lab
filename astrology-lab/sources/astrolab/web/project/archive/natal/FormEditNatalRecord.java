package astrolab.web.project.archive.natal;

import astrolab.db.Action;
import astrolab.db.Event;
import astrolab.db.Text;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.Modify;
import astrolab.web.component.ComponentSelectAccuracy;
import astrolab.web.component.ComponentSelectChoice;
import astrolab.web.component.ComponentSelectNumber;
import astrolab.web.component.ComponentSelectSource;
import astrolab.web.component.location.ComponentSelectLocation;
import astrolab.web.component.time.ComponentSelectTime;
import astrolab.web.server.Request;
import astrolab.web.server.RequestParameters;
import astrolab.web.server.content.LocalizedStringBuffer;

public class FormEditNatalRecord extends HTMLFormDisplay {

  public FormEditNatalRecord() {
    super(Action.getAction(-1, -1, Modify.getId(ModifyCreateNatalRecord.class)));
  }

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    Event record = Event.getSelectedEvent();

    ComponentSelectNumber.fillHidden(buffer, "_event_id", record.getEventId());
    buffer.append("<table border='0'>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.append("<select id='_event_type' name='_event_type'>");
    if (NatalRecord.TYPE_MALE.equals(record.getType())) {
      buffer.append("<option value='" + NatalRecord.TYPE_MALE + "' selected='true'>");
    } else {
      buffer.append("<option value='" + NatalRecord.TYPE_MALE + "'>");
    }
    buffer.localize(NatalRecord.TYPE_MALE);
    buffer.append("</option>");
    if (NatalRecord.TYPE_FEMALE.equals(record.getType())) {
      buffer.append("<option value='" + NatalRecord.TYPE_FEMALE + "' selected='true'>");
    } else {
      buffer.append("<option value='" + NatalRecord.TYPE_FEMALE + "'>");
    }
    buffer.localize(NatalRecord.TYPE_FEMALE);
    buffer.append("</option>");
    if (NatalRecord.TYPE_EVENT.equals(record.getType())) {
      buffer.append("<option value='" + NatalRecord.TYPE_EVENT + "' selected='true'>");
    } else {
      buffer.append("<option value='" + NatalRecord.TYPE_EVENT + "'>");
    }
    buffer.localize(NatalRecord.TYPE_EVENT);
    buffer.append("</option>");
    buffer.append("</select>");
    buffer.append("</td>");
    buffer.append("<td><input id='" + RequestParameters.TEXT_NAME + "' type='text' name='" + RequestParameters.TEXT_NAME + "' value='");
    buffer.localize(record.getSubject());
    buffer.append("' /></td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Location");
    buffer.append(":</td>");
    buffer.append("<td>");
    ComponentSelectLocation.fill(buffer, record.getLocation());
    buffer.append("</td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Time of occurance");
    buffer.append(":</td>");
    buffer.append("<td>");
    ComponentSelectTime.fill(buffer, record.getTime(), ComponentSelectTime.PARAMETER_KEY);
    buffer.append("</td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Accuracy");
    buffer.append(":</td>");
    buffer.append("<td>");
    ComponentSelectAccuracy.fill(buffer, record.getAccuracy());
    buffer.append("</td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Source");
    buffer.append(":</td>");
    buffer.append("<td>");
    ComponentSelectSource.fill(buffer, record.getSource());
    buffer.append("</td>");
    buffer.append("</tr>");

    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Record is visible to");
    buffer.append(":</td>");
    buffer.append("<td>");
    int accessible_by = Text.getAccessibleBy(record.getId());
    String accessibleSelection = (accessible_by == Text.ACCESSIBLE_BY_ALL) ? "everyone" : "me only";
    ComponentSelectChoice.fill(buffer, new String[] { "everyone", "me only" }, new String[] { String.valueOf(Text.ACCESSIBLE_BY_ALL), String.valueOf(Text.ACCESSIBLE_BY_OWNER) }, accessibleSelection, "_accessible_by");
    buffer.append("</td>");
    buffer.append("</tr>");

    buffer.append("</table>");
    buffer.append("<input type='submit' value='");
    buffer.localize("Save");
    buffer.append("' />");
	}

}