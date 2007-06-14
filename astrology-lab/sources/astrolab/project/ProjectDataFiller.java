package astrolab.project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Hashtable;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.Time;
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

      Connection connection = Database.getConnection();
      connection.setAutoCommit(false);
      PreparedStatement query = connection.prepareStatement("SELECT time FROM " + Project.TABLE_PREFIX + project.getName() + " WHERE " + key + " IS NULL LIMIT 360");
      PreparedStatement update = connection.prepareStatement("UPDATE " + Project.TABLE_PREFIX + project.getName() + " SET " + key + " = ? WHERE time = ?");

      while (true) {
        boolean isEmpty = true;
        ResultSet set = query.executeQuery();

        while (set.next()) {
          isEmpty = false;
          Timestamp time = set.getTimestamp(1);
          Time astrotime = new Time(time);
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

}