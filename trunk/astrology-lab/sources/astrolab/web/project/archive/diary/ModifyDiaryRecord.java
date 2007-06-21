package astrolab.web.project.archive.diary;

import astrolab.astronom.SpacetimeEvent;
import astrolab.db.Event;
import astrolab.project.relocation.RelocationRecord;
import astrolab.web.Modify;
import astrolab.web.component.ComponentSelectAccuracy;
import astrolab.web.component.ComponentSelectSource;
import astrolab.web.component.time.ComponentSelectTime;
import astrolab.web.server.Request;
import astrolab.web.server.RequestParameters;

public class ModifyDiaryRecord extends Modify {

	public void operate(Request request) {
    try {
      int user = request.getUser();
      // TODO: fix this!
      int location = RelocationRecord.getLocationOfPerson(user, new SpacetimeEvent(request.get(ComponentSelectTime.PARAMETER_KEY), 0).getTimeInMillis());
      long timestamp = new SpacetimeEvent(request.get(ComponentSelectTime.PARAMETER_KEY), location).getTimeInMillis();
      String accuracy = ComponentSelectAccuracy.retrieve(request);
      String source = ComponentSelectSource.retrieve(request);

      int record = DiaryRecord.store(user, timestamp, request.get(RequestParameters.TEXT_NAME), accuracy, source);
      Event.setSelectedEvent(record, 1);
    } catch (Exception e) {
      e.printStackTrace();
    }
	}

}