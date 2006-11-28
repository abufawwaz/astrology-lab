package astrolab.project.statistics;

import java.util.Date;

import astrolab.db.Event;

public class InMemoryEvent extends Event {

  public InMemoryEvent(int event, int subject, Date timestamp, int location, String type, String accuracy, String source) {
    super(event, subject, timestamp, location, type, accuracy, source);
  }

}
