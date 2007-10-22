package astrolab.perspective.statistics;

import java.util.Properties;

import astrolab.formula.Formulae;
import astrolab.formula.display.ModifyFormulaeSetChartColor;
import astrolab.formula.display.ModifyFormulaeSetTime;
import astrolab.project.Projects;
import astrolab.web.Display;
import astrolab.web.Modify;
import astrolab.web.component.ComponentLink;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayTimeConfiguration extends DisplayAbstractConfiguration {

  public final static int DISPLAY_ID = Display.getId(DisplayTimeConfiguration.class);
  public final static int MODIFY_TIME_ID = Modify.getId(ModifyFormulaeSetTime.class);

  public DisplayTimeConfiguration() {
    super("Time", Display.getId(DisplayTimeDataChart.class));
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    super.fillBodyContent(request, buffer);

    // Chart series
    buffer.append("<table border='0' width='100%'>");

    IteratorProjectTimeData data = new IteratorProjectTimeData(Projects.getProject());
    Formulae[] series = data.getSeries();
    for (int i = 0; i < series.length && i < COLORS.length; i++) {
      buffer.append("<tr>");

      buffer.append("<td width='30'>");
      Properties parameters = new Properties();
      parameters.put("_m", String.valueOf(Modify.getId(ModifyFormulaeSetChartColor.class)));
      parameters.put(ModifyFormulaeSetChartColor.KEY, String.valueOf(series[i].getId()));
      parameters.put(ModifyFormulaeSetChartColor.IS_TO_SET, "false");
      ComponentLink.fillLink(buffer, parameters, COLORS[i]);
      buffer.append("</td>");

      buffer.append("<td>");
      series[i].fill(buffer);
      buffer.append("</td>");

      buffer.append("</tr>");
    }

    buffer.append("</table>");

	}

}