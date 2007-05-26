package astrolab.project;

import astrolab.db.Personalize;
import astrolab.db.Project;
import astrolab.db.ProjectIterator;
import astrolab.db.Text;
import astrolab.web.HTMLDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayProjectList extends HTMLDisplay {

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    int selected_project = Personalize.getFavourite(-1, Text.getId("user.session.project"), -1);
    int row = 0;
    int previousLaboratory = -1;

    buffer.append("<div class='class_title'>");
    buffer.localize("Projects");
		buffer.append("</div>");

    ProjectIterator projects = ProjectIterator.iterate();
		while (projects.hasNext()) {
			Project project = projects.next();
      if (project == null) {
        break;
      }

      if (previousLaboratory != project.getLaboratory()) {
        previousLaboratory = project.getLaboratory();
        buffer.append("<b>");
        buffer.localize(previousLaboratory);
        buffer.append("</b>");
        buffer.append("<br />");
      }

      // TODO: put this into a separate class
      buffer.append("<a href='javascript:top.fireEvent(window, \"project\",\"" + project.getId() + "\")'>");
      if (selected_project == project.getId()) {
        buffer.append("<b><font color='green'>");
      }
			buffer.localize(project.getId());
      if (selected_project == project.getId()) {
        buffer.append("</font></b>");
      }
      buffer.append("</a>");
      buffer.append("<br />");
      row++;
		}
	}

}
