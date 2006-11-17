package astrolab.db;

import java.sql.ResultSet;
import java.util.Date;

public class EventIterator extends RecordIterator {

  protected final static String QUERY = "SELECT archive.event_id, subject_id, event_time, location, type, accuracy, source FROM archive";

  public final static int SORT_BY_DATE_ASC = 1;
  public final static int SORT_BY_DATE_DESC = 2;

  protected EventIterator(ResultSet set) {
    super(set);
  }

  public static EventIterator iterate(String like) {
    String query = "SELECT DISTINCT(archive.event_id), subject_id, event_time, location, type, accuracy, source FROM archive, text" +
      " WHERE (en LIKE '%" + like + "%' OR bg LIKE '%" + like + "%')" +
      " AND (id = event_id OR id = subject_id OR id = location)";
    return iterate(query, 0, 0);
  }

  public static EventIterator iterate(int sort, int limit) {
    return iterate(QUERY, sort, limit);
  }

  public static EventIterator iterateSameType(int type, int sort, int limit) {
    String query = QUERY + " AND event_id = " + type;
    return iterate(query, sort, limit);
  }

  public static EventIterator iterateSameSubject(int subject, int sort, int limit) {
    String query = QUERY + " AND subject_id = " + subject;
    return iterate(query, sort, limit);
  }

  private static EventIterator iterate(String rawquery, int sort, int limit) {
    String query = rawquery;

    switch (sort) {
      case SORT_BY_DATE_ASC: {
        query += " ORDER BY event_time";
        break;
      }
      case SORT_BY_DATE_DESC: {
        query += " ORDER BY event_time DESC";
        break;
      }
    }
    if (limit > 0) {
      query += " LIMIT " + limit;
    }
    return new EventIterator(Database.executeQuery(query));
  }

  protected Object read() throws Exception {
    int event = set.getInt(1);
    int subject = set.getInt(2);
    Date timestamp = set.getTimestamp(3);
    int location = set.getInt(4);
    String type = set.getString(5);
    String accuracy = set.getString(6);
    String source = set.getString(7);
    
    return new Event(event, subject, timestamp, location, type, accuracy, source);
  }

}