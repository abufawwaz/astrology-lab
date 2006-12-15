package astrolab.web;

import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public abstract class HTMLFormDisplay extends HTMLDisplay {

  private int submitAction = -1;
  private int submitDisplay = -1;

  protected HTMLFormDisplay(int submitAction) {
    this.submitAction = submitAction;
  }

  protected HTMLFormDisplay(int submitDisplay, boolean isDisplay) {
    if (isDisplay) {
      this.submitDisplay = submitDisplay;
    } else {
      this.submitAction = submitDisplay;
    }
  }

  public void fillContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:svg=\"http://www.w3.org/2000/svg\">");
    buffer.append("\r\n<head>");
    buffer.append("\r\n\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
    buffer.append("\r\n</head>");

    if ("1".equals(request.getParameters().get("_reload", "1"))) {
      buffer.append("\r\n<body style='background-color:transparent' onload='top.refreshAllPanes(this)'>");
    } else {
      buffer.append("\r\n<body style='background-color:transparent'>");
    }
    buffer.append("\r\n<object id=\"AdobeSVG\" classid=\"clsid:78156a80-c6a1-4bbf-8e6a-3cd390eeb4e2\"></object>");
    buffer.append("\r\n<?import namespace=\"svg\" implementation=\"#AdobeSVG\"?>");
    buffer.append("\r\n<form method=\"post\" action=\"post.html?");
    buffer.append("_rd=");
    buffer.append(HTMLFormDisplay.getId(this.getClass()));
    buffer.append("&amp;");
    if (submitAction >= 0) {
      buffer.append("_a=");
      buffer.append(submitAction);
    } else {
      buffer.append("_d=");
      buffer.append(submitDisplay);
    }
    buffer.append("\">");
    fillBodyContent(request, buffer);
    buffer.append("\r\n</form>");
    buffer.append("\r\n</body>");
    buffer.append("\r\n</html>");
  }

	public abstract void fillBodyContent(Request request, LocalizedStringBuffer buffer);

}