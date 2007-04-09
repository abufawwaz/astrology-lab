package astrolab.project.election;

import astrolab.web.SVGDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayHourlyElectionaryChart extends SVGDisplay {

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("<text y='10'>DisplayHourlyElectionaryChart</text>");
  }

}