package astrolab.project;

import astrolab.astronom.Time;

public class TimeSpan {

  private Time fromTime = null;
  private Time toTime = null;

  public Time getFromTime() {
    return fromTime;
  }

  public Time getToTime() {
    return toTime;
  }

  public static TimeSpan getTimeSpan() {
    return new TimeSpan();
  }
}