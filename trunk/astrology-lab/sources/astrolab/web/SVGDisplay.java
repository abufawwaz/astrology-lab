package astrolab.web;

import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public abstract class SVGDisplay extends Display {

  public SVGDisplay() {
    super.addAction("event", "user.session.event.1");
  }

  public final static String getExtension() {
    return "svg";
  }

  public String getType() {
    return "image/svg+xml";
  }

  public void fillContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("<?xml version=\"1.0\" standalone=\"yes\" ?>\r\n");
    buffer.append("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\r\n");
    buffer.append("<svg onLoad='top.manage_control()' version=\"1.1\" width=\"100%\" height=\"100%\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" ");
    buffer.append(">\r\n");
    fillActionScript(request, buffer);
    buffer.append("<g style=\"font-size:10pt\">\r\n");
    fillBodyContent(request, buffer);
    buffer.append("</g>\r\n");
    buffer.append("</svg>");
  }

  public abstract void fillBodyContent(Request request, LocalizedStringBuffer buffer);

  public void fillViewBox(Request request, LocalizedStringBuffer buffer) {
    buffer.append("viewBox='-5 0 100 100' preserveAspectRatio='xMinYMin meet'");
  }

}