package astrolab.web.display;

import astrolab.web.HTMLDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public abstract class HTMLGroupDisplay extends HTMLDisplay {

  public final static String SELECTED_TAB = "_tab";

  private int[] displays;
  private HTMLDisplay selected;

  protected HTMLGroupDisplay(int[] displays) {
    this.displays = displays;
    this.selected = (HTMLDisplay) HTMLDisplay.getView(getSelectedDisplay());
  }

  public void fillActionScript(Request request, LocalizedStringBuffer buffer) {
    selected.fillActionScript(request, buffer);
  }

  public void fillDisplayContent(Request request, LocalizedStringBuffer buffer) {
    // draw tab bar
    buffer.append("<div class='class_title'>");
    for (int display: displays) {
      if (display == getSelectedDisplay()) {
        fillTabUrl(buffer, selected);
      } else {
        fillTabUrl(buffer, (HTMLDisplay) HTMLDisplay.getView(display));
      }
      buffer.append(" \\ ");
    }
    buffer.append("</div>");

    selected.fillDisplayContent(request, buffer);
  }

  protected void fillTabUrl(LocalizedStringBuffer buffer, HTMLDisplay display) {
    int id = HTMLDisplay.getId(display.getClass());

    if (id == getSelectedDisplay()) {
      buffer.append(display.getTitle());
    } else {
      buffer.append("<a href='tab.html?");
      buffer.append("_d=");
      buffer.append(HTMLGroupDisplay.getId(this.getClass()));
      buffer.append("&amp;");
      buffer.append(SELECTED_TAB);
      buffer.append("=");
      buffer.append(String.valueOf(id));
      buffer.append("'>");
      buffer.append(display.getTitle());
      buffer.append("</a>");
    }
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
  }

  private final int getSelectedDisplay() {
    int selectedDisplayId = Request.getCurrentRequest().getParameters().getInt(SELECTED_TAB);

    return (selectedDisplayId > 0) ? selectedDisplayId : displays[0];
  }
}