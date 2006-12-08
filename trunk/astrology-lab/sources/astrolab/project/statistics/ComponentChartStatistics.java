package astrolab.project.statistics;

import java.util.ArrayList;

import astrolab.astronom.Time;
import astrolab.web.SVGDisplay;
import astrolab.web.project.archive.relocation.RelocationRecord;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentChartStatistics extends SVGDisplay {

  private final static int POLYGON_GROUP_SIZE = 30;

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    int user = request.getUser();
    // TODO: fix this!
    int location = RelocationRecord.getLocationOfPerson(user, new Time(request.get("_select_from_time"), 0).getTimeInMillis());
    Time from_time = new Time(request.get("_select_from_time"), location);
    Time to_time = new Time(request.get("_select_to_time"), location);

    StatisticsRecordIterator iterator = StatisticsRecordIterator.iterate(from_time, to_time);
    ArrayList<StatisticsRecord> list = new ArrayList<StatisticsRecord>();

    if (!iterator.hasNext()) {
      buffer.append("No records selected.");
      return;
    }

    while (iterator.hasNext()) {
      list.add((StatisticsRecord) iterator.next());
    }

    long firstRecordTime = list.get(0).getTime().getTimeInMillis() >> 1000000;
    long lastRecordTime = list.get(list.size() - 1).getTime().getTimeInMillis() / 1000000;

    buffer.append("<svg:svg version='1.1' baseProfile='full' width='100%' height='100%' preserveAspectRatio='none' viewBox='0 0 " + ((int) (lastRecordTime - firstRecordTime)) + " 1000'>");
    buffer.append("<svg:text x='100' y='100'>");
    buffer.append((from_time != null) ? from_time.toSimpleString() : "Start");
    buffer.append(" - ");
    buffer.append((to_time != null) ? to_time.toSimpleString() : "End");
    buffer.append("</svg:text>");

    int lastx = 0;
    int lasty = 0;
    buffer.append("<svg:g style='fill:none;stroke:black;stroke-width:1'>");
    for (int i = 0; i < list.size(); i += POLYGON_GROUP_SIZE) {
      buffer.append("<svg:polyline points='");
      buffer.append(lastx);
      buffer.append(",");
      buffer.append(lasty);
      buffer.append(" ");
      for (int j = 0; (j < POLYGON_GROUP_SIZE) && (i + j < list.size()); j++) {
        StatisticsRecord record = list.get(i + j);
        lastx = (int) (record.getTime().getTimeInMillis() / 1000000 - firstRecordTime);
        buffer.append(lastx);
        buffer.append(",");
        lasty = (int) record.getValue();
        buffer.append(lasty);
        buffer.append(" ");
      }
      buffer.append("' />");
    }
    buffer.append("</svg:g>");

    buffer.append("</svg:svg>");
  }

}
