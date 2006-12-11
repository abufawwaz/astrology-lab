package astrolab.web;

import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public abstract class HTMLDisplay extends Display {

  public final static String getExtension() {
    return "xhtml";
  }

  public String getType() {
    return "application/xhtml+xml";
  }

  public void fillContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:svg=\"http://www.w3.org/2000/svg\">");
    buffer.append("\r\n<head>");
    buffer.append("\r\n\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
    buffer.append("\r\n</head>");
    if (Request.hasDatabaseChange()) {
      buffer.append("\r\n<body onload='top.refreshAllPanes(this)'>");
    } else {
      buffer.append("\r\n<body>");
    }
    fillBodyContent(request, buffer);
    buffer.append("\r\n</body>");
    buffer.append("\r\n</html>");
  }

	public abstract void fillBodyContent(Request request, LocalizedStringBuffer buffer);

}