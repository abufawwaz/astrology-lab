package astrolab.perspective.statistics;

import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayNoDataChart extends DisplayDataChart {

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("<text y='50'>");
    buffer.localize("No data");
    buffer.append("</text>");
  }

}
