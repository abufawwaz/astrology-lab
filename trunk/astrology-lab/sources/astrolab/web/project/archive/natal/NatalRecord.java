package astrolab.web.project.archive.natal;

import astrolab.db.Database;
import astrolab.db.Event;
import astrolab.db.Text;

public class NatalRecord extends Event {

  public final static String TABLENAME = "project_human_birth";

  NatalRecord(int id) {
  	super(id);
  }

  public static int store(String name, long timestamp, int location, String type, String accuracy, String source, int accessible_by) {
    int user = Text.reserve(name, name, Text.TYPE_EVENT, accessible_by);

    Event.store(user, user, timestamp, location, type, accuracy, source);
    if (NatalRecord.TYPE_MALE.equals(type) || NatalRecord.TYPE_FEMALE.equals(type)) {
      Database.execute("DELETE FROM " + TABLENAME + " WHERE event_id = " + user); // TODO: optimize
      Database.execute("INSERT INTO " + TABLENAME + " VALUES (" + user + ")");
    } else {
      Database.execute("DELETE FROM " + TABLENAME + " WHERE event_id = " + user);
    }
    return user;
  }

  public static int count() {
    return Integer.parseInt(Database.query("SELECT COUNT(*) FROM archive, " + TABLENAME + " WHERE archive.event_id=" + TABLENAME + ".event_id"));
  }

}