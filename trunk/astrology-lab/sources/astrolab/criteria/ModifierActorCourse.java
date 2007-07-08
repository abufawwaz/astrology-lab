package astrolab.criteria;

import astrolab.astronom.SpacetimeEvent;
import astrolab.db.Text;

public class ModifierActorCourse implements Modifier {

  public Criterion getModifiedCriterion(Criterion criterion, SpacetimeEvent periodStart, SpacetimeEvent periodEnd) {
    // no modification to the criterion
    return criterion;
  }

  public int getModifiedMark(Criterion criterion, SpacetimeEvent periodStart, SpacetimeEvent periodEnd) {
    CriterionAlgorithm algorithm = criterion.getAlgorithm();
    Course course = new Course(criterion.getActor());
    SpacetimeEvent time1 = periodStart;
    SpacetimeEvent time2 = periodStart;

    while ((time2 = course.getNextCalendar(time1)) != null) {
      if (algorithm.isMarkPositive()) {
        if (algorithm.calculateMark(criterion, time1, time2) != 0) {
          return 1;
        }
      } else {
        if (algorithm.calculateMark(criterion, time1, time2) == 0) {
          return 0;
        }
      }

      time1 = time2;
    };

    return algorithm.isMarkPositive() ? 0 : 1;
  }

  public String actionToString(Criterion criterion, String action) {
    return action;
  }

  public String actorToString(Criterion criterion, String actor) {
    return "(" + Text.getText("course") + " " + Text.getText("of") + " " + actor + ")";
  }

  public String factorToString(Criterion criterion, String factor) {
    return factor;
  }

  public String getSelectedString() {
    return "course";
  }

  public String getUnselectedString() {
    return "position";
  }

}
