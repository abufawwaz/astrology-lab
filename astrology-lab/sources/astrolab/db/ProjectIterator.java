package astrolab.db;

import java.sql.Date;
import java.sql.ResultSet;

import astrolab.perspective.Perspective;

public class ProjectIterator {

  private Project nextProject = null;
  private ResultSet set;

  private ProjectIterator(ResultSet set) {
    this.set = set;

    readNextProject();
  }

  public static ProjectIterator iterate() {
    String query = "SELECT * FROM project, text WHERE project.name = text.id ORDER BY laboratory, " + Personalize.getLanguage();

    return new ProjectIterator(Database.executeQuery(query));
  }

  public boolean hasNext() {
    return (nextProject != null);
  }

  public Project next() {
    Project result = nextProject;

    try {
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      readNextProject();
    }
  }

  private void readNextProject() {
    nextProject = null;

    try {
      if (set != null && set.next()) {
        int name = set.getInt(1);
        int laboratory = set.getInt(2);
        String type = set.getString(3);
        Date started = set.getDate(4);
        int description = set.getInt(5);
        
        nextProject = new Project(name, laboratory, type, started, description);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    if ((nextProject != null) && !Perspective.isProjectAccepted(nextProject)) {
      readNextProject();
    }
  }

}