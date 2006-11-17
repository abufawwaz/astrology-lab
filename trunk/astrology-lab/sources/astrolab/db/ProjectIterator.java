package astrolab.db;

import java.sql.Date;
import java.sql.ResultSet;

public class ProjectIterator {

  private ResultSet set;

  private ProjectIterator(ResultSet set) {
    this.set = set;
  }

  public static ProjectIterator iterateLabs() {
    String query = "SELECT * FROM project, text WHERE project.laboratory = text.id GROUP BY laboratory ORDER BY " + Personalize.getLanguage();

    return new ProjectIterator(Database.executeQuery(query));
  }

  public static ProjectIterator iterate(int laboratory) {
    String query = "SELECT * FROM project, text WHERE " + ((laboratory > 0) ? "laboratory = " + laboratory + "AND " : "") + "project.name = text.id ORDER BY " + Personalize.getLanguage();

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