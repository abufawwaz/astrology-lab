package astrolab.web;

import java.io.UnsupportedEncodingException;

import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public abstract class HTMLDisplay extends Display {

  private String title = null;

  protected HTMLDisplay() {
  }

  protected HTMLDisplay(String title) {
    this.title = title;
  }

  public byte[] getContent() {
    LocalizedStringBuffer buffer = new LocalizedStringBuffer();

    fillContent(Request.getCurrentRequest(), buffer);

    try {
      return buffer.toString().getBytes("UTF-8");
    } catch (UnsupportedEncodingException uee) {
      return buffer.toString().getBytes();
    }
  }

  public final static String getExtension() {
    return "xhtml";
  }

  public String getType() {
    return "application/xhtml+xml";
  }

  public String getTitle() {
    return title;
  }

  public void fillContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:svg=\"http://www.w3.org/2000/svg\">");
    buffer.append("\r\n<head>");
    buffer.append("\r\n\t<meta http-equiv=\"Pragma\" content=\"no-cache\" />");
    buffer.append("\r\n\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
    buffer.append("\r\n\t<object id=\"AdobeSVG\" classid=\"clsid:78156a80-c6a1-4bbf-8e6a-3cd390eeb4e2\"></object>");
    buffer.append("\r\n\t<?import namespace=\"svg\" implementation=\"#AdobeSVG\"?>");
    buffer.append("\r\n</head>");

    buffer.append("\r\n<style type='text/css'>");
    buffer.append("\r\n.class_title { width:100%;  background: #DDDDFF; }");
    buffer.append("\r\n.class_input { width:100%; }");
    buffer.append("\r\n</style>");

    buffer.append("\r\n<body onload='if (top.manage_control) top.manage_control()'>");

    if (getTitle() != null) {
      buffer.append("<div class='class_title'>");
      buffer.localize(getTitle());
      buffer.append("</div>");
    }

    fillDisplayContent(request, buffer);

    fillActionScript(request, buffer);

    buffer.append("\r\n</body>");
    buffer.append("\r\n</html>");
  }

	public void fillDisplayContent(Request request, LocalizedStringBuffer buffer) {
    fillBodyContent(request, buffer);
  }

  public abstract void fillBodyContent(Request request, LocalizedStringBuffer buffer);

}