package astrolab.project.statistics;

import astrolab.db.Database;
import astrolab.db.Event;
import astrolab.db.Personalize;
import astrolab.db.Text;

public class StatisticsRecord extends Event {

  public final static String TABLENAME = "project_statistics_value";

  StatisticsRecord(int id) {
  	super(id);
  }

  public double getValue() {
    return Double.parseDouble(Database.query("SELECT record_value from " + TABLENAME + " WHERE event_id = " + getId()));
  }

  public static int store(String name, double value, long timestamp, int location, String type, String accuracy, String source, int accessible_by) {
    int record = Text.reserve(name, name, Text.TYPE_EVENT, accessible_by);
    int selected_project = Personalize.getFavourite(-1, Text.getId("user.session.project"), -1);

    Event.store(record, record, timestamp, location, type, accuracy, source);
    Database.execute("INSERT INTO project_statistics_value VALUES (" + record + ", " + selected_project + ", " + value + ")");
    return record;
  }

}