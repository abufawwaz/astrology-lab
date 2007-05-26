package astrolab.perspective.election;

import java.util.Calendar;

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

  private int DAYS = 31;
  private Calendar timestamp;

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
      timestamp = Calendar.getInstance();
    }
    timestamp.set(Calendar.HOUR, 0);
    timestamp.set(Calendar.MINUTE, 0);
    timestamp.set(Calendar.SECOND, 0);
    DAYS = timestamp.getActualMaximum(Calendar.DAY_OF_MONTH);
  }

  private final Calendar getCalendar(int day) {
    timestamp.set(Calendar.DAY_OF_MONTH, day + 1);
    return timestamp;
  }

  private final void fillMarks(LocalizedStringBuffer buffer, Criterion[] criteria) {
    int[][] marks = new int[criteria.length][DAYS];
    int maxValue = 0;

    for (int d = 0; d < DAYS; d ++) {
      Calendar calendar = getCalendar(d);

      for (int c = 0; c < criteria.length; c++) {
        marks[c][d] = criteria[c].getMark(calendar) + ((c > 0) ? marks[c - 1][d] : 0);
      }

      if (marks[criteria.length - 1][d] > maxValue) {
        maxValue = marks[criteria.length - 1][d];
      }
    }
    if (maxValue == 0) {
      maxValue = HEIGHT;
    }

    for (int c = criteria.length - 1; c >= 0; c--) {
      buffer.append("<polygon points='");
      buffer.append(String.valueOf(DAYS));
      buffer.append(",0 0,0");

      for (int d = 0; d < DAYS; d ++) {
        buffer.append(" ");
        buffer.append(String.valueOf(d));
        buffer.append(",");
        buffer.append(String.valueOf(((float) HEIGHT * marks[c][d]) / maxValue));
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
    buffer.append(" var start_timestamp = " + getCalendar(0).getTimeInMillis());
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