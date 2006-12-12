package astrolab.web.project.archive.relocation;

import java.sql.Timestamp;
import java.util.Date;

import astrolab.db.Database;
import astrolab.db.Event;
import astrolab.db.Text;

public class RelocationRecord extends Event {

  private final static String TABLENAME = "project_relocation";

  protected RelocationRecord(int id) {
    super(id);
  }

  protected RelocationRecord(int id, int person, Date timestamp, int location, String accuracy, String source) {
    super(id, person, timestamp, location, Event.TYPE_EVENT, accuracy, source);
  }

  public static int store(int person, long timestamp, int location, String accuracy, String source) {
    int id = Text.reserve("Relocation:" + person + ":" + timestamp, Text.TYPE_EVENT);

    Event.store(id, person, timestamp, location, Event.TYPE_EVENT, accuracy, source);
    Database.execute("INSERT INTO " + TABLENAME + " VALUES (" + id + ")");

    return id;
  }

  public static int count() {
    return Integer.parseInt(Database.query("SELECT COUNT(*) FROM archive, " + TABLENAME + " WHERE archive.event_id=" + TABLENAME + ".event_id"));
  }

  public static int getLocationOfPerson(int person, long timestamp) {
    String sqltimestamp = new Timestamp(timestamp).toString();
    String sql = "SELECT location, event_time FROM archive, " + TABLENAME + " WHERE (archive.event_id=" + TABLENAME + ".event_id OR archive.event_id=" + person + ") AND archive.subject_id=" + person + " AND archive.event_time < '" + sqltimestamp + "' ORDER BY event_time DESC";
    return Integer.parseInt(Database.query(sql));
  }

}