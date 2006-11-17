package astrolab.project.sleep;

import astrolab.web.HTMLDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplaySleepRecords extends HTMLDisplay {

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    buffer.localize("Ok");

	}

}
