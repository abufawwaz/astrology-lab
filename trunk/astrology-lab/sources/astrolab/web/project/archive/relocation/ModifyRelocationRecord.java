package astrolab.web.project.archive.relocation;

import astrolab.astronom.Time;
import astrolab.db.Event;
import astrolab.web.Modify;
import astrolab.web.component.ComponentSelectAccuracy;
import astrolab.web.component.ComponentSelectSource;
import astrolab.web.component.location.ComponentSelectLocation;
import astrolab.web.server.Request;

public class ModifyRelocationRecord extends Modify {

	public void operate(Request request) {
    try {
      int user = request.getUser();
      int location = ComponentSelectLocation.retrieve(request);
      long timestamp = new Time(request.get(Request.TEXT_DATE), location).getTimeInMillis();
      String accuracy = ComponentSelectAccuracy.retrieve(request);
      String source = ComponentSelectSource.retrieve(request);

      int record = RelocationRecord.store(user, timestamp, location, accuracy, source);
      Event.setSelectedEvent(record, 1);
    } catch (Exception e) {
      e.printStackTrace();
    }
	}

}