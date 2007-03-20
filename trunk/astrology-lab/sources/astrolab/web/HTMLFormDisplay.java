package astrolab.web;

import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public abstract class HTMLFormDisplay extends HTMLDisplay {

  private int submitAction = -1;
  private int submitDisplay = -1;

  protected HTMLFormDisplay(int submitAction) {
    this.submitAction = submitAction;
  }

  /**
   * @deprecated use constructor with title instead
   */
  protected HTMLFormDisplay(int submitDisplay, boolean isDisplay) {
    if (isDisplay) {
      this.submitDisplay = submitDisplay;
    } else {
      this.submitAction = submitDisplay;
    }
  }

  protected HTMLFormDisplay(String title, int submitDisplay, boolean isDisplay) {
    super(title);
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
    buffer.append("\r\n\t<object id=\"AdobeSVG\" classid=\"clsid:78156a80-c6a1-4bbf-8e6a-3cd390eeb4e2\"></object>");
    buffer.append("\r\n\t<?import namespace=\"svg\" implementation=\"#AdobeSVG\"?>");
    buffer.append("\r\n</head>");

    fillActionScript(request, buffer, false);

    buffer.append("\r\n<body style='background-color:transparent' onload='top.manage_control()'>");

    if (getTitle() != null) {
      buffer.append("<div class='class_title'>");
      buffer.localize(getTitle());
      buffer.append("</div>");
    }

    buffer.append("<form method=\"post\" action=\"post.html?");
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
    buffer.append("\r\n");
    fillBodyContent(request, buffer);
    buffer.append("\r\n</form>");
    buffer.append("\r\n</body>");

    // TODO get tge style sheet from the database or use dedicated CSS file
    buffer.append("<style type='text/css'>");
    buffer.append("\r\n.class_title { width:100%;  background: #DDDDFF; }");
    buffer.append("\r\n.class_input { width:100%; }");
    buffer.append("\r\n</style>");

    buffer.append("\r\n</html>");
  }

  public void addSubmit(LocalizedStringBuffer buffer, String text) {
    buffer.append("<input type='submit' value='");
    buffer.localize(text);
    buffer.append("' />");
  }

  public abstract void fillBodyContent(Request request, LocalizedStringBuffer buffer);

}