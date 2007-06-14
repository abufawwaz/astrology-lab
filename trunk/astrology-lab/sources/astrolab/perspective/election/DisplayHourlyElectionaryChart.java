package astrolab.perspective.election;

import java.util.Calendar;

import astrolab.criteria.Criteria;
import astrolab.criteria.Criterion;
import astrolab.web.SVGDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayHourlyElectionaryChart extends SVGDisplay {

  private final static int HEIGHT = 10;
  private final static float MINUTE_STEP = ((float) 15) / 60;

  private Calendar timestamp;

  public DisplayHourlyElectionaryChart() {
    super.addAction("timestamp", "timestamp");
//    super.addAction("timestamp", "javascript:updateTime(message)");
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
    buffer.append("  top.fireEvent(window, 'timestamp', Math.floor(start_timestamp + selected_hour * an_hour ));");
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
    buffer.append("  var hour = 24 * evt.clientX / document.rootElement.width.baseVal.value;");
    buffer.append("  var text = document.createTextNode('' + Math.floor(hour) + ':' + Math.floor((hour - Math.floor(hour)) * 60));");
    buffer.append("  document.getElementById(mark + '_id').setAttribute('transform', 'translate(' + (24 * evt.clientX / document.rootElement.width.baseVal.value) + ' 0)');");
    buffer.append("  var text_node = document.getElementById(mark + '_text');");
    buffer.append("  while (text_node.hasChildNodes()) text_node.removeChild(text_node.firstChild);");
    buffer.append("  text_node.appendChild(text);");
    buffer.append(" }");
    buffer.newline();
    buffer.append(" var start_timestamp = " + getCalendar(0).getTimeInMillis());
    buffer.newline();
//    buffer.append(" var selected_day = 0");
//    buffer.newline();
    buffer.append(" var an_hour = 60 * 60 * 1000");
    buffer.newline();
//    buffer.append(" var selected_hour = 0");
//    buffer.newline();
//    buffer.append(" function updateTime(timestamp) {");
//    buffer.append("  selected_day = timestamp - start_timestamp - selected_hour * an_hour; alert(new Date(start_timestamp + selected_day))");
//    buffer.append(" }");
//    buffer.newline();
    buffer.append("</script>");
  }

  private final void determineStartingTime(Request request, Criterion[] criteria) {
    String timeParameter = request.get("timestamp");
    if (timeParameter != null) {
      timestamp = Calendar.getInstance();
      timestamp.setTimeInMillis(Long.parseLong(timeParameter));
    }
    if (timestamp == null) {
      timestamp = Calendar.getInstance();
    }
    timestamp.set(Calendar.HOUR_OF_DAY, 0);
    timestamp.set(Calendar.MINUTE, 0);
    timestamp.set(Calendar.SECOND, 0);
  }

  private final void fillMarks(LocalizedStringBuffer buffer, Criterion[] criteria) {
    int[][] marks = new int[criteria.length][(int) (24 / MINUTE_STEP)];
    int maxValue = 0;
    Calendar periodStart;
    Calendar periodEnd = getCalendar(0);

    for (int h = 0; h < 24 / MINUTE_STEP; h++) {
      periodStart = periodEnd;
      periodEnd = getCalendar(MINUTE_STEP * (h + 1));

      for (int c = 0; c < criteria.length; c++) {
        marks[c][h] = criteria[c].getMark(periodStart, periodEnd) + ((c > 0) ? marks[c - 1][h] : 0);
      }

      if ((criteria.length > 0) && marks[criteria.length - 1][h] > maxValue) {
        maxValue = marks[criteria.length - 1][h];
      }
    }
    maxValue++;

    for (int c = criteria.length - 1; c >= 0; c--) {
      buffer.append("<polygon points='");
      buffer.append(String.valueOf(24));
      buffer.append(",0 0,0");

      for (int h = 0; h < 24 / MINUTE_STEP; h ++) {
        buffer.append(" ");
        buffer.append(String.valueOf(MINUTE_STEP * h));
        buffer.append(",");
        buffer.append(String.valueOf(((float) HEIGHT * marks[c][h]) / maxValue));
      }

      buffer.append("' style='stroke:none;fill:");
      buffer.append(criteria[c].getColor());
      buffer.append("' />");
    }
  }

  private final Calendar getCalendar(float hour) {
    Calendar result = Calendar.getInstance();
    result.setTime(timestamp.getTime());
    result.set(Calendar.HOUR_OF_DAY, (int) hour);
    result.set(Calendar.MINUTE, (int) ((hour - (int) hour) * 60));
    return result;
  }

  public void fillViewBox(Request request, LocalizedStringBuffer buffer) {
    buffer.append("preserveAspectRatio='none' viewBox='0 0 24 "+ HEIGHT + "'");
  }

}