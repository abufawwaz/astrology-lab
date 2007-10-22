package astrolab.perspective.statistics;

import astrolab.db.Action;
import astrolab.formula.FormulaIterator;
import astrolab.formula.display.ModifyFormulae;
import astrolab.formula.display.ModifyFormulaeSetTime;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.Modify;
import astrolab.web.component.time.ComponentSelectTime;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

abstract class DisplayAbstractConfiguration extends HTMLFormDisplay {

  public final static int MODIFY_TIME_ID = Modify.getId(ModifyFormulaeSetTime.class);
  protected final static String[] COLORS = { "black", "red", "orange", "yellow", "green", "blue", "indigo"  };

  private int id;

  public DisplayAbstractConfiguration(String title, int id) {
    super(title, Action.getAction(-1, -1, Modify.getId(ModifyFormulae.class)), false);
//    super(Action.getAction(-1, DISPLAY_ID, MODIFY_TIME_ID));

    this.id = id;
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    addScript(buffer);

//    buffer.append("<table>");
//    buffer.append("<tr>");
//    buffer.append("<td>");
//    buffer.localize("From");
//    buffer.append("</td>");
//    buffer.append("<td>");
//    ComponentSelectTime.fill(buffer, FormulaIterator.getChartFromTime(), ModifyFormulaeSetTime.KEY_FROM_TIME, true);
//    buffer.append("</td>");
//    buffer.append("</tr>");
//    buffer.append("<tr>");
//    buffer.append("<td>");
//    buffer.localize("To");
//    buffer.append("</td>");
//    buffer.append("<td>");
//    ComponentSelectTime.fill(buffer, FormulaIterator.getChartToTime(), ModifyFormulaeSetTime.KEY_TO_TIME, true);
//    buffer.append("</td>");
//    buffer.append("</tr>");
//    buffer.append("</table>");
//    buffer.append("Time selection temporary disabled");
//    buffer.append("<hr />");
	}

  private final void addScript(LocalizedStringBuffer buffer) {
    buffer.newline();
    buffer.append("<script language='javascript'>");
    buffer.append("top.fireEvent(window, '_chart_type', '");
    buffer.append(String.valueOf(id));
    buffer.append("')");
    buffer.append("</script>");
    buffer.newline();
  }

}