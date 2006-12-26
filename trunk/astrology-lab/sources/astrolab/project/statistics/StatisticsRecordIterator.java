package astrolab.project.statistics;

import java.sql.ResultSet;

import astrolab.astronom.Time;
import astrolab.db.Database;
import astrolab.db.Event;
import astrolab.db.EventIterator;
import astrolab.db.Personalize;
import astrolab.db.Text;

public class StatisticsRecordIterator extends EventIterator {

  protected final static String QUERY = "SELECT archive.event_id, subject_id, event_time, location, type, accuracy, source, record_value, record_value, record_value FROM archive, project_statistics_value";

  protected StatisticsRecordIterator(ResultSet set) {
    super(set);
  }

  public static StatisticsRecordIterator iterate() {
    return iterate(null, null);
  }

  public static StatisticsRecordIterator iterate(int slots) {
    return iterate(null, null, slots);
  }

  public static StatisticsRecordIterator iterate(Time from_time, Time to_time) {
    int selected_project = Personalize.getFavourite(-1, Text.getId("user.session.project"), -1);
    return iterate(selected_project, from_time, to_time);
  }

  public static StatisticsRecordIterator iterate(Time from_time, Time to_time, int slots) {
    int selected_project = Personalize.getFavourite(-1, Text.getId("user.session.project"), -1);
    return iterate(selected_project, from_time, to_time, slots);
  }

  public static StatisticsRecordIterator iterate(int project, Time from_time, Time to_time) {
    String query = QUERY +
        " WHERE project_id = " + project +
        " AND archive.event_id = project_statistics_value.event_id" +
        ((from_time != null) ? " AND event_time >= '" + from_time.toMySQLString() + "'" : "") +
        ((to_time != null) ? " AND event_time <= '" + to_time.toMySQLString() + "'" : "") +
        " ORDER BY event_time";
    return new StatisticsRecordIterator(Database.executeQuery(query));
  }

  public static StatisticsRecordIterator iterate(int project, Time from_time, Time to_time, int slots) {
    String fromSQLTime = (from_time != null) ? from_time.toMySQLString() : Database.query("select min(event_time) from archive, project_statistics_value where archive.event_id = project_statistics_value.event_id");
    String toSQLTime = (to_time != null) ? to_time.toMySQLString() : Database.query("select max(event_time) from archive, project_statistics_value where archive.event_id = project_statistics_value.event_id");
    String query = "SELECT " +
        "archive.event_id, " +
        "archive.subject_id, " +
        "archive.event_time, " +
        "archive.location, " +
        "archive.type, " +
        "archive.accuracy, " +
        "archive.source, " +
        "avg(record_value), " +
        "min(record_value), " +
        "max(record_value), " +
        "floor(TO_DAYS(event_time) / (datediff('" + toSQLTime + "', '" + fromSQLTime + "') / " + slots + ")) * (datediff('" + toSQLTime + "', '" + fromSQLTime + "') / " + slots + ") as slot " +
      "from archive, project_statistics_value " +
      "where archive.event_id = project_statistics_value.event_id " +
        ((from_time != null) ? "AND event_time >= '" + fromSQLTime + "' " : "") +
        ((to_time != null) ? "AND event_time <= '" + toSQLTime + "' " : "") +
      "group by slot";
    return new StatisticsRecordIterator(Database.executeQuery(query));
  }

  protected Object read() throws Exception {
    return new StatisticsRecord((Event) super.read(), set.getDouble(8), set.getDouble(9), set.getDouble(10));
  }

}