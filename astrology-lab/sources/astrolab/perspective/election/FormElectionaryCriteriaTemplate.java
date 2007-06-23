package astrolab.perspective.election;

import java.util.Properties;

import astrolab.criteria.Criteria;
import astrolab.db.Text;
import astrolab.web.Display;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.component.ComponentLink;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class FormElectionaryCriteriaTemplate extends HTMLFormDisplay {

  public final static int ID = Display.getId(FormElectionaryCriteriaTemplate.class);
  private final static String KEY_TEMPLATE = "_elect_template";

  public FormElectionaryCriteriaTemplate() {
    super("Template", ID, false);
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    int[] templates = Criteria.getTemplates();
    Properties parameters = new Properties();

    parameters.setProperty(KEY_TEMPLATE, "0");
    ComponentLink.fillLink(buffer, FormElectionaryCriteriaTemplate.class, parameters, "custom");
    buffer.append("<br />");

    for (int template: templates) {
      if (template != 0) {
        parameters.setProperty(KEY_TEMPLATE, String.valueOf(template));
        ComponentLink.fillLink(buffer, FormElectionaryCriteriaTemplate.class, parameters, Text.getText(template));
        buffer.append("<br />");
      }
    }

    if (request.get(KEY_TEMPLATE) != null) {
      Criteria.selectTemplate(Integer.parseInt(request.get(KEY_TEMPLATE)));
      fillRefreshScript(buffer);
    }
  }

  private final static void fillRefreshScript(LocalizedStringBuffer buffer) {
    buffer.newline();
    buffer.append("<script language='javascript'>");
    buffer.append("top.fireEvent(window, 'criteria', 'template')");
    buffer.append("</script>");
    buffer.newline();
  }

}