package astrolab.project.match;

import astrolab.db.Event;
import astrolab.web.HTMLDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class GeneralPowerView extends HTMLDisplay {

  public GeneralPowerView() {
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    Factor[] factors = FactorsFactory.getFactors(Event.getSelectedEvent()).toArray();

    buffer.append("<table>");
    buffer.append("<tr><td>Factor</td><td>Power</td></tr>");
    for (int i = 0; i < factors.length; i++) {
      buffer.append("<tr><td>");
      buffer.append(factors[i].getName());
      buffer.append("</td><td>");
      buffer.append(String.valueOf(factors[i].getGeneralInfluence()));
      buffer.append("</td></tr>");
    }
    buffer.append("</table>");
  }
}