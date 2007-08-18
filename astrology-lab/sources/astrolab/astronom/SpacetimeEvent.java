package astrolab.astronom;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import astrolab.astronom.util.Trigonometry;
import astrolab.astronom.util.Zodiac;
import astrolab.db.Personalize;
import astrolab.db.Text;
import astrolab.project.geography.Location;

public class SpacetimeEvent {

  public final static int YEAR = Calendar.YEAR;
  public final static int MONTH = Calendar.MONTH;
  public final static int MAX_DAY_OF_MONTH = -Calendar.DAY_OF_MONTH;
  public final static int DAY_OF_WEEK = Calendar.DAY_OF_WEEK;
  public final static int DAY_OF_MONTH = Calendar.DAY_OF_MONTH;
  public final static int DAY_OF_YEAR = Calendar.DAY_OF_YEAR;
  public final static int HOUR_OF_DAY = Calendar.HOUR_OF_DAY;
  public final static int MINUTE = Calendar.MINUTE;
  public final static int SECOND = Calendar.SECOND;
  public final static TimeZone GMT_TIME_ZONE = TimeZone.getTimeZone("GMT");

  private final static String[] MONTHS_SHORT = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

  private double ra = Double.NaN;
  private double ob = Double.NaN;
  private double gmtJulianHour;
  private double gmtJulianDay;
  private double gmtJulianYear;
  private Calendar calendar = Calendar.getInstance();
  private Location location = null;

  protected SpacetimeEvent() {
  }

  private SpacetimeEvent(Calendar calendar, Location location) {
    init(calendar.getTimeInMillis(), location);
  }

  public SpacetimeEvent(long timestamp) {
    init(timestamp, null);
  }

  public SpacetimeEvent(long timestamp, Location location) {
    init(timestamp, location);
  }

  public SpacetimeEvent(long time, TimeZone zone) {
    init(time, Location.getLocation(zone));
  }

  /**
   * @param text "kk:mm:ss dd-MM-yyyy"
   */
  public SpacetimeEvent(String text, int location) {
    try {
      DateFormat dateFormat = new SimpleDateFormat("kk:mm:ss dd-MM-yyyy");
      TimeZone zone = Location.getLocation(location).getTimeZone();
      dateFormat.setTimeZone(zone);

      init(dateFormat.parse(text).getTime(), Location.getLocation(location));
    } catch (NullPointerException e) {
      init(System.currentTimeMillis(), null);
    } catch (ParseException e) {
      e.printStackTrace();
      init(System.currentTimeMillis(), null);
    }
  }

  protected final void init(long time, Location location) {
    this.calendar.setTimeInMillis(time);
    this.calendar.setTimeZone(GMT_TIME_ZONE);

    // calculate GMT Julian day
    int im = (12 * (calendar.get(Calendar.YEAR) + 4800)) + (calendar.get(Calendar.MONTH) - 2);
    double j = (2 * (im - (im / 12) * 12) + 7 + 365 * im) / 12;
    j = ((int) j) + calendar.get(Calendar.DAY_OF_MONTH) + (im / 48) - 32083;
    if (j > 2299171) {
      j = j + (im / 4800) - (im / 1200) + 38;
    }
    this.gmtJulianDay = j;

    // calculate GMT hour of day
    Calendar gmt = Calendar.getInstance(GMT_TIME_ZONE);
    gmt.setTimeInMillis(calendar.getTimeInMillis());
    this.gmtJulianHour = gmt.get(Calendar.HOUR_OF_DAY);
    this.gmtJulianHour += ((double) gmt.get(Calendar.MINUTE)) / 60;
    this.gmtJulianHour += ((double) gmt.get(Calendar.SECOND)) / 3600;

    // calculate Julian year time
    this.gmtJulianYear = ((this.gmtJulianDay - 2415020) + ((this.gmtJulianHour / 24) - 0.5)) / 36525;

    if ((location != null) && (location.getId() != 0)) {
      this.location = location;
    } else {
      this.location = Personalize.getLocation();
    }

    if ((this.location == null) || (this.location.getId() == 0)) {
      this.location = Location.getLocation(GMT_TIME_ZONE);
    }

    this.calendar.setTimeZone(this.location.getTimeZone());
  }

  public boolean isBefore(SpacetimeEvent event) {
    return this.getTimeInMillis() < event.getTimeInMillis();
  }

  public int get(int key) {
    if (key >= 0) {
      return calendar.get(key);
    } else {
      return calendar.getActualMaximum(-key);
    }
  }

  public long getTimeInMillis() {
    return calendar.getTimeInMillis();
  }

  public final Location getLocation() {
    return location;
  }

  public final double getStandardYearTime() {
    return gmtJulianYear;
  }

  public final double getRa() {
    if (Double.isNaN(ra)) {
      double t = gmtJulianYear;
      ra = Trigonometry.radians(Zodiac.degree((6.6460656 + (2400.0513 * t) + (2.58E-05 * t * t) + gmtJulianHour) * 15 - location.getLongitude()));
    }
    return ra;
  }

  public final double getOb() {
    if (Double.isNaN(ob)) {
      ob = Trigonometry.radians(23.452294 - 0.0130125 * gmtJulianYear);
    }
    return ob;
  }

  public String toString() {
    return calendar.get(Calendar.DAY_OF_MONTH) + " " + Text.getText(MONTHS_SHORT[calendar.get(Calendar.MONTH)]) + " " + calendar.get(Calendar.YEAR) + " "
            + format(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + format(calendar.get(Calendar.MINUTE)) + ":" + format(calendar.get(Calendar.SECOND)) + " "
            + "[" + location.getTimeZone().getID() + "]"
            + (location.getTimeZone().inDaylightTime(calendar.getTime()) ? " DST" : "");
  }

  public final String toSimpleString() {
    return calendar.get(Calendar.DAY_OF_MONTH) + " " + Text.getText(MONTHS_SHORT[calendar.get(Calendar.MONTH)]) + " " + calendar.get(Calendar.YEAR) + " "
            + format(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + format(calendar.get(Calendar.MINUTE)) + ":" + format(calendar.get(Calendar.SECOND));
  }

  public final String toMySQLString() {
    return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " "
            + format(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + format(calendar.get(Calendar.MINUTE)) + ":" + format(calendar.get(Calendar.SECOND));
  }

  public SpacetimeEvent getMovedSpacetimeEvent(int offsetType, int offsetValue) {
    Calendar calendar = Calendar.getInstance(this.calendar.getTimeZone());
    calendar.setTimeInMillis(this.calendar.getTimeInMillis());
    calendar.add(offsetType, offsetValue);
    return new SpacetimeEvent(calendar, location);
  }

  private final String format(int data) {
    String result = "00" + data;
    return result.substring(result.length() - 2);
  }

  public int hashCode() {
    return ((int) gmtJulianDay) & 0xFFFFFFFF;
  }

  public boolean equals(Object object) {
    if (object instanceof SpacetimeEvent) {
      SpacetimeEvent event = (SpacetimeEvent) object;

      if (this.location.getId() != event.location.getId()) {
        return false;
      } else if (this.getTimeInMillis() != event.getTimeInMillis()) {
        return false;
      } else {
        return true;
      }
    } else {
      return false;
    }
  }
}
