package astrolab.web.project.archive.diary;

import astrolab.db.Action;
import astrolab.db.Text;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.Modify;
import astrolab.web.component.ComponentSelectAccuracy;
import astrolab.web.component.ComponentSelectSource;
import astrolab.web.component.time.ComponentSelectTime;
import astrolab.web.project.archive.relocation.RelocationRecord;
import astrolab.web.server.Request;
import astrolab.web.server.RequestParameters;
import astrolab.web.server.content.LocalizedStringBuffer;

public class FormDiaryRecord extends HTMLFormDisplay {

  public FormDiaryRecord() {
    super(Action.getAction(-1, -1, Modify.getId(ModifyDiaryRecord.class)));
  }

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    int location = RelocationRecord.getLocationOfPerson(request.getUser(), System.currentTimeMillis());
    String locationName = Text.getText(location);

    buffer.append("<table border='0'>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Description");
    buffer.append(":</td>");
    buffer.append("<td><textarea id=\"" + RequestParameters.TEXT_NAME + "\" name='" + RequestParameters.TEXT_NAME + "' title='");
    buffer.localize("Enter the description of the event here!");
    buffer.append("' cols='40' rows='6'></textarea></td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Subject");
    buffer.append(":</td>");
    buffer.append("<td><div title='");
    buffer.localize("This is you.");
    buffer.append("'>" + Text.getText(request.getUser()) + "</div></td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Time of occurance");
    buffer.append(":</td>");
    buffer.append("<td>");
    ComponentSelectTime.fill(buffer, ComponentSelectTime.PARAMETER_KEY);
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
    buffer.localize("Location");
    buffer.append(":</td>");
    buffer.append("<td><div title=\"");
    buffer.localize("Report any different location in project Relocation!");
    buffer.append("\">" + locationName + "</div></td>");
    buffer.append("</tr>");
    buffer.append("</table>");
    buffer.append("<input type='submit' value='");
    buffer.localize("Save");
    buffer.append("' />");
	}

}