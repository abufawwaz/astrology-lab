package astrolab.criteria;

import astrolab.astronom.SpacetimeEvent;
import astrolab.db.Text;

public class ModifierActionAspectApplying implements Modifier {

  public Criterion getModifiedCriterion(Criterion criterion, SpacetimeEvent periodStart, SpacetimeEvent periodEnd) {
    return criterion;
  }

  public int getModifiedMark(Criterion criterion, SpacetimeEvent periodStart, SpacetimeEvent periodEnd) {
    return MARK_NO_EFFECT;
  }

  public String actorToString(Criterion criterion, String actor) {
    return actor;
  }

  public String actionToString(Criterion criterion, String action) {
    return Text.getText("applying") + " " + action;
  }

  public String factorToString(Criterion criterion, String factor) {
    return factor;
  }

  public String getSelectedString() {
    return "applying";
  }

  public String getUnselectedString() {
    return "exact";
  }

}