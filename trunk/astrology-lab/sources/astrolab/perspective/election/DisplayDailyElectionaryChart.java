package astrolab.perspective.election;

import java.util.Calendar;

import astrolab.astronom.SpacetimeEvent;
import astrolab.criteria.Criteria;
import astrolab.criteria.Criterion;
import astrolab.criteria.CriterionStartTime;
import astrolab.web.SVGDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayDailyElectionaryChart extends SVGDisplay {

  private final static String[] MONTHS = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

  private final static int HEIGHT = 10;
  private final static int WIDTH = 31;
  private final static int HOUR_STEP = 6;

  private int DAYS = 31;
  private SpacetimeEvent timestamp;

  public DisplayDailyElectionaryChart() {
    super.addAction("timestamp", "javascript:updateTime(message)");
    super.addAction("criteria", "action");
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    Criterion[] criteria = Criteria.getCriteria();

    determineStartingTime(criteria);

    fillMarks(buffer, criteria);
    fillBackground(buffer);
    fillMarkLine(buffer);
    fillSelectLine(buffer);
    fillForeground(buffer);
    fillScript(buffer);
  }

  private final void determineStartingTime(Criterion[] criteria) {
    for (int i = 0; i < criteria.length; i++) {
      if (criteria[i] instanceof CriterionStartTime) {
        timestamp = ((CriterionStartTime) criteria[i]).getStartTime();
      }
    }
    if (timestamp == null) {
      timestamp = new SpacetimeEvent(System.currentTimeMillis());
    }
    DAYS = timestamp.get(SpacetimeEvent.MAX_DAY_OF_MONTH);
  }

  private final SpacetimeEvent getCalendar(int day, int hour) {
    Calendar result = Calendar.getInstance();
    result.setTimeInMillis(timestamp.getTimeInMillis());
    result.set(Calendar.DAY_OF_MONTH, day + 1);
    result.set(Calendar.HOUR_OF_DAY, hour + 1);
    return new SpacetimeEvent(result.getTimeInMillis());
  }

  private final void fillMarks(LocalizedStringBuffer buffer, Criterion[] criteria) {
    int[][] marks = new int[criteria.length][DAYS * 24 / HOUR_STEP];
    int maxValue = 0;
    SpacetimeEvent periodStart;
    SpacetimeEvent periodEnd;

    for (int d = 0; d < DAYS; d ++) {
      periodEnd = getCalendar(d, 0);
      for (int h = 0; h < 24; h += HOUR_STEP) {
        periodStart = periodEnd;
        periodEnd = getCalendar(d, h + 1);
  
        for (int c = 0; c < criteria.length; c++) {
          marks[c][(d * 24 + h) / HOUR_STEP] = criteria[c].getMark(periodStart, periodEnd) + ((c > 0) ? marks[c - 1][(d * 24 + h) / HOUR_STEP] : 0);
        }
  
        if ((criteria.length > 0) && marks[criteria.length - 1][(d * 24 + h) / HOUR_STEP] > maxValue) {
          maxValue = marks[criteria.length - 1][(d * 24 + h) / HOUR_STEP];
        }
      }
    }
    maxValue++;

    for (int c = criteria.length - 1; c >= 0; c--) {
      buffer.append("<polygon points='");
      buffer.append(String.valueOf(DAYS));
      buffer.append(",0 0,0");

      for (int d = 0; d < DAYS; d ++) {
        for (int h = 0; h < 24; h += HOUR_STEP) {
          buffer.append(" ");
          buffer.append(String.valueOf(d));
          buffer.append(".");
          buffer.append(String.valueOf((int) (((float) h) / 24 * 100)));
          buffer.append(",");
          buffer.append(String.valueOf(((float) HEIGHT * marks[c][(d * 24 + h) / HOUR_STEP]) / maxValue));
        }
      }

      buffer.append("' style='stroke:none;fill:");
      buffer.append(criteria[c].getColor());
      buffer.append("' />");
    }
  }

  private final void fillBackground(LocalizedStringBuffer buffer) {
    buffer.append("<g style='fill:blue;fill-opacity:0.2'>");
    for (int i = 0; i < DAYS; i += 2) {
      buffer.append("<rect x='");
      buffer.append(i);
      buffer.append("' width='1' height='");
      buffer.append(HEIGHT);
      buffer.append("' />");
    }
    buffer.append("</g>");
  }

  private final void fillMarkLine(LocalizedStringBuffer buffer) {
    // mark line
    buffer.append("<g id='chart_mark_id' transform='translate(0 0)' style='stroke-width:.1;stroke:black'>");
    buffer.append("<g transform='translate(0 " + HEIGHT + ")'><text id='chart_mark_text' style='stroke:none;font-size:" + (((float) HEIGHT) / 20) + "pt'>?? " + MONTHS[timestamp.get(Calendar.MONTH)] + "</text></g>");
    buffer.append("<line y2='");
    buffer.append(HEIGHT);
    buffer.append("' />");
    buffer.append("</g>");
  }

  private final void fillSelectLine(LocalizedStringBuffer buffer) {
    // select line
    buffer.append("<g id='chart_select_id' dispay='none' transform='translate(0 0)' style='stroke-width:.1;stroke:blue'>");
    buffer.append("<g transform='translate(0 " + HEIGHT + ")'><text id='chart_select_text' style='stroke:none;font-size:" + (((float) HEIGHT) / 20) + "pt'>?? " + MONTHS[timestamp.get(Calendar.MONTH)] + "</text></g>");
    buffer.append("<line y2='");
    buffer.append(HEIGHT);
    buffer.append("' />");
    buffer.append("</g>");
  }

  private final void fillForeground(LocalizedStringBuffer buffer) {
    // background for the mouse listener
    buffer.append("<rect id='chart_id' width='" + DAYS + "' height='");
    buffer.append(HEIGHT);
    buffer.append("' style='fill:blue;fill-opacity:0.01' />");
  }

  private final void fillScript(LocalizedStringBuffer buffer) {
    buffer.newline();
    buffer.append("<script type='text/javascript'>");
    buffer.append(" document.getElementById('chart_id').onclick = function(evt) {");
    buffer.append("  selected_day = Math.floor(" + WIDTH + " * evt.clientX / document.rootElement.width.baseVal.value);");
    buffer.append("  moveMarkTo('chart_mark', evt);");
    buffer.append("  top.fireEvent(window, 'timestamp', Math.floor(start_timestamp + selected_day * a_day + selected_hour));");
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
    buffer.append("  var day = Math.floor(" + WIDTH + " * evt.clientX / document.rootElement.width.baseVal.value);");
    buffer.append("  var text = document.createTextNode('' + (day + 1) + ' " + MONTHS[timestamp.get(Calendar.MONTH)] + "');");
    buffer.append("  document.getElementById(mark + '_id').setAttribute('transform', 'translate(' + day + ' 0)');");
    buffer.append("  var text_node = document.getElementById(mark + '_text');");
    buffer.append("  while (text_node.hasChildNodes()) text_node.removeChild(text_node.firstChild);");
    buffer.append("  text_node.appendChild(text);");
    buffer.append(" }");
    buffer.newline();
    buffer.append(" var start_timestamp = " + getCalendar(0, 0).getTimeInMillis());
    buffer.newline();
    buffer.append(" var a_day = 1000 * 60 * 60 * 24");
    buffer.newline();
    buffer.append(" var selected_day = 0");
    buffer.newline();
    buffer.append(" var selected_hour = 0");
    buffer.newline();
    buffer.append(" function updateTime(timestamp) {");
    buffer.append("  selected_hour = diff - Math.floor((timestamp - start_timestamp) / aday) * aday;");
    buffer.append(" }");
    buffer.newline();
    buffer.append("</script>");
  }

  public void fillViewBox(Request request, LocalizedStringBuffer buffer) {
    buffer.append("preserveAspectRatio='none' viewBox='0 0 " + WIDTH + " "+ HEIGHT + "'");
  }

}