package astrolab.project.archive;

import java.sql.Timestamp;

import astrolab.db.Database;
import astrolab.db.Event;
import astrolab.db.Text;

public class NatalRecord extends Event {

  public final static String TABLENAME = "project_archive";

  NatalRecord(int id) {
  	super(id);
  }

  public static int store(int natal_record_id, String name, long timestamp, int location, String type, String accuracy, String source, int accessible_by) {
    int user = natal_record_id;
    String sqltimestamp = new Timestamp(timestamp).toString();

    if (user <= 0) {
      user = Text.reserve(name, name, Text.TYPE_EVENT, accessible_by);
    } else {
      Text.changeText(user, name, accessible_by);
    }

    if (!exists(user)) {
      Database.execute("INSERT INTO " + TABLENAME + " VALUES (" + user + ", " + user + ", '" + sqltimestamp + "', " + location + ", '" + type + "', '" + accuracy + "', '" + source + "')");
    } else {
      Database.execute("UPDATE " + TABLENAME + " SET subject_id = " + user + " WHERE event_id = " + user);
      Database.execute("UPDATE " + TABLENAME + " SET event_time = '" + sqltimestamp + "' WHERE event_id = " + user);
      Database.execute("UPDATE " + TABLENAME + " SET location = " + location + " WHERE event_id = " + user);
      Database.execute("UPDATE " + TABLENAME + " SET type = '" + type + "' WHERE event_id = " + user);
      Database.execute("UPDATE " + TABLENAME + " SET accuracy = '" + accuracy + "' WHERE event_id = " + user);
      Database.execute("UPDATE " + TABLENAME + " SET source = '" + source + "' WHERE event_id = " + user);
    }
    return user;
  }

  public static boolean exists(int natal_record_id) {
    return (Database.query("SELECT * FROM " + TABLENAME + " where event_id = " + natal_record_id) != null);
  }

}