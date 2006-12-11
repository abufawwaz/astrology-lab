package astrolab.db;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

import astrolab.astronom.Time;

import astrolab.astronom.util.Trigonometry;
import astrolab.astronom.util.Zodiac;
import astrolab.web.project.archive.natal.DisplayNatalRecords;

public class Event extends AttributedObject {

  public final static String ACCURACY_SECOND = "a second";
  public final static String ACCURACY_MINUTE = "a minute";
  public final static String ACCURACY_5_MINUTES = "5 minutes";
  public final static String ACCURACY_10_MINUTES = "10 minutes";
  public final static String ACCURACY_30_MINUTES = "30 minutes";
  public final static String ACCURACY_HOUR = "an hour";
  public final static String ACCURACY_FEW_HOURS = "few hours";
  public final static String ACCURACY_DAY = "a day";
  public final static String ACCURACY_WEEK = "a week";
  public final static String ACCURACY_MONTH = "a month";
  public final static String ACCURACY_YEAR = "an year";

  public final static String SOURCE_ACCURATE = "accurate";
  public final static String SOURCE_RECTIFIED = "rectified";
  public final static String SOURCE_RECALLED = "recalled";
  public final static String SOURCE_GUESSED = "guessed";
  public final static String SOURCE_PLANNED = "planned";

  public final static String TYPE_MALE = "male";
  public final static String TYPE_FEMALE = "female";
  public final static String TYPE_EVENT = "event";

  private int subject_id;
  private Time timestamp;
  private int location;
  private String type;
  private String accuracy;
  private String source;

  public static Event getSelectedEvent() {
    return getSelectedEvent(1);
  }

  public static Event getSelectedEvent(int number) {
    int selection = Personalize.getFavourite(-1, Text.getId("user.session.event." + number), 0);
    if (selection <= 0) {
      selection = Personalize.getUser(true);
    }

    return new Event(selection);
  }

  public static int[] getSelectedEvents() {
    int[] result = new int[2];

    for (int i = 0; i < result.length; i++) {
      result[i] = Personalize.getFavourite(-1, Text.getId("user.session.event." + (i + 1)), 0);
      if (result[i] <= 0) {
        result[i] = Personalize.getUser(true);
      }
    }

    return result;
  }

  public static void setSelectedEvent(int event, int number) {
    if (event >= Text.TYPE_EVENT && event < Text.TYPE_LABORATORY) { // TODO: create a method to distinguish events
      Personalize.addFavourite(-1, event, Text.getId("user.session.event." + number));
      Personalize.addFavourite(DisplayNatalRecords.ID, event);
    }
  }

  public Event(int id) {
  	super(id);

  	try {
	  	ResultSet set = Database.executeQuery("SELECT subject_id, event_time, location, type, accuracy, source FROM archive WHERE event_id = " + id);
	
	    if (set != null && set.next()) {
        subject_id = set.getInt(1);
        location = set.getInt(3);
        type = set.getString(4);
        accuracy = set.getString(5);
        source = set.getString(6);

        timestamp = new Time(set.getTimestamp(2).getTime(), getLocation().getTimeZone());
	    }
  	} catch (Exception e) {
  		e.printStackTrace();

      timestamp = new Time();
  	}
  }

  protected Event(int event, int subject, Date timestamp, int location, String type, String accuracy, String source) {
  	super(event);
    this.subject_id = subject;
    this.location = location;
    this.type = type;
    this.accuracy = accuracy;
    this.source = source;

    this.timestamp = new Time(timestamp.getTime(), getLocation().getTimeZone());
  }

  public Time getTime() {
    return timestamp;
  }

  public Location getLocation() {
    return Location.getLocation(location);
  }

  public String getSubject() {
    return Text.getText(getSubjectId());
  }

  public int getSubjectId() {
    return subject_id;
  }

  public String getAccuracy() {
    return accuracy;
  }

  public String getType() {
    return type;
  }

  public String getSource() {
    return source;
  }

  public String getEvent() {
    return Text.getText(getId());
  }

  public int getEventId() {
    return getId();
  }

  public double getRa() {
    double t = getTime().getJulianYearTime();
    return Trigonometry.radians(Zodiac.degree((6.6460656 + (2400.0513 * t) + (2.58E-05 * t * t) + timestamp.getGMTHourOfDay()) * 15 - getLocation().getLongitude()));
  }

  public double getOb() {
    return Trigonometry.radians(23.452294 - 0.0130125 * getTime().getJulianYearTime());
  }

  public static void store(int id, int person, long timestamp, int location, String type, String accuracy, String source) {
    String sqltimestamp = new Timestamp(timestamp).toString();

    if (Database.query("SELECT * FROM archive where event_id = " + id) == null) {
      Database.execute("INSERT INTO archive VALUES (" + id + ", " + person + ", '" + sqltimestamp + "', " + location + ", '" + type + "', '" + accuracy + "', '" + source + "')");
    } else {
      Database.execute("UPDATE archive SET subject_id = " + person + " WHERE event_id = " + id);
      Database.execute("UPDATE archive SET event_time = '" + sqltimestamp + "' WHERE event_id = " + id);
      Database.execute("UPDATE archive SET location = " + location + " WHERE event_id = " + id);
      Database.execute("UPDATE archive SET type = '" + type + "' WHERE event_id = " + id);
      Database.execute("UPDATE archive SET accuracy = '" + accuracy + "' WHERE event_id = " + id);
      Database.execute("UPDATE archive SET source = '" + source + "' WHERE event_id = " + id);
    }
  }

}