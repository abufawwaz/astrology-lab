package astrolab.project;

import java.util.HashMap;

import astrolab.db.Database;
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
        String[] keys = listKeys(projectName);

        for (String key: keys) {
          if ("subject_id".equalsIgnoreCase(key)) {
            project = new SubjectDataProject(selectedProjectId, projectName);
            projects.put(projectName, project);
            return project;
          }
          if ("time".equalsIgnoreCase(key)) {
            project = new EventsDataProject(selectedProjectId, projectName);
            projects.put(projectName, project);
            return project;
          }
        }
      }
    }

    if (project == null){
      project = new NoDataProject(selectedProjectId);
    }

    return project;
  }

  private static String[] listKeys(String project) {
    return Database.queryList("SHOW FIELDS FROM " + Project.TABLE_PREFIX + project);
  }

}