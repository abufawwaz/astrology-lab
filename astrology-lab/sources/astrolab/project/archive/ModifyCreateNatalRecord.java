package astrolab.project.archive;

import astrolab.astronom.Time;
import astrolab.db.Event;
import astrolab.db.Text;
import astrolab.project.geography.ComponentSelectLocation;
import astrolab.web.Modify;
import astrolab.web.component.ComponentSelectAccuracy;
import astrolab.web.component.ComponentSelectChoice;
import astrolab.web.component.ComponentSelectNumber;
import astrolab.web.component.ComponentSelectSource;
import astrolab.web.component.time.ComponentSelectTime;
import astrolab.web.server.Request;
import astrolab.web.server.RequestParameters;

public class ModifyCreateNatalRecord extends Modify {

	public void operate(Request request) {
    try {
      int event_id = (int) ComponentSelectNumber.retrieve(request, "_event_id");
  		String name = request.get(RequestParameters.TEXT_NAME);
      int location = ComponentSelectLocation.retrieve(request);
      Time time = new Time(request.get(ComponentSelectTime.PARAMETER_KEY), location);
      long timestamp = time.getTimeInMillis();
      String accuracy = ComponentSelectAccuracy.retrieve(request);
      String source = ComponentSelectSource.retrieve(request);
      String type = Text.getDescriptiveId(request.get("_event_type"));
      int accessible_by = Integer.parseInt(ComponentSelectChoice.retrieve(request, "_accessible_by"));

      NatalRecord.store(event_id, name, timestamp, location, type, accuracy, source, accessible_by);
      Event.setSelectedEvent(Text.getId(name), 1);
    } catch (Exception e) {
      e.printStackTrace();
    }
	}

}