package astrolab.project.sleep;

import astrolab.db.Action;
import astrolab.db.Text;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.Modify;
import astrolab.web.component.ComponentSelectAccuracy;
import astrolab.web.component.ComponentSelectSource;
import astrolab.web.component.time.ComponentSelectTime;
import astrolab.web.project.archive.relocation.RelocationRecord;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class FormSleepRecord extends HTMLFormDisplay {

  public FormSleepRecord() {
    super(Action.getAction(-1, -1, Modify.getId(ModifySleepRecord.class)));
  }

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    int user = request.getUser();
    int location = RelocationRecord.getLocationOfPerson(user, System.currentTimeMillis());
    String locationName = Text.getText(location);

    buffer.append("<table border='0'>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize(user);
    buffer.append("</td><td>");
    buffer.localize("sleeps");
    buffer.append("</td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("From");
    buffer.append(":</td>");
    buffer.append("<td>");
    ComponentSelectTime.fill(buffer, "_sleep_from");
    buffer.append("</td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("To");
    buffer.append(":</td>");
    buffer.append("<td>");
    ComponentSelectTime.fill(buffer, "_sleep_to");
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
    buffer.append("<td><div title='");
    buffer.localize("Report any different location in project Relocation!");
    buffer.append("'>" + locationName + "</div></td>");
    buffer.append("</tr>");
    buffer.append("</table>");
    buffer.append("<input type='submit' value='");
    buffer.localize("Save");
    buffer.append("' />");
	}

}