package astrolab.perspective.election;

import java.util.ArrayList;
import java.util.Hashtable;

import astrolab.criteria.Criterion;
import astrolab.web.HTMLDisplay;
import astrolab.web.component.ComponentController;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayElectionaryCriteriaComponents extends HTMLDisplay {

  public DisplayElectionaryCriteriaComponents() {
    super("Components");
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("<table border='0' width='100%' cellspacing='0' cellpadding='0'>");
    Hashtable<String, ArrayList<Integer>> types = Criterion.TYPES;
    for (String type: types.keySet()) {
      buffer.append("<tr>");
      buffer.append("<td colspan='6'>");
      buffer.append("<font size='-2'><i><b>");
      buffer.localize(type);
      buffer.append("</b></i></font>");
      buffer.append("</td>");
      buffer.append("</tr>");

      ArrayList<Integer> typeList = types.get(type);
      buffer.append("<tr>");
      for (int i = 0; i < typeList.size(); i++) {
        if ((i > 0) && (i % 6 == 0)) {
          buffer.append("</tr><tr>");
        }
        int id = typeList.get(i);
        String controlId = "controller_" + type + "_" + id; 
        ComponentController.fill(buffer, controlId, type, String.valueOf(id));
        buffer.append("<td id='");
        buffer.append(controlId);
        buffer.append("'>");
        buffer.localize(id);
        buffer.append("</td>");
      }
      buffer.append("</tr>");
    }
    buffer.append("</table>");
  }

}