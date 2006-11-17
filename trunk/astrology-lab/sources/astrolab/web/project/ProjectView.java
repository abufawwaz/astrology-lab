package astrolab.web.project;

import astrolab.db.Project;
import astrolab.web.HTMLDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ProjectView extends HTMLDisplay {

  private Project project;

  public ProjectView(Project project) {
    this.project = project;
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("<b><a href=''>");
    buffer.append(project.getName());
    buffer.append("</a></b> ");
    buffer.append(project.getDescription());
  }
}