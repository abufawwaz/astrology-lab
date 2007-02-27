package astrolab.formula.display;

import java.util.Properties;

import astrolab.db.Action;
import astrolab.formula.FormulaIterator;
import astrolab.formula.Formulae;
import astrolab.formula.FormulaeSeries;
import astrolab.web.Display;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.Modify;
import astrolab.web.component.ComponentLink;
import astrolab.web.component.time.ComponentSelectTime;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayProjectFormula extends HTMLFormDisplay {

  public final static int DISPLAY_ID = Display.getId(DisplayProjectFormula.class);
  public final static int MODIFY_TIME_ID = Modify.getId(ModifyFormulaeSetTime.class);

  public DisplayProjectFormula() {
    super(Action.getAction(-1, DISPLAY_ID, MODIFY_TIME_ID));
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("<table>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("From");
    buffer.append("</td>");
    buffer.append("<td>");
    ComponentSelectTime.fill(buffer, FormulaIterator.getChartFromTime(), ModifyFormulaeSetTime.KEY_FROM_TIME, true);
    buffer.append("</td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("To");
    buffer.append("</td>");
    buffer.append("<td>");
    ComponentSelectTime.fill(buffer, FormulaIterator.getChartToTime(), ModifyFormulaeSetTime.KEY_TO_TIME, true);
    buffer.append("</td>");
    buffer.append("</tr>");
    buffer.append("</table>");
    buffer.append("<hr />");

    buffer.append("<hr />");

    Formulae base = FormulaIterator.getChartBase();
    buffer.append("<table border='1'>");
    buffer.append("<tr>");
    buffer.append("<th>");
    buffer.localize("f(x)");
    buffer.append("</th>");
    buffer.append("<th>");
    buffer.localize("Base");
    buffer.append("</th>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.append(base.getId());
    buffer.append("</td>");
    buffer.append("<td>");
    buffer.append(base.getText());
    buffer.append("</td>");
    buffer.append("</tr>");
    buffer.append("</table>");

    buffer.append("<hr />");

    Formulae period = FormulaIterator.getChartPeriod();
    buffer.append("<table border='1'>");
    buffer.append("<tr>");
    buffer.append("<th>");
    buffer.localize("f(x)");
    buffer.append("</th>");
    buffer.append("<th>");
    buffer.localize("Period");
    buffer.append("</th>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.append(period.getId());
    buffer.append("</td>");
    buffer.append("<td>");
    buffer.append(period.getText());
    buffer.append("</td>");
    buffer.append("</tr>");
    buffer.append("</table>");

    buffer.append("<hr />");

    buffer.append("<table border='1'>");
    buffer.append("<tr>");
    buffer.append("<th>");
    buffer.localize("f(x)");
    buffer.append("</th>");
    buffer.append("<th>");
    buffer.localize("Color");
    buffer.append("</th>");
    buffer.append("<th>");
    buffer.localize("Formulae");
    buffer.append("</th>");
    buffer.append("<th>");
    buffer.localize("Score");
    buffer.append("</th>");
    buffer.append("</tr>");

    FormulaeSeries[] series = FormulaIterator.getChartSeries();
    for (int i = 0; i < series.length; i++) {
      buffer.append("<tr>");
      buffer.append("<td>");
      buffer.append(series[i].getId());
      buffer.append("</td>");

      buffer.append("<td>");
      Properties parameters = new Properties();
      parameters.put("_m", String.valueOf(Modify.getId(ModifyFormulaeSetChartColor.class)));
      parameters.put(ModifyFormulaeSetChartColor.KEY, String.valueOf(series[i].getId()));
      parameters.put(ModifyFormulaeSetChartColor.IS_TO_SET, String.valueOf(series[i].getColor() == null));
      ComponentLink.fillLink(buffer, request.getDisplay().getClass(), parameters, (series[i].getColor() != null) ? series[i].getColor() : "white");
      buffer.append("</td>");

      buffer.append("<td>");
      buffer.append(series[i].getText());
      buffer.append("</td>");
      buffer.append("<td>");
      buffer.append(series[i].getScore());
      buffer.append("</td>");

      buffer.append("</tr>");
    }
    buffer.append("</table>");
	}

}
