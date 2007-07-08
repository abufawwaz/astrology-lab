package astrolab.criteria;

import astrolab.astronom.Aspects;
import astrolab.astronom.SpacetimeEvent;

public class CriterionAspect extends CriterionAlgorithm {

  public CriterionAspect() {
    super(ALGORITHM_ASPECT);
  }

  public boolean accepts(Criterion criterion) {
    return true;
  }

  public String[] getActorTypes() {
    return new String[] { Criterion.TYPE_PLANET };
  }

  public String[] getActionTypes() {
    return new String[] { Criterion.TYPE_ASPECT };
  }

  public String[] getFactorTypes() {
    return new String[] { Criterion.TYPE_PLANET, Criterion.TYPE_HOUSE };
  }

  public int calculateMark(Criterion criterion, SpacetimeEvent periodStart, SpacetimeEvent periodEnd) {
    int action = criterion.getAction();
    int aspect = CriteriaUtilities.hasAspectsBetween(criterion.getActor(), criterion.getFactor(), periodStart, periodEnd, criterion.getModifiers());

    if (action == Aspects.CONJUNCT) {
      if (aspect == 0) {
        return 1;
      }
    } else if (action == Aspects.SEXTILE) {
      if (aspect == 60) {
        return 1;
      }
    } else if (action == Aspects.SQUARE) {
      if (aspect == 90) {
        return 1;
      }
    } else if (action == Aspects.TRINE) {
      if (aspect == 120) {
        return 1;
      }
    } else if (action == Aspects.OPPOSITION) {
      if (aspect == 180) {
        return 1;
      }
    }

    return 0;
  }

}
