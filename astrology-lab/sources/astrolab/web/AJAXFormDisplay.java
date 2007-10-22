package astrolab.web;

import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public abstract class AJAXFormDisplay extends HTMLDisplay {

  private int modifyAction = -1;

  protected AJAXFormDisplay(String title, int modifyAction) {
    super(title);
    this.modifyAction = modifyAction;
  }

  public void fillContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:svg=\"http://www.w3.org/2000/svg\">");
    buffer.append("\r\n<head>");
    buffer.append("\r\n\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
    buffer.append("\r\n\t<object id=\"AdobeSVG\" classid=\"clsid:78156a80-c6a1-4bbf-8e6a-3cd390eeb4e2\"></object>");
    buffer.append("\r\n\t<?import namespace=\"svg\" implementation=\"#AdobeSVG\"?>");
    buffer.append("\r\n</head>");

    fillActionScript(request, buffer);

    buffer.newline();
    buffer.append("<body style='background-color:transparent' onload='top.manage_control()'>");
    buffer.newline();
    buffer.append("<form>");
    buffer.newline();

    if (getTitle() != null) {
      buffer.append("<div class='class_title'>");
      buffer.localize(getTitle());
      buffer.append("</div>");
    }

    buffer.newline();
    fillBodyContent(request, buffer);
    buffer.newline();
    buffer.append("</form>");
    buffer.append("</body>");

    // TODO get tge style sheet from the database or use dedicated CSS file
    buffer.append("<style type='text/css'>");
    buffer.append("\r\n.class_title { width:100%;  background: #DDDDFF; }");
    buffer.append("\r\n.class_input { width:100%; }");
    buffer.append("\r\n</style>");

    buffer.append("\r\n</html>");
  }

  protected void addSubmit(LocalizedStringBuffer buffer, String text, String function) {
    buffer.append("<input type='button' name='submit' value='");
    buffer.localize(text);
    buffer.append("' onclick='top.postAjaxRequest(\"");
    fillUrl(buffer);
    buffer.append("\", this.form, function(ajax) {");
    buffer.append(function);
    buffer.append("})' />");
  }

  public abstract void fillBodyContent(Request request, LocalizedStringBuffer buffer);

  protected void fillUrl(LocalizedStringBuffer buffer) {
    buffer.append("ajax.html?_d=0&amp;_m=");
    buffer.append(modifyAction);
  }
}