package astrolab.perspective.election;

import java.util.HashSet;

import astrolab.criteria.Criteria;
import astrolab.criteria.Criterion;
import astrolab.criteria.CriterionAlgorithm;
import astrolab.criteria.Modifier;
import astrolab.criteria.Modifiers;
import astrolab.db.Text;
import astrolab.web.Display;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.component.ComponentControllable;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayElectionaryRestrictionList extends HTMLFormDisplay {

  public final static int ID = Display.getId(DisplayElectionaryRestrictionList.class);

  private final static String CRITERIA_TYPE = "_cr.type";
  private final static String CRITERIA_ACTOR = "_cr.actor";
  private final static String CRITERIA_ACTION = "_cr.action";
  private final static String CRITERIA_FACTOR = "_cr.factor";
  private final static String CRITERIA_MODIFIERS = "_cr.modifiers";

  public DisplayElectionaryRestrictionList() {
    super("New Criteria", ID, true);
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    fillSelection(request, buffer, checkSelection(request, buffer));
  }

  public void fillSelection(Request request, LocalizedStringBuffer buffer, boolean home) {
    int type = home ? 0 : request.getParameters().getInt(CRITERIA_TYPE);
    int actor = home ? 0 : request.getParameters().getInt(CRITERIA_ACTOR);
    int action = home ? 0 : request.getParameters().getInt(CRITERIA_ACTION);
    int factor = home ? 0 : request.getParameters().getInt(CRITERIA_FACTOR);
    int modifiers = home  ? Modifier.MODIFIER_ALL : request.getParameters().getInt(CRITERIA_MODIFIERS);
    Criterion[] criteria = Criteria.getCriteria(type, actor, action, factor, modifiers);
    HashSet<String> types = new HashSet<String>();

    buffer.append("<table border='1'>");
    buffer.append("<tr>");

    buffer.append("<td>");
    fillModifiers(buffer, Modifiers.ACTOR_MODIFIERS, modifiers, home);
    buffer.append("</td>");

    buffer.append("<td>");
    fillModifiers(buffer, Modifiers.ACTION_MODIFIERS, modifiers, home);
    buffer.append("</td>");

    buffer.append("<td>");
    fillModifiers(buffer, Modifiers.FACTOR_MODIFIERS, modifiers, home);
    buffer.append("</td>");

    buffer.append("</tr>");

    buffer.append("<tr>");

    // actors
    buffer.append("<td>");
    buffer.append("<table border='0'>");

    if (actor == 0) {
      types.clear();
      for (Criterion c: criteria) {
        for (String criteriaType: c.getAlgorithm().getActorTypes()) {
          types.add(criteriaType);
        }
      }
      for (String criteriaType: types) {
        fillControl(buffer, criteriaType, CRITERIA_ACTOR, home);
      }
    } else {
      fillControl(buffer, Text.getText(actor), CRITERIA_ACTOR, home);
    }
    buffer.append("</table>");
    buffer.append("</td>");

    // actions
    buffer.append("<td>");
    buffer.append("<table border='0'>");

    if (action == 0) {
      types.clear();
      for (Criterion c: criteria) {
        for (String criteriaType: c.getAlgorithm().getActionTypes()) {
          types.add(criteriaType);
        }
      }
      for (String criteriaType: types) {
        fillControl(buffer, criteriaType, CRITERIA_ACTION, home);
      }
    } else {
      fillControl(buffer, Text.getText(action), CRITERIA_ACTION, home);
    }
    buffer.append("</table>");
    buffer.append("</td>");

    // factors
    buffer.append("<td>");
    buffer.append("<table border='0'>");

    if (factor == 0) {
      types.clear();
      for (Criterion c: criteria) {
        for (String criteriaType: c.getAlgorithm().getFactorTypes()) {
          types.add(criteriaType);
        }
      }
      for (String criteriaType: types) {
        fillControl(buffer, criteriaType, CRITERIA_FACTOR, home);
      }
    } else {
      fillControl(buffer, Text.getText(factor), CRITERIA_ACTION, home);
    }
    buffer.append("</table>");
    buffer.append("</td>");

    buffer.append("</tr>");
    buffer.append("</table>");

    for (int i = 0; i < Math.min(criteria.length, 9); i++) {
      boolean isComplete = isComplete(criteria[i]);
      if (isComplete) {
        buffer.append("<a href='new_option.html?_d=");
        buffer.append(String.valueOf(ID));
        buffer.append((!home) ? getParameters(CRITERIA_MODIFIERS) : "");
        buffer.append("&amp;");
        buffer.append(CRITERIA_MODIFIERS);
        buffer.append("=");
        buffer.append(criteria[i].getModifiers());
        buffer.append("&amp;");
        buffer.append(CRITERIA_TYPE);
        buffer.append("=");
        buffer.append(criteria[i].getAlgorithm().getType());
        buffer.append("'>");
        criteria[i].toString(buffer);
        buffer.append("</a>");
      } else {
        criteria[i].toString(buffer);
      }
      buffer.append("<br />");
    }
    if (criteria.length > 9) {
      buffer.append("...");
    }
  }

  private final boolean isComplete(Criterion criterion) {
    CriterionAlgorithm algorithm = criterion.getAlgorithm();

    if ((algorithm.getActorTypes().length > 0) && (criterion.getActor() == 0)) {
      return false;
    }
    if ((algorithm.getActionTypes().length > 0) && (criterion.getAction() == 0)) {
      return false;
    }
    if ((algorithm.getFactorTypes().length > 0) && (criterion.getFactor() == 0)) {
      return false;
    }

    return true;
  }

  private final void fillModifiers(LocalizedStringBuffer buffer, int[] mods, int modifiers, boolean home) {
    for (int mod: mods) {
      String[] modTexts = Modifiers.toString(modifiers, mod);

      buffer.append("<tr><td>");
      buffer.append("<a href='new_option.html?_d=");
      buffer.append(String.valueOf(ID));
      buffer.append((!home) ? getParameters(CRITERIA_MODIFIERS) : "");
      buffer.append("&amp;");
      buffer.append(CRITERIA_MODIFIERS);
      buffer.append("=");
      buffer.append(Modifiers.switchModifier(modifiers, mod));
      buffer.append("' ");
      // TODO: add an "on mouse over" change of text to modTexts[1]
      buffer.append(">");
      buffer.localize(modTexts[0]);
      buffer.append("</a>");
      buffer.append("</td></tr>");
    }
  }

  private final void fillControl(LocalizedStringBuffer buffer, String type, String parameter, boolean home) {
    String controlId = parameter + type;
    String function = "function(new_option) { window.location.href='new_option.html?"
      + "_d=" + ID
      + ((!home) ? getParameters(parameter) : "")
      + "&amp;" + parameter + "=' + new_option"
      + "}";

    buffer.append("<tr><td>");
    ComponentControllable.fill(buffer, controlId, type, function);
    buffer.append("<div id='");
    buffer.append(controlId);
    buffer.append("' width='100%'><font color='gray'><i>");
    buffer.localize(type);
    buffer.append("</i></font></div>");
    buffer.append("</td></tr>");
  }

  private final String getParameters(String exceptParameter) {
    Request request = Request.getCurrentRequest();
    String actor = (CRITERIA_ACTOR != exceptParameter) ? request.get(CRITERIA_ACTOR) : null;
    String action = (CRITERIA_ACTION != exceptParameter) ? request.get(CRITERIA_ACTION) : null;
    String factor = (CRITERIA_FACTOR != exceptParameter) ? request.get(CRITERIA_FACTOR) : null;
    String modifiers = (CRITERIA_MODIFIERS != exceptParameter) ? request.get(CRITERIA_MODIFIERS) : null;
    String result = "";

    if (actor != null) {
      result += "&amp;" + CRITERIA_ACTOR + "=" + actor;
    }
    if (action != null) {
      result += "&amp;" + CRITERIA_ACTION + "=" + action;
    }
    if (factor != null) {
      result += "&amp;" + CRITERIA_FACTOR + "=" + factor;
    }
    if (modifiers != null) {
      result += "&amp;" + CRITERIA_MODIFIERS + "=" + modifiers;
    }
    return result;
  }

  private final static boolean checkSelection(Request request, LocalizedStringBuffer buffer) {
    int type = request.getParameters().getInt(CRITERIA_TYPE);
    if (type != 0) {
      int actor = request.getParameters().getInt(CRITERIA_ACTOR);
      int action = request.getParameters().getInt(CRITERIA_ACTION);
      int factor = request.getParameters().getInt(CRITERIA_FACTOR);
      int modifiers = request.getParameters().getInt(CRITERIA_MODIFIERS);
      Criterion[] criteria = Criteria.getCriteria(type, actor, action, factor, modifiers);

      if (criteria.length == 1) {
        criteria[0].store();
        fillRefreshScript(buffer);
        return true;
      }
    }
    return false;
  }

  private final static void fillRefreshScript(LocalizedStringBuffer buffer) {
    buffer.newline();
    buffer.append("<script language='javascript'>");
    buffer.append("top.fireEvent(window, 'criteria', 'refresh')");
    buffer.append("</script>");
    buffer.newline();
  }

}