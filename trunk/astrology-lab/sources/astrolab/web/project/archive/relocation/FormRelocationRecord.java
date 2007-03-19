package astrolab.web.project.archive.relocation;

import astrolab.db.Action;
import astrolab.db.Text;
import astrolab.project.geography.ComponentSelectLocation;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.Modify;
import astrolab.web.component.ComponentSelectAccuracy;
import astrolab.web.component.ComponentSelectSource;
import astrolab.web.component.time.ComponentSelectTime;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class FormRelocationRecord extends HTMLFormDisplay {

  public FormRelocationRecord() {
    super(Action.getAction(-1, -1, Modify.getId(ModifyRelocationRecord.class)));
  }

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("<table border='0'>");
    buffer.append("<tr>");
    buffer.append("<td>Relocation of </td>");
    buffer.append("<td><div title='This is you.'>" + Text.getText(request.getUser()) + "</div></td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td>Time of relocation:</td>");
    buffer.append("<td>");
    ComponentSelectTime.fill(buffer, ComponentSelectTime.PARAMETER_KEY);
    buffer.append("</td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td>Accuracy:</td>");
    buffer.append("<td>");
    ComponentSelectAccuracy.fill(buffer);
    buffer.append("</td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td>Source:</td>");
    buffer.append("<td>");
    ComponentSelectSource.fill(buffer);
    buffer.append("</td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td>Location:</td>");
    buffer.append("<td>");
    ComponentSelectLocation.fill(buffer);
    buffer.append("</td>");
    buffer.append("</tr>");
    buffer.append("</table>");
    buffer.append("<input type='submit' value='Save' />");
	}

}