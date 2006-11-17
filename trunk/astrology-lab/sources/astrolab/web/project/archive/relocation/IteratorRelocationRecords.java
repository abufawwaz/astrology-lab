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
    return new IteratorRelocationRecords(Database.executeQuery(EventIterator.QUERY + ", project_relocation WHERE archive.event_id = project_relocation.event_id AND archive.subject_id = " + user + " ORDER BY archive.event_time"));
  }

  protected Object read() throws Exception {
    int event = set.getInt(1);
    int subject = set.getInt(2);
    Date timestamp = set.getTimestamp(3);
    int location = set.getInt(4);
    String accuracy = set.getString(6);
    String source = set.getString(7);
    
    return new RelocationRecord(event, subject, timestamp, location, accuracy, source);
  }

}