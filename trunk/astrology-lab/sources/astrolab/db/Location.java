package astrolab.db;

import java.util.Hashtable;
import java.util.TimeZone;

import astrolab.web.component.tree.TreeObject;;

public class Location implements TreeObject {

  private int id;
  private double longitude = Double.NaN;
  private double lattitude = Double.NaN;
  private int region = -1;
  private TimeZone time_zone = null;

  private static Hashtable cache = new Hashtable();

  private Location(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public double getLongitude() {
    if (Double.isNaN(longitude)) {
      String value = Database.query("SELECT longitude FROM locations WHERE id = " + id);
      if (value != null) {
        longitude = Double.parseDouble(value);
      }
    }
    return longitude;
  }

  public double getLattitude() {
    if (Double.isNaN(lattitude)) {
      String value = Database.query("SELECT lattitude FROM locations WHERE id = " + id);
      if (value != null) {
        lattitude = Double.parseDouble(value);
      }
    }
    return lattitude;
  }

  public int getRegion() {
    if (region < 0) {
      String value = Database.query("SELECT region FROM locations WHERE id = " + id);
      if (value != null) {
        region = Integer.parseInt(value);
      }
    }
    return region;
  }

  public Location getById(int id) {
    return Location.getLocation(id);
  }

  public Location getParent() {
    return Location.getLocation(getRegion());
  }

  public TimeZone getTimeZone() {
    if (time_zone == null) {
      String value = Database.query("SELECT descrid FROM locations, text WHERE locations.id = " + id + " AND time_zone = text.id AND time_zone > 0");
      time_zone = (value != null) ? TimeZone.getTimeZone(value) : TimeZone.getDefault();
    }
    return time_zone;
  }

  public String toString() {
    return Text.getText(id);
  }

  public String toFullString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append(Text.getText(id));
    buffer.append(" (");
    buffer.append((int) Math.abs(getLongitude()));
    buffer.append((getLongitude() > 0) ? "W" : "E");
    buffer.append(roundLeft(getLongitude()));
    buffer.append(" ");
    buffer.append((int) Math.abs(getLattitude()));
    buffer.append((getLattitude() > 0) ? "N" : "S");
    buffer.append(roundLeft(getLattitude()));
    buffer.append(")");
    return buffer.toString();
  }

  public String getText(boolean toSelect) {
    return (toSelect) ? toFullString() : toString();
  }

  public static Location getLocation(int id) {
    Integer intId = new Integer(id);
    Location result = (Location) cache.get(intId);

    if (result == null) {
      result = new Location(id);
      cache.put(intId, result);
    }

    return result;
  }

  public static void store(String name, int region, double longitude, double lattitude, TimeZone zone) {
    int id = Text.reserve(name, Text.TYPE_REGION);
    int timeZone = Text.reserve(zone.getDisplayName(), zone.getID(), Text.TYPE_TIME_ZONE);
    Database.execute("INSERT INTO locations VALUES (" + id + ", " + region + ", '" + longitude + "', " + lattitude + ", '" + timeZone + "')");
  }

  private String roundLeft(double value) {
    double diff = Math.abs(value) - Math.floor(Math.abs(value));
    diff *= 60;
    return String.valueOf((int) diff);
  }

  public RecordIterator iterateChildren() {
    return LocationIterator.iterate(getId());
  }

  public RecordIterator iterateSubTrees() {
    return LocationIterator.iterateSubRegions(getId());
  }

}