package astrolab.project.statistics;

import java.sql.ResultSet;

import astrolab.astronom.Time;
import astrolab.db.Database;
import astrolab.db.Event;
import astrolab.db.EventIterator;
import astrolab.db.Personalize;
import astrolab.db.Text;

public class StatisticsRecordIterator extends EventIterator {

  protected final static String QUERY = "SELECT archive.event_id, subject_id, event_time, location, type, accuracy, source, record_value FROM archive, project_statistics_value";

  protected StatisticsRecordIterator(ResultSet set) {
    super(set);
  }

  public static StatisticsRecordIterator iterate() {
    return iterate(null, null);
  }

  public static StatisticsRecordIterator iterate(Time from_time, Time to_time) {
    int selected_project = Personalize.getFavourite(-1, Text.getId("user.session.project"), -1);
    return iterate(selected_project, from_time, to_time);
  }

  public static StatisticsRecordIterator iterate(int project, Time from_time, Time to_time) {
    String query = QUERY +
        " WHERE project_id = " + project +
        " AND archive.event_id = project_statistics_value.event_id" +
        ((from_time != null) ? " AND event_time >= '" + from_time.toMySQLString() + "'" : "") +
        ((to_time != null) ? " AND event_time <= '" + to_time.toMySQLString() + "'" : "");
    return new StatisticsRecordIterator(Database.executeQuery(query));
  }

  protected Object read() throws Exception {
    return new StatisticsRecord((Event) super.read(), set.getDouble(8));
  }

}