package astrolab.project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Hashtable;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.SpacetimeEvent;
import astrolab.db.Database;
import astrolab.tools.Log;

public class ProjectDataFiller extends Thread {

  private String key;
  private Project project;

  private final static Hashtable<String, ProjectDataFiller> FILLER = new Hashtable<String, ProjectDataFiller>();

  private ProjectDataFiller(String key, Project project) {
    this.key = key;
    this.project = project;

    start();
  }

  public static void startFiller(String key, Project project) {
    ProjectDataFiller filler = FILLER.get(key + "@" + project.getName());

    if (filler == null) {
      filler = new ProjectDataFiller(key, project);
      FILLER.put(key + "@" + project.getName(), filler);
    }
  }

  public static void endFiller(String key, Project project) {
    FILLER.remove(key + "@" + project.getName());
  }

  public void run() {
    double position;

    try {
      setPriority(Thread.MIN_PRIORITY);
      Log.beSilent(true);

      ensureColumn(key);

      PreparedStatement query;
      PreparedStatement update;
      Connection connection = Database.getConnection();
      connection.setAutoCommit(false);
      String table = Project.TABLE_PREFIX + project.getName();
      String projectTableKey = null;
      for (ProjectDataKey key: project.getKeys()) {
        if ("time".equals(key.getName())) {
          projectTableKey = "time";
          break;
        } else if ("event_time".equals(key.getName())) {
          projectTableKey = "event_time";
          break;
        }
      }
      if (projectTableKey != null) {
        query = connection.prepareStatement("SELECT " + projectTableKey + " FROM " + table + " WHERE " + table + "." + key + " IS NULL LIMIT 360");
        update = connection.prepareStatement("UPDATE " + table + " SET " + table + "." + key + " = ? WHERE " + projectTableKey + " = ?");
      } else {
        query = connection.prepareStatement("SELECT project_archive.event_time FROM " + table + ", project_archive WHERE " + table + "." + key + " IS NULL AND " + table + ".subject_id = project_archive.event_id LIMIT 360");
        update = connection.prepareStatement("UPDATE " + table + " SET " + table + "." + key + " = ? WHERE subject_id IN (SELECT event_id FROM project_archive WHERE project_archive.event_time = ?)");
      }

      while (true) {
        boolean isEmpty = true;
        ResultSet set = query.executeQuery();

        while (set.next()) {
          isEmpty = false;
          Timestamp time = set.getTimestamp(1);
//if (projectTableKey != null) {
//System.err.println(" query  '" + "SELECT " + projectTableKey + " FROM " + table + " WHERE " + table + "." + key + " IS NULL LIMIT 360" + "'");
//System.err.println(" update '" + "UPDATE " + table + " SET " + table + "." + key + " = ? WHERE " + projectTableKey + " = ?" + "'");
//} else {
//  System.err.println(" query '" + "SELECT project_archive.event_time FROM " + table + ", project_archive WHERE " + table + "." + key + " IS NULL AND " + table + ".subject_id = project_archive.event_id LIMIT 360" + "'");
//  System.err.println(" update '" + "UPDATE " + table + " SET " + table + "." + key + " = ? WHERE subject_id = (SELECT event_id FROM project_archive WHERE project_archive.event_time = ?)" + "'");
//}
          SpacetimeEvent astrotime = new SpacetimeEvent(time.getTime());
//          solar.calculate(new InMemoryEvent(astrotime)); // TODO: calculate based on time!
          position = ActivePoint.getActivePoint(key, astrotime).getPosition();

          update.setDouble(1, position);
          update.setTimestamp(2, time);
          update.execute();
        }

        set.close();
        connection.commit();
        if (isEmpty) {
          break;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      endFiller(key, project);
    }
  }

  private void ensureColumn(String key) {
    for (ProjectDataKey column: project.getKeys()) {
      if (column.getName().equalsIgnoreCase(key)) {
        return;
      }
    }

    Database.execute("ALTER TABLE " + Project.TABLE_PREFIX + project.getName() + " ADD COLUMN " + key + " DOUBLE KEY");
    Database.execute("ALTER TABLE " + Project.TABLE_PREFIX + project.getName() + " ADD INDEX USING BTREE (" + key + ")");
    project.refresh();
  }
}