package astrolab.web.component.general;

import astrolab.db.Text;
import astrolab.web.HTMLDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class PageNotImplemented extends HTMLDisplay {

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("<center>");
    buffer.append(Text.getText("menu.not-implemented"));
    buffer.append("</center>");
	}

}
