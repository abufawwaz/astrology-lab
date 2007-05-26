package astrolab.perspective.election;

import astrolab.web.HTMLFormDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class FormElectionaryCriteriaTemplate extends HTMLFormDisplay {

  public FormElectionaryCriteriaTemplate() {
    super("Template", 0, false);
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("<a href=''>");
    buffer.localize("Good times");
    buffer.append("</a>");
    buffer.append("<br />");
    buffer.append("<a href=''>");
    buffer.localize("Hard work");
    buffer.append("</a>");
    buffer.append("<br />");
    buffer.append("<a href=''>");
    buffer.localize("Attend examination");
    buffer.append("</a>");
    buffer.append("<br />");
  }

}