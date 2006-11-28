package astrolab.project.statistics;

import astrolab.astronom.Time;
import astrolab.web.HTMLDisplay;
import astrolab.web.component.ComponentSelectEvent;
import astrolab.web.project.archive.relocation.RelocationRecord;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentTableStatistics extends HTMLDisplay {

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    int user = request.getUser();
    // TODO: fix this!
    int location = RelocationRecord.getLocationOfPerson(user, new Time(request.get("_select_from_time"), 0).getTimeInMillis());
    Time from_time = new Time(request.get("_select_from_time"), location);
    Time to_time = new Time(request.get("_select_to_time"), location);

    ComponentSelectEvent.fill(buffer, request, StatisticsRecordIterator.iterate(from_time, to_time));
  }

}
