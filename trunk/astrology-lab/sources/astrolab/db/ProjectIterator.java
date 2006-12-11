package astrolab.db;

import java.sql.Date;
import java.sql.ResultSet;

public class ProjectIterator {

  private ResultSet set;

  private ProjectIterator(ResultSet set) {
    this.set = set;
  }

  public static ProjectIterator iterate() {
    String query = "SELECT * FROM project, text WHERE project.name = text.id ORDER BY laboratory, " + Personalize.getLanguage();

    return new ProjectIterator(Database.executeQuery(query));
  }

  public boolean hasNext() {
    try {
      if (set == null) {
        return false;
      } else if (!set.isLast()) {
        return true;
      } else {
        set.close();
        set = null;
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public Project next() {
    try {
      if (set != null && set.next()) {
        int name = set.getInt(1);
        int laboratory = set.getInt(2);
        String type = set.getString(3);
        Date started = set.getDate(4);
        int description = set.getInt(5);
        
        return new Project(name, laboratory, type, started, description);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

}