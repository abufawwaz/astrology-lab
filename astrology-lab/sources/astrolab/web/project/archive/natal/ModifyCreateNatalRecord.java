package astrolab.web.project.archive.natal;

import astrolab.astronom.Time;
import astrolab.db.Event;
import astrolab.db.Text;
import astrolab.web.Modify;
import astrolab.web.component.ComponentSelectAccuracy;
import astrolab.web.component.ComponentSelectChoice;
import astrolab.web.component.ComponentSelectSource;
import astrolab.web.component.location.ComponentSelectLocation;
import astrolab.web.server.Request;

public class ModifyCreateNatalRecord extends Modify {

	public void operate(Request request) {
    try {
  		String name = request.get(Request.TEXT_NAME);
      int location = ComponentSelectLocation.retrieve(request);
      Time time = new Time(request.get(Request.TEXT_DATE), location);
      long timestamp = time.getTimeInMillis();
      String accuracy = ComponentSelectAccuracy.retrieve(request);
      String source = ComponentSelectSource.retrieve(request);
      String type = Text.getDescriptiveId(request.get("_event_type"));
      int accessible_by = Integer.parseInt(ComponentSelectChoice.retrieve(request, "_accessible_by"));
  
      NatalRecord.store(name, timestamp, location, type, accuracy, source, accessible_by);
      Event.setSelectedEvent(Text.getId(name), 1);
    } catch (Exception e) {
      e.printStackTrace();
    }
	}

}