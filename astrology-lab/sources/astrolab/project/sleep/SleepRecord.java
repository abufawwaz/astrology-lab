package astrolab.project.sleep;

import astrolab.db.Database;
import astrolab.db.Event;
import astrolab.db.Text;
import astrolab.project.relocation.RelocationRecord;

class SleepRecord extends Event {

  private final static String TABLENAME = "project_sleep";

  SleepRecord(int id) {
  	super(id);
  }

  public static int store(int person, long timestamp_from, long timestamp_to, String accuracy, String source) {
    int location = RelocationRecord.getLocationOfPerson(person, timestamp_from);
    int id_from = Text.reserve("Sleep:" + person + ":" + timestamp_from + ":" + System.currentTimeMillis(), Text.TYPE_EVENT);
    int id_to = Text.reserve("Sleep:" + person + ":" + timestamp_to + ":" + System.currentTimeMillis(), Text.TYPE_EVENT);

    Event.store(id_from, person, timestamp_from, location, Event.TYPE_EVENT, accuracy, source);
    Event.store(id_to, person, timestamp_to, location, Event.TYPE_EVENT, accuracy, source);

    Database.execute("INSERT INTO " + TABLENAME + " VALUES (" + id_from + ", " + id_to + ")");

    return id_from;
  }

}