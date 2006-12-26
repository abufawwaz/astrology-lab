package astrolab.project.statistics;

import astrolab.db.Database;
import astrolab.db.Event;
import astrolab.db.Personalize;
import astrolab.db.Text;

public class StatisticsRecord extends Event {

  public final static String TABLENAME = "project_statistics_value";

  private double value = Double.NaN;
  private double minValue = Double.NaN;
  private double maxValue = Double.NaN;

  StatisticsRecord(int id) {
    super(id);
  }

  StatisticsRecord(Event event, double value, double minValue, double maxValue) {
    super(event.getEventId(), event.getSubjectId(), event.getTime().getTime(), event.getLocation().getId(), event.getType(), event.getAccuracy(), event.getSource());
    this.value = value;
    this.minValue = minValue;
    this.maxValue = maxValue;
  }

  public double getValue() {
    if (value == Double.NaN) {
      value = Double.parseDouble(Database.query("SELECT record_value from " + TABLENAME + " WHERE event_id = " + getId()));
    }
    return value;
  }

  public double getMaxValue() {
    return (maxValue == Double.NaN) ? getValue() : maxValue;
  }

  public double getMinValue() {
    return (minValue == Double.NaN) ? getValue() : minValue;
  }

  public static int store(String name, double value, long timestamp, int location, String type, String accuracy, String source, int accessible_by) {
    int record = Text.reserve(name, name, Text.TYPE_EVENT, accessible_by);
    int selected_project = Personalize.getFavourite(-1, Text.getId("user.session.project"), -1);

    Event.store(record, record, timestamp, location, type, accuracy, source);
    Database.execute("INSERT INTO project_statistics_value VALUES (" + record + ", " + selected_project + ", " + value + ")");
    return record;
  }

}