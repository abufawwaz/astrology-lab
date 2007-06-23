package astrolab.perspective.election;

import astrolab.criteria.TypeCriterion;
import astrolab.web.Display;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.component.ComponentControllable;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayElectionaryRestrictionList extends HTMLFormDisplay {

  public final static int ID = Display.getId(DisplayElectionaryRestrictionList.class);

  private final static String CRITERIA_PREFIX = "_cr";

  public DisplayElectionaryRestrictionList() {
    super("New Criteria", ID, true);
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    String selection = "";
    String selectedType = request.get(CRITERIA_PREFIX + ".T");
    String[] selectedActors = getSelection(request, buffer, selectedType);
    String[][] criteria;

    if (selectedActors != null) {
      criteria = TypeCriterion.getCriteria(selectedType, selectedActors);
    } else {
      selectedActors = new String[10];
      criteria = TypeCriterion.getCriteria(null, selectedActors);
    }

    for (int i = 0; i < selectedActors.length; i++) {
      if (selectedActors[i] != null) {
        selection += "&amp;" + CRITERIA_PREFIX + "." + i + "=" + selectedActors[i];
      }
    }

    boolean spanBorder;
    int maxSpan = Integer.MAX_VALUE;
    buffer.append("<table border='1'>");
    for (int col = 0; col < criteria.length; col++) {
      buffer.append("<tr>");
      spanBorder = false;
      maxSpan = Integer.MAX_VALUE;
      for (int row = 1; row < criteria[col].length; row++) {
//        if ((col == 0) || spanBorder || (!criteria[col][row].equals(criteria[col - 1][row]))) {
          spanBorder = true;
          int span = 1;
          while ((span < maxSpan) && (col + span < criteria.length) && criteria[col][row].equals(criteria[col + span][row])) span++;
          maxSpan = span; // next column cannot span more than this one

//          if (span == 1) {
            buffer.append("<td>");
//          } else {
//            buffer.append("<td rowspan='");
//            buffer.append(span);
//            buffer.append("'>");
//          }
          if (selectedActors[row - 1] != null) {
            buffer.localize(Integer.parseInt(selectedActors[row - 1]));
          } else if (criteria[col][row].startsWith("'")) {
            buffer.append(criteria[col][row]);
          } else {
            String controlId = CRITERIA_PREFIX + "." + col + "." + row;
            String function = "function(new_option) { window.location.href='new_option.html?"
              + "_d=" + ID
              + "&amp;" + CRITERIA_PREFIX + ".T=" + criteria[col][0]
              + selection
              + "&amp;" + CRITERIA_PREFIX + "." + (row - 1) + "=' + new_option"
              + "}";

            ComponentControllable.fill(buffer, controlId, criteria[col][row], function);
            buffer.append("<div id='");
            buffer.append(controlId);
            buffer.append("' width='100%'><font color='gray'><i>");
            buffer.localize(criteria[col][row]);
            buffer.append("</i></font></div>");
          }
          buffer.append("</td>");
//        }
      }
      buffer.append("</tr>");
    }
    buffer.append("</table>");
  }

  private final static String[] getSelection(Request request, LocalizedStringBuffer buffer, String selectedType) {
    String[] selected = new String[10];

    for (int i = 0; i < 10; i++) {
      selected[i] = request.get(CRITERIA_PREFIX + "." + i);
    }

    // if criterion is fulfilled then store it and refresh the view
    if (TypeCriterion.isCriteriaDetermined(selectedType, selected)) {
      TypeCriterion.store(selectedType, selected);
      fillRefreshScript(buffer);
      return null;
    } else {
      return selected;
    }
  }

  private final static void fillRefreshScript(LocalizedStringBuffer buffer) {
    buffer.newline();
    buffer.append("<script language='javascript'>");
    buffer.append("top.fireEvent(window, 'criteria', 'refresh')");
    buffer.append("</script>");
    buffer.newline();
  }

}