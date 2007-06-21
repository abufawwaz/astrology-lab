package astrolab.db;

import java.sql.ResultSet;
import java.util.Date;

import astrolab.astronom.SpacetimeEvent;

import astrolab.project.archive.DisplayArchiveRecordList;
import astrolab.project.geography.Location;
import astrolab.web.server.Request;

public class Event extends SpacetimeEvent {

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

  private int id;
  private int subject_id;
  private String type;
  private String accuracy;
  private String source;

  public static Event getSelectedEvent() {
    return getSelectedEvent(1);
  }

  public static Event getSelectedEvent(int number) {
    String temporarySelection = Request.getCurrentRequest().get("user.session.event." + number);
    int selection = 0;
    try {
      selection = Integer.parseInt(temporarySelection);
    } catch (NumberFormatException nfe) {
    }

    if (selection <= 0) {
      selection = Personalize.getFavourite(-1, Text.getId("user.session.event." + number), 0);
    }
    if (selection <= 0) {
      selection = Personalize.getUser();
    }

    return new Event(selection);
  }

  public static int[] getSelectedEvents() {
    int[] result = new int[2];

    for (int i = 0; i < result.length; i++) {
      result[i] = Personalize.getFavourite(-1, Text.getId("user.session.event." + (i + 1)), 0);
      if (result[i] <= 0) {
        result[i] = Personalize.getUser();
      }
    }

    return result;
  }

  public static void setSelectedEvent(int event, int number) {
    if (event >= Text.TYPE_EVENT) {
      Personalize.addFavourite(-1, event, Text.getId("user.session.event." + number));
      Personalize.addFavourite(DisplayArchiveRecordList.ID, event);
    }
  }

  public Event(int id) {
    this.id = id;

    try {
      ResultSet set = Database.executeQuery("SELECT subject_id, event_time, location, type, accuracy, source FROM project_archive WHERE event_id = " + id);
  
      if (set != null && set.next()) {
        subject_id = set.getInt(1);
        int location = set.getInt(3);
        type = set.getString(4);
        accuracy = set.getString(5);
        source = set.getString(6);

        init(set.getTimestamp(2).getTime(), Location.getLocation(location));
      } else {
        throw new IllegalStateException("No event with id " + id);
      }
    } catch (Exception e) {
      e.printStackTrace();

      throw new IllegalStateException("Record of event with id " + id + " cannot be parsed.");
    }
  }

  protected Event(int event, int subject, Date timestamp, int location, String type, String accuracy, String source) {
    this.id = event;
  	this.subject_id = subject;
    this.type = type;
    this.accuracy = accuracy;
    this.source = source;

    init(timestamp.getTime(), Location.getLocation(location));
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

  public int getId() {
    return id;
  }

  public String toString() {
    return "Event: " + getId() + " type: " + getType();
  }

  public static void store(int id, int person, long timestamp, int location, String type, String accuracy, String source) {
    throw new IllegalStateException();
//    String sqltimestamp = new Timestamp(timestamp).toString();
//
//    if (Database.query("SELECT * FROM archive where event_id = " + id) == null) {
//      Database.execute("INSERT INTO archive VALUES (" + id + ", " + person + ", '" + sqltimestamp + "', " + location + ", '" + type + "', '" + accuracy + "', '" + source + "')");
//    } else {
//      Database.execute("UPDATE archive SET subject_id = " + person + " WHERE event_id = " + id);
//      Database.execute("UPDATE archive SET event_time = '" + sqltimestamp + "' WHERE event_id = " + id);
//      Database.execute("UPDATE archive SET location = " + location + " WHERE event_id = " + id);
//      Database.execute("UPDATE archive SET type = '" + type + "' WHERE event_id = " + id);
//      Database.execute("UPDATE archive SET accuracy = '" + accuracy + "' WHERE event_id = " + id);
//      Database.execute("UPDATE archive SET source = '" + source + "' WHERE event_id = " + id);
//    }
  }

}