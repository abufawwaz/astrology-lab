package astrolab.web.project.labs;

import astrolab.db.Action;
import astrolab.db.Personalize;
import astrolab.db.Project;
import astrolab.db.ProjectIterator;
import astrolab.db.Text;
import astrolab.web.HTMLDisplay;
import astrolab.web.Modify;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ProjectList extends HTMLDisplay {

  private final static int ACTION_SELECT_PROJECT = Action.getAction(-1, -1, Modify.getId(SelectProject.class));

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    int selected_project = Personalize.getFavourite(-1, Text.getId("user.session.project"), -1);
    int row = 0;
    int previousLaboratory = -1;

    buffer.localize("The projects are:");
		buffer.append("<hr />");

    buffer.append("<table border='0'>");
    ProjectIterator projects = ProjectIterator.iterate();
		while (projects.hasNext()) {
			Project project = projects.next();
      if (project == null) {
        break;
      }

      buffer.append("<tr><td>");
      if (previousLaboratory != project.getLaboratory()) {
        previousLaboratory = project.getLaboratory();
        buffer.localize(previousLaboratory);
      }
      buffer.append("</td>");
      buffer.append("<td>");
      buffer.append("<a href='root.html?_a=");
      buffer.append(ACTION_SELECT_PROJECT);
      buffer.append("&amp;_s=");
      buffer.append(project.getId());
      buffer.append("'>");
      if (selected_project == project.getId()) {
        buffer.append("<b><font color='green'>");
      }
			buffer.localize(project.getId());
      if (selected_project == project.getId()) {
        buffer.append("</font></b>");
      }
      buffer.append("</a>");
      buffer.append("</td></tr>");
      row++;
		}
    buffer.append("</table>");
	}

}
