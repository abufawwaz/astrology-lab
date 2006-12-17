package astrolab.project.statistics;

import java.util.ArrayList;

import astrolab.astronom.Time;
import astrolab.db.Location;
import astrolab.db.Personalize;
import astrolab.db.Text;
import astrolab.web.SVGDisplay;
import astrolab.web.project.archive.relocation.RelocationRecord;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentChartStatistics extends SVGDisplay {

  private final static int POLYGON_GROUP_SIZE = 30;

  private ArrayList<StatisticsRecord> list = null;
  private Time from_time;
  private Time to_time;

  protected int strokeWidth = 1;

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    if (list == null) {
      return;
    }

    buffer.append("<text x='100' y='100'>");
    buffer.append((from_time != null) ? from_time.toSimpleString() : "Start");
    buffer.append(" - ");
    buffer.append((to_time != null) ? to_time.toSimpleString() : "End");
    buffer.append("</text>");

    int lastx = 0;
    int lasty = 0;
    buffer.append("<g style='fill:none;");
    buffer.append(decorateLine());
    buffer.append("'>");
    for (int i = 0; i < list.size(); i += POLYGON_GROUP_SIZE) {
      buffer.append("<polyline points='");
      buffer.append(lastx);
      buffer.append(",");
      buffer.append(lasty);
      buffer.append(" ");
      for (int j = 0; (j < POLYGON_GROUP_SIZE) && (i + j < list.size()); j++) {
        StatisticsRecord record = list.get(i + j);
        lastx = (int) (record.getTime().getTimeInMillis() / 1000000);
        buffer.append(lastx);
        buffer.append(",");
        lasty = calculateValue(record);
        buffer.append(lasty);
        buffer.append(" ");
      }
      buffer.append("' />");
    }
    buffer.append("</g>");
  }

  public void fillViewBox(Request request, LocalizedStringBuffer buffer) {
    from_time = getTime(request, Text.getId("user.selection.x1"));
    to_time = getTime(request, Text.getId("user.selection.x2"));
    int maxValue = Personalize.getFavourite(-1, Text.getId("user.selection.y2"), -1);
    boolean yChanged = false;

    StatisticsRecordIterator iterator = StatisticsRecordIterator.iterate(from_time, to_time);
    if (!iterator.hasNext()) {
      System.err.println("NO records !!!");
      return;
    }

    list = new ArrayList<StatisticsRecord>();
    while (iterator.hasNext()) {
      StatisticsRecord record = (StatisticsRecord) iterator.next();
      int value = calculateValue(record);

      list.add(record);

      if (value > maxValue) {
        maxValue = value;
        yChanged = true;
      }
    }

    int firstRecordTime = (int) (list.get(0).getTime().getTimeInMillis() / 1000000);
    if (from_time == null) {
      Personalize.addFavourite(-1, firstRecordTime, Text.getId("user.selection.x1"));
    }

    int lastRecordTime = (int) (list.get(list.size() - 1).getTime().getTimeInMillis() / 1000000);
    if (to_time == null) {
      Personalize.addFavourite(-1, lastRecordTime, Text.getId("user.selection.x2"));
    }

    if (yChanged) {
      Personalize.addFavourite(-1, maxValue, Text.getId("user.selection.y2"));
    }

    buffer.append("preserveAspectRatio='none' viewBox='" + firstRecordTime + " 0 " + (lastRecordTime - firstRecordTime) + " " + maxValue + "'");
  }

  protected String decorateLine() {
    return "stroke:green;stroke-width:" + strokeWidth;
  }

  protected int calculateValue(StatisticsRecord record) {
    return (int) record.getValue();
  }

  private final Time getTime(Request request, int time_id) {
    int user = request.getUser();
    // TODO: fix this! Get the location and time of the selection
    Location location = Location.getLocation(RelocationRecord.getLocationOfPerson(user, System.currentTimeMillis()));
    Time time = null;
    int time_data = Personalize.getFavourite(-1, time_id, -1);
    if (time_data != -1) {
      time = new Time(((long) time_data) * 1000000, location.getTimeZone());
    }
    return time;
  }
}
