package astrolab.project.geography;

import java.util.Hashtable;
import java.util.TimeZone;

import astrolab.db.Database;
import astrolab.db.RecordIterator;
import astrolab.db.Text;
import astrolab.web.component.tree.TreeObject;;

public class Location implements TreeObject {

  private int id;
  private double longitude = Double.NaN;
  private double lattitude = Double.NaN;
  private int region = -1;
  private TimeZone time_zone = null;

  private static Hashtable<Integer, Location> cache = new Hashtable<Integer, Location>();
  private static Hashtable<TimeZone, Location> zones = new Hashtable<TimeZone, Location>();

  private Location(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public double getLongitude() {
    if (Double.isNaN(longitude)) {
      String value = Database.query("SELECT longitude FROM " + ProjectGeography.TABLE_NAME + " WHERE id = " + id);
      if (value != null) {
        longitude = Double.parseDouble(value);
      }
    }
    return longitude;
  }

  public double getLattitude() {
    if (Double.isNaN(lattitude)) {
      String value = Database.query("SELECT lattitude FROM " + ProjectGeography.TABLE_NAME + " WHERE id = " + id);
      if (value != null) {
        lattitude = Double.parseDouble(value);
      }
    }
    return lattitude;
  }

  public int getRegion() {
    if (region < 0) {
      String value = Database.query("SELECT region FROM " + ProjectGeography.TABLE_NAME + " WHERE id = " + id);
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
      String value = Database.query("SELECT descrid FROM " + ProjectGeography.TABLE_NAME + ", text WHERE " + ProjectGeography.TABLE_NAME + ".id = " + id + " AND time_zone = text.id AND time_zone > 0");
      time_zone = (value != null) ? TimeZone.getTimeZone(value) : TimeZone.getDefault();
    }
    return time_zone;
  }

  public String toString() {
    return Text.getText(id);
  }

  public String getDescription() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("(");
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

  public String getText() {
    return Text.getText(id);
  }

  public static Location getLocation(int id) {
    Location result = cache.get(id);

    if (result == null) {
      result = new Location(id);
      cache.put(id, result);
    }

    return result;
  }

  public static Location getLocation(TimeZone zone) {
    Location result = zones.get(zone);
    if (result == null) {
      // TODO: get the correct center of the time zone
      result = Location.getLocation(Text.getId("Sofia"));
      zones.put(zone, result);
    }
    return result;
  }

  public static int store(String nameEn, int region, double longitude, double lattitude, TimeZone zone) {
    int id = Text.reserve(nameEn, Text.TYPE_REGION);
    int timeZone = Text.reserve(zone.getDisplayName(), zone.getID(), Text.TYPE_TIME_ZONE);
    Database.execute("INSERT INTO " + ProjectGeography.TABLE_NAME + " VALUES (" + id + ", " + region + ", '" + longitude + "', " + lattitude + ", '" + timeZone + "')");
    return id;
  }

  public static void update(int id, int region, double longitude, double lattitude, TimeZone zone) {
    Location location = Location.getLocation(id);
    int timeZone = Text.reserve(zone.getDisplayName(), zone.getID(), Text.TYPE_TIME_ZONE);
    if (location.region != region && region != 0) {
      Database.execute("UPDATE " + ProjectGeography.TABLE_NAME + " SET region = " + region + " WHERE id = " + id);
    }
    if (location.longitude != longitude && longitude != 0) {
      Database.execute("UPDATE " + ProjectGeography.TABLE_NAME + " SET longitude = " + longitude + " WHERE id = " + id);
    }
    if (location.lattitude != lattitude && lattitude != 0) {
      Database.execute("UPDATE " + ProjectGeography.TABLE_NAME + " SET lattitude = " + lattitude + " WHERE id = " + id);
    }
    Database.execute("UPDATE " + ProjectGeography.TABLE_NAME + " SET time_zone = " + timeZone + " WHERE id = " + id);
    cache.remove(id);
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