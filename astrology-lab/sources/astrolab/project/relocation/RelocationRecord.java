package astrolab.project.relocation;

import java.sql.Timestamp;
import java.util.Date;

import astrolab.db.Database;
import astrolab.db.Event;

public class RelocationRecord extends Event {

  private final static String TABLENAME = "project_relocation";

  protected RelocationRecord(int id) {
    super(id);
  }

  protected RelocationRecord(int person, Date timestamp, int location) {
    super(-1, person, timestamp, location, Event.TYPE_EVENT, Event.ACCURACY_DAY, Event.SOURCE_RECALLED);
  }

  public static int getLocationOfPerson(int person, long timestamp) {
    String sqltimestamp = new Timestamp(timestamp).toString();
    String sqlNatal = "SELECT location, event_time FROM project_archive WHERE project_archive.event_id=" + person;
    String sqlRelocation = "SELECT location, time as event_time FROM " + TABLENAME + " WHERE " + TABLENAME + ".subject_id=" + person + " AND " + TABLENAME + ".time < '" + sqltimestamp + "' ORDER BY event_time DESC";
    return Integer.parseInt(Database.query(sqlNatal + " UNION " + sqlRelocation));
  }

  public static void store(int user, long timestamp, int location) {
    String sqltimestamp = new Timestamp(timestamp).toString();

    Database.execute("INSERT INTO " + TABLENAME + " (subject_id, time, location) VALUES (" + user + ", '" + sqltimestamp + "', " + location + ")");
  }

}