package astrolab.project.relocation;

import java.sql.ResultSet;
import java.util.Date;

import astrolab.db.Database;
import astrolab.db.EventIterator;

public class IteratorRelocationRecords extends EventIterator {

  private IteratorRelocationRecords(ResultSet set) {
    super(set);
  }

  public static IteratorRelocationRecords iterate(int user) {
    return new IteratorRelocationRecords(Database.executeQuery("SELECT subject_id, time, location FROM project_relocation WHERE subject_id = " + user + " ORDER BY time"));
  }

  protected Object read() throws Exception {
    int subject = set.getInt(1);
    Date timestamp = set.getTimestamp(2);
    int location = set.getInt(3);
    
    return new RelocationRecord(subject, timestamp, location);
  }

}