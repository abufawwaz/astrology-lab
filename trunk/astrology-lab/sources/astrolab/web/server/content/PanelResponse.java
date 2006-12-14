package astrolab.web.server.content;

import java.io.UnsupportedEncodingException;

import astrolab.web.server.Request;
import astrolab.web.server.Response;

public class PanelResponse implements Response {

	private Request request;
  private String response;

	public PanelResponse(Request request) {
		this.request = request;
	}

	public String getType() {
		String accepted = request.getParameters().get("Accept");
		if (accepted != null && "application/xhtml+xml".equals(request.getDisplay().getType()) && accepted.indexOf("application/xhtml+xml") < 0) {
  		return "text/html";
		} else {
			return request.getDisplay().getType();
		}
	}

	public byte[] getBytes() {
    LocalizedStringBuffer buffer = new LocalizedStringBuffer();

    if (request.getModify() != null) {
      request.getModify().operate(request);
    }
    request.getDisplay().fillContent(request, buffer);

    try {
      return buffer.toString().getBytes("UTF-8");
    } catch (UnsupportedEncodingException uee) {
      return buffer.toString().getBytes();
    }
	}

	public String toString() {
		return response;
	}

}
