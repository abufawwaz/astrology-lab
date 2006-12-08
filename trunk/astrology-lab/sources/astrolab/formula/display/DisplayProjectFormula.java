package astrolab.formula.display;

import astrolab.db.Action;
import astrolab.formula.FormulaIterator;
import astrolab.formula.Formulae;
import astrolab.web.Display;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayProjectFormula extends HTMLFormDisplay {

	public final static int ID = Display.getId(DisplayProjectFormula.class);

  public DisplayProjectFormula() {
    super(Action.getAction(-1, ID, -1));
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    buffer.localize("You are allowed 10 slots for custom formula:");
    buffer.append("<br />");

    int slot = 1;
    FormulaIterator iterator = FormulaIterator.iterateOwn();
    buffer.append("<table border='1'>");
    buffer.append("<tr>");
    buffer.append("<th>");
    buffer.localize("Slot");
    buffer.append("</th>");
    buffer.append("<th>");
    buffer.localize("Formulae");
    buffer.append("</th>");
    buffer.append("<th>");
    buffer.localize("Score");
    buffer.append("</th>");
    buffer.append("</tr>");
    while (iterator.hasNext()) {
      Formulae f = (Formulae) iterator.next();
      buffer.append("<tr>");
      buffer.append("<td>");
      buffer.append(slot);
      buffer.append("</td>");
      buffer.append("<td>");
      buffer.append(f.toString());
      buffer.append("</td>");
      buffer.append("<td>");
      buffer.append(f.getScore());
      buffer.append("</td>");
      buffer.append("</tr>");
      slot++;
    }
    buffer.append("</table>");

    buffer.append("<hr />");
    buffer.localize("These are the best formula:");
    buffer.append("<br />");

    iterator = FormulaIterator.iterateBest();
    buffer.append("<table border='1'>");
    buffer.append("<tr>");
    buffer.append("<th>");
    buffer.localize("Owner");
    buffer.append("</th>");
    buffer.append("<th>");
    buffer.localize("Formulae");
    buffer.append("</th>");
    buffer.append("<th>");
    buffer.localize("Score");
    buffer.append("</th>");
    buffer.append("</tr>");
    while (iterator.hasNext()) {
      Formulae f = (Formulae) iterator.next();
      buffer.append("<tr>");
      buffer.append("<td>");
      buffer.localize(f.getOwner());
      buffer.append("</td>");
      buffer.append("<td>");
      buffer.append(f.toString());
      buffer.append("</td>");
      buffer.append("<td>");
      buffer.append(f.getScore());
      buffer.append("</td>");
      buffer.append("</tr>");
      slot++;
    }
    buffer.append("</table>");
	}

}
