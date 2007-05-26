package astrolab.perspective.election;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.Time;
import astrolab.criteria.Criteria;
import astrolab.criteria.Criterion;
import astrolab.criteria.CriterionStartTime;
import astrolab.criteria.CriterionZodiacSign;
import astrolab.criteria.TypeCriterion;
import astrolab.web.Display;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.component.ComponentControllable;
import astrolab.web.component.time.ComponentSelectTime;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayElectionaryRestrictionList extends HTMLFormDisplay {

  public final static int ID = Display.getId(DisplayElectionaryRestrictionList.class);

  private final static String CRITERIA_PREFIX = "criteria_";

  private final static String NEW_OPTIONAL_CRITERIA = "new_optional_criteria";
  private final static String NEW_OPTIONAL_CRITERIA_SIGN = "new_optional_criteria_sign";

  private boolean fireChange = false;

  public DisplayElectionaryRestrictionList() {
    super("Criteria", ID, true);
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    Time startTime = ComponentSelectTime.retrieve(request, CriterionStartTime.REQUEST_PARAMETER);
    String newOptionalCriteria = request.get(NEW_OPTIONAL_CRITERIA);
    String newOptionalCriteriaSign = request.get(NEW_OPTIONAL_CRITERIA_SIGN);

    if (startTime != null) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTimeZone(startTime.getTimeZone());
      calendar.setTimeInMillis(startTime.getTimeInMillis());
      new CriterionStartTime(calendar).store();
      fireChange = true;
    }

//    request.getParameters().get(parameter)
    if ((newOptionalCriteria != null) && (newOptionalCriteriaSign != null)) {
      new CriterionZodiacSign(ActivePoint.getActivePoint(Integer.parseInt(newOptionalCriteria)), Integer.parseInt(newOptionalCriteriaSign), false, "green").store();
      fireChange = true;
    }

    Criterion[] criteria = Criteria.getCriteria();
    for (int i = 0; i < criteria.length; i++) {
      buffer.append(criteria[i].toString());
    }

    if ((newOptionalCriteria != null) && (newOptionalCriteriaSign == null)) {
      fillOptionalCriteriaForm(request, buffer, Integer.parseInt(newOptionalCriteria));
    }
    fillControllables(buffer);

    if (fireChange) {
      fillRefreshScript(buffer);
    }
  }

//  private final static void fillCriteriaForm(Request request, LocalizedStringBuffer buffer, String[] data, String[] types) {
//    Map<String, Set<String>> criteria = TypeCriterion.getCriteria(types);
//    String[] keys = criteria.keySet().toArray(new String[0]);
//
//    buffer.append("<table><tr>");
//    for (int key = 0; key < keys.length; key++) {
//      buffer.append("<td>");
//      buffer.append("</td>");
//      
//    }
//
//    buffer.append("<table><tr><td>");
//    buffer.localize(planet);
//    buffer.append("</td>");
//
//    buffer.append("<td>");
//    String controlId = "controller_" + NEW_OPTIONAL_CRITERIA_SIGN;
//    String function = "function(new_option_sign) { window.location.href='new_option.html?_d=" + ID + "&amp;" + NEW_OPTIONAL_CRITERIA + "=" + planet + "&amp;" + NEW_OPTIONAL_CRITERIA_SIGN + "=' + new_option_sign }";
//    ComponentControllable.fill(buffer, controlId, "Sign", function);
//    buffer.append("<div id='");
//    buffer.append(controlId);
//    buffer.append("' width='20'><font color='gray'><i>");
//    buffer.localize("sign");
//    buffer.append("</i></font></div>");
//    buffer.append("</td></tr></table>");
//  }

  private final static void fillControllables(LocalizedStringBuffer buffer) {
    String controlId = "controller_" + NEW_OPTIONAL_CRITERIA;
    String function = "function(new_option) { window.location.href='new_option.html?_d=" + ID + "&amp;" + NEW_OPTIONAL_CRITERIA + "=' + new_option }";
    ComponentControllable.fill(buffer, controlId, "Planet", function);
    buffer.append("<div id='");
    buffer.append(controlId);
    buffer.append("' width='100%'><font color='gray'><i>");
    buffer.localize("new optional criteria");
    buffer.append("</i></font></div>");
  }

  private final static void fillOptionalCriteriaForm(Request request, LocalizedStringBuffer buffer, int planet) {
    buffer.append("<table><tr><td>");
    buffer.localize(planet);
    buffer.append("</td>");

    buffer.append("<td>");
    String controlId = "controller_" + NEW_OPTIONAL_CRITERIA_SIGN;
    String function = "function(new_option_sign) { window.location.href='new_option.html?_d=" + ID + "&amp;" + NEW_OPTIONAL_CRITERIA + "=" + planet + "&amp;" + NEW_OPTIONAL_CRITERIA_SIGN + "=' + new_option_sign }";
    ComponentControllable.fill(buffer, controlId, "Sign", function);
    buffer.append("<div id='");
    buffer.append(controlId);
    buffer.append("' width='20'><font color='gray'><i>");
    buffer.localize("sign");
    buffer.append("</i></font></div>");
    buffer.append("</td></tr></table>");
  }

  private final static void fillRefreshScript(LocalizedStringBuffer buffer) {
    buffer.newline();
    buffer.append("<script language='javascript'>");
    buffer.append("top.fireEvent(window, 'criteria', 'refresh')");
    buffer.append("</script>");
    buffer.newline();
  }

}