package astrolab.project.relocation;

import astrolab.astronom.Time;
import astrolab.project.geography.ComponentSelectLocation;
import astrolab.web.Modify;
import astrolab.web.component.time.ComponentSelectTime;
import astrolab.web.server.Request;

public class ModifyRelocationRecord extends Modify {

	public void operate(Request request) {
    try {
      int user = request.getUser();
      int location = ComponentSelectLocation.retrieve(request);

      long timestamp = new Time(request.get(ComponentSelectTime.PARAMETER_KEY), location).getTimeInMillis();

      if (user > 0 && location > 0) {
        RelocationRecord.store(user, timestamp, location);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
	}

}