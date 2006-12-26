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

  private ArrayList<StatisticsRecord> list = null;
  private Time from_time;
  private Time to_time;

  protected int strokeWidth = 1;
  private int recordsPerChart = 50;
  private int timeSlice = 100000000;

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    if ((list == null) || (list.size() == 0)) {
      // no statistic records -> no chart
      return;
    }

    buffer.append("<text x='100' y='100'>");
    buffer.append((from_time != null) ? from_time.toSimpleString() : "Start");
    buffer.append(" - ");
    buffer.append((to_time != null) ? to_time.toSimpleString() : "End");
    buffer.append("</text>");

    buffer.append("<g style='fill:none;");
    buffer.append(decorateLine());
    buffer.append("'>");

    for (int i = 0; i < list.size(); i++) {
      StatisticsRecord record = list.get(i);
      int x = (int) (record.getTime().getTimeInMillis() / timeSlice);

      buffer.append("<line x1='");
      buffer.append(x);
      buffer.append("' y1='");
      buffer.append((int) record.getMinValue());
      buffer.append("' x2='");
      buffer.append(x);
      buffer.append("' y2='");
      buffer.append((int) record.getMaxValue());
      buffer.append("' />");
    }

    StatisticsRecord record = list.get(0);
    buffer.append("<polyline points='");
    buffer.append((int) (record.getTime().getTimeInMillis() / timeSlice));
    buffer.append(",");
    buffer.append((int) record.getValue());
    buffer.append(" ");
    for (int i = 1; i < list.size(); i++) {
      record = list.get(i);
      buffer.append((int) (record.getTime().getTimeInMillis() / timeSlice));
      buffer.append(",");
      buffer.append((int) record.getValue());
      buffer.append(" ");
    }
    buffer.append("' />");
    buffer.append("</g>");
  }

  public void fillViewBox(Request request, LocalizedStringBuffer buffer) {
    from_time = getTime(request, Text.getId("user.selection.x1"));
    to_time = getTime(request, Text.getId("user.selection.x2"));
    int maxValue = Personalize.getFavourite(-1, Text.getId("user.selection.y2"), -1);
    boolean yChanged = false;

    StatisticsRecordIterator iterator = StatisticsRecordIterator.iterate(recordsPerChart);
    if (!iterator.hasNext()) {
      System.err.println("NO records !!!");
      return;
    }

    list = new ArrayList<StatisticsRecord>();
    while (iterator.hasNext()) {
      StatisticsRecord record = (StatisticsRecord) iterator.next();
      int value = (int) record.getValue();

      list.add(record);

      if (value > maxValue) {
        maxValue = value;
        yChanged = true;
      }
    }

    int firstRecordTime = (int) (list.get(0).getTime().getTimeInMillis() / timeSlice);
    if (from_time == null) {
      Personalize.addFavourite(-1, firstRecordTime, Text.getId("user.selection.x1"));
    }

    int lastRecordTime = (int) (list.get(list.size() - 1).getTime().getTimeInMillis() / timeSlice);
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

  private final Time getTime(Request request, int time_id) {
    int user = request.getUser();
    // TODO: fix this! Get the location and time of the selection
    Location location = Location.getLocation(RelocationRecord.getLocationOfPerson(user, System.currentTimeMillis()));
    Time time = null;
    int time_data = Personalize.getFavourite(-1, time_id, -1);
    if (time_data != -1) {
      time = new Time(((long) time_data) * timeSlice, location.getTimeZone());
    }
    return time;
  }
}
