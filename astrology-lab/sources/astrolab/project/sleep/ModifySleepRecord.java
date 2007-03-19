package astrolab.project.sleep;

import astrolab.astronom.Time;
import astrolab.db.Event;
import astrolab.project.relocation.RelocationRecord;
import astrolab.web.Modify;
import astrolab.web.component.ComponentSelectAccuracy;
import astrolab.web.component.ComponentSelectSource;
import astrolab.web.server.Request;

public class ModifySleepRecord extends Modify {

	public void operate(Request request) {
    if (request.get("_sleep_from") == null) {
      return;
    }
    try {
      int user = request.getUser();
      // TODO: fix this!
      int location = RelocationRecord.getLocationOfPerson(user, new Time(request.get("_sleep_from"), 0).getTimeInMillis());
      long sleep_from = new Time(request.get("_sleep_from"), location).getTimeInMillis();
      long sleep_to = new Time(request.get("_sleep_to"), location).getTimeInMillis();

      int record = SleepRecord.store(user, sleep_from, sleep_to, ComponentSelectAccuracy.retrieve(request), ComponentSelectSource.retrieve(request));
      Event.setSelectedEvent(record, 1);
    } catch (Exception e) {
      e.printStackTrace();
    }
	}

}