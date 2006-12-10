package astrolab.web.project.labs;

import astrolab.db.Personalize;
import astrolab.db.Project;
import astrolab.db.ProjectIterator;
import astrolab.db.Text;
import astrolab.web.HTMLDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ProjectList extends HTMLDisplay {

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    int selected_project = Personalize.getFavourite(-1, Text.getId("user.session.project"), -1);
    int row = 0;

    buffer.localize("The projects are:");
		buffer.append("<hr />");
    int lab = Personalize.getFavourite(-1, Text.getId("user.session.laboratory"), -1);
    ProjectIterator projects = ProjectIterator.iterate(lab);
		while (projects.hasNext()) {
			Project project = projects.next();
      if (project == null) {
        break;
      }

      buffer.append("<a href='root.html?_s=");
      buffer.append(project.getId());
      buffer.append("'>");
      if (selected_project == project.getId()) {
        buffer.append("<b><font color='green'>");
      }
			buffer.append(Text.getText(project.getId()));
      if (selected_project == project.getId()) {
        buffer.append("</font></b>");
      }
      buffer.append("</a>");
      buffer.append("<br />");
      row++;
		}
	}

}
