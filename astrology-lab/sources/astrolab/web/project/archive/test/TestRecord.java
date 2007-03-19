package astrolab.web.project.archive.test;

import astrolab.db.Database;
import astrolab.db.Event;
import astrolab.db.Text;
import astrolab.project.relocation.RelocationRecord;

class TestRecord extends Event {

  TestRecord(int id) {
  	super(id);
  }

  public static int store(int test, int person, long timestamp, int[] data) {
    int location = RelocationRecord.getLocationOfPerson(person, timestamp);
    int id = Text.reserve("Test" + data[0] + ":" + person + ":" + timestamp, Text.TYPE_EVENT);

    Event.store(id, person, timestamp, location, Event.TYPE_EVENT, Event.ACCURACY_SECOND, Event.SOURCE_ACCURATE);
    String table = "project_" + Text.getDescriptiveId(test);
    String sql = "INSERT INTO " + table + " VALUES (" + id;
    for (int i = 0; i < data.length; i++) {
      sql += ", " + data[i];
    }
    sql += ")";

    Database.execute(sql);
    return id;
  }

}