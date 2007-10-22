package astrolab.web;

import java.io.UnsupportedEncodingException;

import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public abstract class SVGDisplay extends Display {

  private double x;
  private double y;
  private double width;
  private double height;
  private String aspectRatio;

  public SVGDisplay() {
    this(-5, 0, 100, 100, "xMinYMin meet");
  }

  public SVGDisplay(double x, double y, double width, double height, String aspectRatio) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.aspectRatio = aspectRatio;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getWidth() {
    return width;
  }

  public double getHeight() {
    return height;
  }

  public final static String getExtension() {
    return "svg";
  }

  public String getType() {
    return "image/svg+xml";
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

  public void fillContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("<?xml version=\"1.0\" standalone=\"yes\" ?>"); buffer.newline();
    buffer.append("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">"); buffer.newline();
    buffer.append("<svg onLoad='top.manage_control()' version=\"1.1\" width=\"100%\" height=\"100%\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" ");
    fillViewBox(request, buffer);
    buffer.append(">"); buffer.newline();
    buffer.append("<g style=\"font-size:10pt\">"); buffer.newline();
    fillBodyContent(request, buffer);
    buffer.append("</g>"); buffer.newline();
    fillActionScript(request, buffer); buffer.newline();
    buffer.append("</svg>");
  }

  public abstract void fillBodyContent(Request request, LocalizedStringBuffer buffer);

  public void fillViewBox(Request request, LocalizedStringBuffer buffer) {
    buffer.append("viewBox='");
    buffer.append(String.valueOf(x));
    buffer.append(" ");
    buffer.append(String.valueOf(y));
    buffer.append(" ");
    buffer.append(String.valueOf(width));
    buffer.append(" ");
    buffer.append(String.valueOf(height));
    buffer.append("' preserveAspectRatio='");
    buffer.append(aspectRatio);
    buffer.append("'");
  }

}