package astrolab.project.relocation;

import astrolab.db.Text;
import astrolab.project.geography.ComponentSelectLocation;
import astrolab.web.AJAXFormDisplay;
import astrolab.web.Modify;
import astrolab.web.component.time.ComponentSelectTime;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class FormRelocationRecord extends AJAXFormDisplay {

  public FormRelocationRecord() {
    super("Add relocation", Modify.getId(ModifyRelocationRecord.class));
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
    buffer.append("<td>Location:</td>");
    buffer.append("<td>");
    ComponentSelectLocation.fill(buffer);
    buffer.append("</td>");
    buffer.append("</tr>");
    buffer.append("</table>");
    super.addSubmit(buffer, "Save", "top.fireEvent(window, \"relocation.new\", \"yes\")");
	}

}