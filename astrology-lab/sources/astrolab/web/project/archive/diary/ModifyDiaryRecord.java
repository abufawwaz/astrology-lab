package astrolab.web.project.archive.diary;

import astrolab.astronom.Time;
import astrolab.db.Event;
import astrolab.web.Modify;
import astrolab.web.project.archive.relocation.RelocationRecord;
import astrolab.web.server.Request;

public class ModifyDiaryRecord extends Modify {

	public void operate(Request request) {
    try {
      int user = request.getUser();
      // TODO: fix this!
      int location = RelocationRecord.getLocationOfPerson(user, new Time(request.get(Request.TEXT_DATE), 0).getTimeInMillis());
      long timestamp = new Time(request.get(Request.TEXT_DATE), location).getTimeInMillis();

      int record = DiaryRecord.store(user, timestamp, request.get(Request.TEXT_NAME), request.get(Request.CHOICE_ACCURACY), request.get(Request.CHOICE_SOURCE));
      Event.setSelectedEvent(record, 1);
    } catch (Exception e) {
      e.printStackTrace();
    }
	}

}