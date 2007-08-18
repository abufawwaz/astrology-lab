package astrolab.project;

import java.util.HashMap;

import astrolab.db.Personalize;
import astrolab.db.Text;
import astrolab.web.server.Request;

public class Projects {

  private static HashMap<String, Project> projects = new HashMap<String, Project>();

  public static Project getProject() {
    int selectedProjectId = -1;

    String selection = Request.getCurrentRequest().get("user.session.project");
    if (selection != null) {
      selectedProjectId = Integer.parseInt(selection);
    }

    if (selectedProjectId < 0) {
      selectedProjectId = Personalize.getFavourite(-1, Text.getId("user.session.project"), -1);
    }

    if (selectedProjectId < 0) {
      selectedProjectId = Text.getId("Archive");
    }

    return getProject(selectedProjectId);
  }

  public static Project getProject(int selectedProjectId) {
    Project project = null;
    String projectName = Text.getDescriptiveId(selectedProjectId);

    if (projectName != null) {
      project = projects.get(projectName);
  
      if (project == null) {
        project = new Project(selectedProjectId, projectName);

        boolean timeKeyExists = false;
        for (ProjectDataKey key: project.getKeys()) {
          if ("time".equalsIgnoreCase(key.getName())) {
            timeKeyExists = true;
            break;
          }
        }
        if (timeKeyExists) {
          projects.put(projectName, project);
        } else {
          project = null;
        }
      }
    }

    if (project == null){
      project = new NoDataProject(selectedProjectId);
    }

    return project;
  }

}