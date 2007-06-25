package astrolab.perspective.election;

import java.util.Properties;

import astrolab.astronom.SpacetimeEvent;
import astrolab.criteria.Criteria;
import astrolab.criteria.Criterion;
import astrolab.db.Text;
import astrolab.web.Display;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.component.ComponentLink;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayElectionaryCriteriaList extends HTMLFormDisplay {

  public final static int ID = Display.getId(DisplayElectionaryCriteriaList.class);
  public final static String PARAMETER_ACTION = "_elective_criteria_action";
  public final static String PARAMETER_TARGET = "_elective_criteria_target";
  public final static String ACTION_DELETE = "delete";
  public final static String ACTION_COLOR = "color";
  public final static String ACTION_MULTIPLY_INCREASE = "multiply_increase";
  public final static String ACTION_MULTIPLY_DECREASE = "multiply_decrease";
  public final static String ACTION_START_TIME = "time";

  public DisplayElectionaryCriteriaList() {
    super(getTemplateName(), ID, true);
    super.addAction("criteria", "action");
  }

  private final static String getTemplateName() {
    int template = Criteria.getSelectedTemplate();
    return (template > 0) ? Text.getText(template) : "Criteria";
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    checkActionRequest(request, buffer);

    Properties parameters;
    Criterion[] criteria = Criteria.getCriteria();
    boolean modifyable = Criteria.getSelectedTemplate() == 0;

    fillTimestart(buffer);

    buffer.append("<table>");
    for (int i = 0; i < criteria.length; i++) {
      buffer.append("<tr>");

      buffer.append("<td>");
      if (modifyable) {
        parameters = new Properties();
        parameters.put(PARAMETER_ACTION, ACTION_COLOR);
        parameters.put(PARAMETER_TARGET, String.valueOf(criteria[i].getId()));
        ComponentLink.fillLink(buffer, request.getDisplay().getClass(), parameters, criteria[i].getColor());
      } else {
        buffer.localize(criteria[i].getColor());
      }
      buffer.append("</td>");

      buffer.append("<td>");
      criteria[i].toString(buffer);
      buffer.append("</td>");

      buffer.append("<td>");
      buffer.append("x");
      buffer.append(criteria[i].getMultiplyBy());
      buffer.append("</td>");

      if (modifyable) {
        buffer.append("<td>");
        parameters = new Properties();
        parameters.put(PARAMETER_ACTION, ACTION_MULTIPLY_INCREASE);
        parameters.put(PARAMETER_TARGET, String.valueOf(criteria[i].getId()));
        ComponentLink.fillLink(buffer, request.getDisplay().getClass(), parameters, "+");
        buffer.append("</td>");
  
        buffer.append("<td>");
        parameters = new Properties();
        parameters.put(PARAMETER_ACTION, ACTION_MULTIPLY_DECREASE);
        parameters.put(PARAMETER_TARGET, String.valueOf(criteria[i].getId()));
        ComponentLink.fillLink(buffer, request.getDisplay().getClass(), parameters, "-");
        buffer.append("</td>");
  
        buffer.append("<td>");
        parameters = new Properties();
        parameters.put(PARAMETER_ACTION, ACTION_DELETE);
        parameters.put(PARAMETER_TARGET, String.valueOf(criteria[i].getId()));
        ComponentLink.fillLink(buffer, request.getDisplay().getClass(), parameters, "X");
        buffer.append("</td>");
      }

      buffer.append("</tr>");
    }
    buffer.append("</table>");
  }

  private final static void checkActionRequest(Request request, LocalizedStringBuffer buffer) {
    String action = request.get(PARAMETER_ACTION);
    String criterion = request.get(PARAMETER_TARGET);

    if (ACTION_DELETE.equals(action)) {
      getCriterion(criterion).delete();
      fillRefreshScript(buffer);
    } else if (ACTION_COLOR.equals(action)) {
      getCriterion(criterion).changeColor();
      fillRefreshScript(buffer);
    } else if (ACTION_MULTIPLY_INCREASE.equals(action)) {
      getCriterion(criterion).changeMultiply(true);
      fillRefreshScript(buffer);
    } else if (ACTION_MULTIPLY_DECREASE.equals(action)) {
      getCriterion(criterion).changeMultiply(false);
      fillRefreshScript(buffer);
    } else if (ACTION_START_TIME.equals(action)) {
      ElectiveEnvironment.moveStartingTime(Integer.parseInt(criterion));
      fillRefreshScript(buffer);
    }
  }

  private final static void fillTimestart(LocalizedStringBuffer buffer) {
    SpacetimeEvent selection = ElectiveEnvironment.getStartingTime();
    Properties parameters = new Properties();

    buffer.newline();

    parameters.put(PARAMETER_ACTION, ACTION_START_TIME);
    parameters.put(PARAMETER_TARGET, "-1");
    ComponentLink.fillLink(buffer, DisplayElectionaryCriteriaList.class, parameters, "&lt;&lt;");

    buffer.append(" ");
    buffer.localize(DisplayDailyElectionaryChart.MONTHS[selection.get(SpacetimeEvent.MONTH)]);
    buffer.append(" ");
    buffer.append(selection.get(SpacetimeEvent.YEAR));
    buffer.append(" ");

    parameters.put(PARAMETER_ACTION, ACTION_START_TIME);
    parameters.put(PARAMETER_TARGET, "1");
    ComponentLink.fillLink(buffer, DisplayElectionaryCriteriaList.class, parameters, "&gt;&gt;");

    buffer.newline();
  }

  private final static void fillRefreshScript(LocalizedStringBuffer buffer) {
    buffer.newline();
    buffer.append("<script language='javascript'>");
    buffer.append("top.fireEvent(window, 'criteria', 'refresh')");
    buffer.append("</script>");
    buffer.newline();
  }

  private final static Criterion getCriterion(String textId) {
    int deleteId = Integer.parseInt(textId);
    Criterion[] criteria = Criteria.getCriteria();
    for (int i = 0; i < criteria.length; i++) {
      if (criteria[i].getId() == deleteId) {
        return criteria[i];
      }
    }
    return null;
  }

}