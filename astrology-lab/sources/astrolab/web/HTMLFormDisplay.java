package astrolab.web;

import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public abstract class HTMLFormDisplay extends HTMLDisplay {

  private int submitAction = -1;
  private int submitDisplay = -1;
  private int submitModify = -1;

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

  public void setSubmitAction(int action) {
    submitAction = action;
  }

  public void setSubmitDisplay(int display) {
    submitDisplay = display;
  }

  public void setSubmitModify(int modify) {
    submitModify = modify;
  }

  public void addSubmit(LocalizedStringBuffer buffer, String text) {
    buffer.append("<input type='submit' value='");
    buffer.localize(text);
    buffer.append("' />");
  }

  public void fillDisplayContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("<form method=\"post\" action=\"");
    fillUrl(buffer);
    buffer.append("\">");
    buffer.append("\r\n");
    fillBodyContent(request, buffer);
    buffer.append("\r\n</form>");
  }

  public abstract void fillBodyContent(Request request, LocalizedStringBuffer buffer);

  protected void fillUrl(LocalizedStringBuffer buffer) {
    buffer.append("post.html?");
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
    if (submitModify > 0) {
      buffer.append("&amp;");
      buffer.append("_m=");
      buffer.append(submitModify);
    }
  }
}