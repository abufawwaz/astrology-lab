package astrolab.perspective.election;

import java.util.Calendar;

import astrolab.astronom.SpacetimeEvent;
import astrolab.criteria.Criteria;
import astrolab.criteria.Criterion;
import astrolab.web.SVGDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayHourlyElectionaryChart extends SVGDisplay {

  private final static int HEIGHT = 10;
  private final static float HEIGHT_LINE_WIDTH = (float) HEIGHT / 100;
  private final static float MINUTE_STEP = ((float) 15) / 60;

  private SpacetimeEvent timestamp;

  public DisplayHourlyElectionaryChart() {
    super.addAction("timestamp", "timestamp");
    super.addAction("criteria", "action");
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    Criterion[] criteria = Criteria.getCriteria();

    determineStartingTime(request, criteria);

    fillMarks(buffer, criteria);

    buffer.append("<g style='fill:blue;fill-opacity:0.2'>");
    for (int i = 0; i < 24; i += 2) {
      buffer.append("<rect x='");
      buffer.append(i);
      buffer.append("' width='1' height='");
      buffer.append(HEIGHT);
      buffer.append("' />");
    }
    buffer.append("</g>");
    buffer.append("<g style='fill:none;stroke:black;stroke-width:0.02'>");
    for (int i = 3; i < 24; i += 3) {
      buffer.newline();
      buffer.append("<circle cx='");
      buffer.append(i);
      buffer.append("' cy='1' r='0.6' />");
      buffer.append("<line x1='" + i + "' y1='0.4' x2='" + i + "' y2='1' />");
    }
    buffer.append("</g>");

    // mark line
    buffer.append("<g id='chart_mark_id' transform='translate(0 0)' style='stroke-width:.1;stroke:black'>");
    buffer.append("<g transform='translate(0 " + HEIGHT + ")'><text id='chart_mark_text' style='stroke:none;font-size:" + (((float) HEIGHT) / 20) + "pt'>??:??</text></g>");
    buffer.append("<line y2='");
    buffer.append(HEIGHT);
    buffer.append("' />");
    buffer.append("</g>");

    // select line
    buffer.append("<g id='chart_select_id' dispay='none' transform='translate(0 0)' style='stroke-width:.1;stroke:blue'>");
    buffer.append("<g transform='translate(0 " + HEIGHT + ")'><text id='chart_select_text' style='stroke:none;font-size:" + (((float) HEIGHT) / 20) + "pt'>??:??</text></g>");
    buffer.append("<line y2='");
    buffer.append(HEIGHT);
    buffer.append("' />");
    buffer.append("</g>");

    // background for the mouse listener
    buffer.append("<rect id='chart_id' width='24' height='");
    buffer.append(HEIGHT);
    buffer.append("' style='fill:blue;fill-opacity:0.01' />");

    buffer.newline();
    buffer.append("<script type='text/javascript'>");
    buffer.append(" document.getElementById('chart_id').onclick = function(evt) {");
    buffer.append("  var selected_hour = 24 * evt.clientX / document.rootElement.width.baseVal.value;");
    buffer.append("  moveMarkTo('chart_mark', evt);");
    buffer.append("  top.fireEvent(window, 'timestamp', Math.floor(start_timestamp + selected_hour * an_hour));");
    buffer.append(" }");
    buffer.newline();
    buffer.append(" document.getElementById('chart_id').onmousemove = function(evt) {");
    buffer.append("  moveMarkTo('chart_select', evt);");
    buffer.append(" }");
    buffer.newline();
    buffer.append(" document.getElementById('chart_id').onmouseout = function(evt) {");
    buffer.append("  document.getElementById('chart_select_id').setAttribute('display', 'none');");
    buffer.append(" }");
    buffer.newline();
    buffer.append(" document.getElementById('chart_id').onmouseover = function(evt) {");
    buffer.append("  document.getElementById('chart_select_id').setAttribute('display', 'inline');");
    buffer.append(" }");
    buffer.newline();
    buffer.append(" function moveMarkTo(mark, evt) {");
    buffer.append("  moveMarkToHour(mark, 24 * evt.clientX / document.rootElement.width.baseVal.value);");
    buffer.append(" }");
    buffer.newline();
    buffer.append(" function moveMarkToHour(mark, hour) {");
    buffer.append("  var text = document.createTextNode('' + Math.floor(hour) + ':' + Math.floor((hour - Math.floor(hour)) * 60));");
    buffer.append("  document.getElementById(mark + '_id').setAttribute('transform', 'translate(' + hour + ' 0)');");
    buffer.append("  var text_node = document.getElementById(mark + '_text');");
    buffer.append("  while (text_node.hasChildNodes()) text_node.removeChild(text_node.firstChild);");
    buffer.append("  text_node.appendChild(text);");
    buffer.append(" }");
    buffer.newline();

    long startTimeMillis = getCalendar(0).getTimeInMillis();
    float selectedHour = ((float) (timestamp.getTimeInMillis() - startTimeMillis)) / 1000 / 60 / 60;
    System.err.println(" start: " + getCalendar(0).toMySQLString() + "   -> " + new java.util.Date(getCalendar(0).getTimeInMillis()).toString());
    System.err.println(" time : " + new java.util.Date(timestamp.getTimeInMillis()).toString());
    System.err.println(" diff: " + (timestamp.getTimeInMillis() - startTimeMillis));
    buffer.append(" var start_timestamp = " + startTimeMillis);
    buffer.newline();
    buffer.append(" var an_hour = 60 * 60 * 1000");
    buffer.newline();
    buffer.append(" moveMarkToHour('chart_select', " + selectedHour + ");");
    buffer.append(" moveMarkToHour('chart_mark', " + selectedHour + ");");
    buffer.newline();
    buffer.append("</script>");
  }

  private final void determineStartingTime(Request request, Criterion[] criteria) {
    String timeParameter = request.get("timestamp");
    if (timeParameter != null) {
      timestamp = new SpacetimeEvent(Long.parseLong(timeParameter));
    }
    if (timestamp == null) {
      timestamp = ElectiveEnvironment.getStartingTime();
    }
  }

  private final void fillMarks(LocalizedStringBuffer buffer, Criterion[] criteria) {
    int[][][] marks = new int[criteria.length][(int) (24 / MINUTE_STEP)][2];
    int maxValue = 0;
    SpacetimeEvent periodStart;
    SpacetimeEvent periodEnd = getCalendar(0);

    for (int h = 0; h < 24 / MINUTE_STEP; h++) {
      periodStart = periodEnd;
      periodEnd = getCalendar(MINUTE_STEP * (h + 1));

      for (int c = 0; c < criteria.length; c++) {
        int value = criteria[c].getMark(periodStart, periodEnd) * criteria[c].getMultiplyBy();
        if (value > 0) {
          marks[c][h][0] = value + ((c > 0) ? marks[c - 1][h][0] : 0);
          marks[c][h][1] = ((c > 0) ? marks[c - 1][h][1] : 0);
        } else {
          marks[c][h][0] = ((c > 0) ? marks[c - 1][h][0] : 0);
          marks[c][h][1] = Math.abs(value) + ((c > 0) ? marks[c - 1][h][1] : 0);
        }
      }

      if (criteria.length > 0) {
        if (marks[criteria.length - 1][h][0] > maxValue) {
          maxValue = marks[criteria.length - 1][h][0];
        }
        if (marks[criteria.length - 1][h][1] > maxValue) {
          maxValue = marks[criteria.length - 1][h][1];
        }
      }
    }
    maxValue++;

    float YCENTER = (float) HEIGHT / 2;
    for (int s = 0; s < 2; s++) {
      for (int c = criteria.length - 1; c >= 0; c--) {
        buffer.append("<polygon points='");
        buffer.append(String.valueOf(24));
        buffer.append(",");
        buffer.append(YCENTER);
        buffer.append(" 0,");
        buffer.append(YCENTER);

        for (int h = 0; h < 24 / MINUTE_STEP; h ++) {
          float yaxis = YCENTER;
          buffer.append(" ");
          buffer.append(String.valueOf(MINUTE_STEP * h));
          buffer.append(",");
          if (s == 0) {
            yaxis -= yaxis * marks[c][h][s] / maxValue;
          } else {
            yaxis += yaxis * marks[c][h][s] / maxValue;
          }
          buffer.append(String.valueOf(yaxis));
        }
  
        buffer.append("' style='stroke:none;fill:");
        buffer.append(criteria[c].getColor());
        buffer.append("' />");
      }
    }

    buffer.append("<line x1='0' x2='");
    buffer.append(String.valueOf(24));
    buffer.append("' y1='");
    buffer.append(YCENTER);
    buffer.append("' y2='");
    buffer.append(YCENTER);
    buffer.append("' style='stroke:black;stroke-width:");
    buffer.append(HEIGHT_LINE_WIDTH);
    buffer.append("' />");
  }

  private final SpacetimeEvent getCalendar(float hour) {
    int hh = (int) hour;
    int mm = (int) ((hour - (int) hour) * 60);
    SpacetimeEvent result = timestamp;

    if (result.get(SpacetimeEvent.HOUR_OF_DAY) != hh) {
      result = result.getMovedSpacetimeEvent(SpacetimeEvent.HOUR_OF_DAY, hh - result.get(SpacetimeEvent.HOUR_OF_DAY));
    }
    if (result.get(SpacetimeEvent.MINUTE) != mm) {
      result = result.getMovedSpacetimeEvent(SpacetimeEvent.MINUTE, mm - result.get(SpacetimeEvent.MINUTE));
    }
    if (result.get(SpacetimeEvent.SECOND) != 0) {
      result = result.getMovedSpacetimeEvent(SpacetimeEvent.SECOND, - result.get(SpacetimeEvent.SECOND));
    }
    if (result.get(Calendar.MILLISECOND) != 0) {
      result = result.getMovedSpacetimeEvent(Calendar.MILLISECOND, - result.get(Calendar.MILLISECOND));
    }

    return result;
  }

  public void fillViewBox(Request request, LocalizedStringBuffer buffer) {
    buffer.append("preserveAspectRatio='none' viewBox='0 0 24 "+ HEIGHT + "'");
  }

}