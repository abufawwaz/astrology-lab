package astrolab.web.project.archive.relocation;

import java.sql.ResultSet;
import java.util.Date;

import astrolab.db.Database;
import astrolab.db.EventIterator;

public class IteratorRelocationRecords extends EventIterator {

  private IteratorRelocationRecords(ResultSet set) {
    super(set);
  }

  public static IteratorRelocationRecords iterate(int user) {
    return new IteratorRelocationRecords(Database.executeQuery(EventIterator.QUERY + ", project_relocation WHERE subject_id = " + user + " ORDER BY event_time"));
  }

  protected Object read() throws Exception {
    int subject = set.getInt(1);
    Date timestamp = set.getTimestamp(2);
    int location = set.getInt(3);
    
    return new RelocationRecord(subject, timestamp, location);
  }

}