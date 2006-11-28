package astrolab.project.statistics;

import java.sql.ResultSet;

import astrolab.astronom.Time;
import astrolab.db.Database;
import astrolab.db.EventIterator;
import astrolab.db.Personalize;
import astrolab.db.Text;

public class StatisticsRecordIterator extends EventIterator {

  protected final static String QUERY = "SELECT archive.event_id, subject_id, event_time, location, type, accuracy, source FROM archive";

  protected StatisticsRecordIterator(ResultSet set) {
    super(set);
  }

  public static StatisticsRecordIterator iterate(Time from_time, Time to_time) {
    int selected_project = Personalize.getFavourite(-1, Text.getId("user.session.project"), -1);
    return iterate(selected_project, from_time, to_time);
  }

  public static StatisticsRecordIterator iterate(int project, Time from_time, Time to_time) {
    String query = QUERY + " WHERE event_time >= '" + from_time.toMySQLString() + "' AND event_time <= '" + to_time.toMySQLString() + "'";
    return new StatisticsRecordIterator(Database.executeQuery(query));
  }

}