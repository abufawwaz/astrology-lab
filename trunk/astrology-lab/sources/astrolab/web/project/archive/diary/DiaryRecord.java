package astrolab.web.project.archive.diary;

import astrolab.db.Database;
import astrolab.db.Event;
import astrolab.db.Text;
import astrolab.project.relocation.RelocationRecord;

class DiaryRecord extends Event {

  private final static String TABLENAME = "project_diary";

  DiaryRecord(int id) {
  	super(id);
  }

  public static int store(int person, long timestamp, String description, String accuracy, String source) {
    int location = RelocationRecord.getLocationOfPerson(person, timestamp);
    int id = Text.reserve("Diary:" + person + ":" + timestamp + ":" + System.currentTimeMillis(), Text.TYPE_EVENT);

    Event.store(id, person, timestamp, location, Event.TYPE_EVENT, accuracy, source);
    Database.execute("INSERT INTO " + TABLENAME + " VALUES (" + id + ", '" + description + "')");
    return id;
  }

  public static int count() {
    return Integer.parseInt(Database.query("SELECT COUNT(*) FROM archive, " + TABLENAME + " WHERE archive.event_id=" + TABLENAME + ".event_id"));
  }

}