package astrolab.project;

import java.util.HashMap;

import astrolab.db.Personalize;
import astrolab.db.Text;

public class Projects {

  private static HashMap<String, Project> projects = new HashMap<String, Project>();

  public static Project getProject() {
    int selectedProjectId = Personalize.getFavourite(-1, Text.getId("user.session.project"), -1);
    String projectName = Text.getDescriptiveId(selectedProjectId);
    Project project = projects.get(projectName);

    if (project == null) {
      project = new Project(projectName);
      projects.put(projectName, project);
    }

    return project;
  }

}