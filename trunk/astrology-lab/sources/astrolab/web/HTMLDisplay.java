package astrolab.web;

import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public abstract class HTMLDisplay extends Display {

  public final static String getExtension() {
    return "html";
  }

  public String getType() {
    return null;
  }

  public void fillContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("<html>");
    buffer.append("\r\n<head>");
    buffer.append("\r\n\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
    buffer.append("\r\n</head>");
    buffer.append("\r\n<body>");
    fillBodyContent(request, buffer);
    buffer.append("\r\n</body>");
    buffer.append("\r\n</html>");
  }

	public abstract void fillBodyContent(Request request, LocalizedStringBuffer buffer);

}