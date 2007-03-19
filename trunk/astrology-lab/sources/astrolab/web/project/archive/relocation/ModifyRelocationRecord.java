package astrolab.web.project.archive.relocation;

import astrolab.astronom.Time;
import astrolab.db.Event;
import astrolab.project.geography.ComponentSelectLocation;
import astrolab.web.Modify;
import astrolab.web.component.ComponentSelectAccuracy;
import astrolab.web.component.ComponentSelectSource;
import astrolab.web.component.time.ComponentSelectTime;
import astrolab.web.server.Request;

public class ModifyRelocationRecord extends Modify {

	public void operate(Request request) {
    try {
      int user = request.getUser();
      int location = ComponentSelectLocation.retrieve(request);

      long timestamp = new Time(request.get(ComponentSelectTime.PARAMETER_KEY), location).getTimeInMillis();
      String accuracy = ComponentSelectAccuracy.retrieve(request);
      String source = ComponentSelectSource.retrieve(request);

      int record = RelocationRecord.store(user, timestamp, location, accuracy, source);
      Event.setSelectedEvent(record, 1);
    } catch (Exception e) {
      e.printStackTrace();
    }
	}

}