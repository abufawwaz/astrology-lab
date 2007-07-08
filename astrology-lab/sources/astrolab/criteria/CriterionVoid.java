package astrolab.criteria;

import java.util.HashSet;

import astrolab.astronom.SpacetimeEvent;
import astrolab.astronom.planet.SolarSystem;
import astrolab.db.Text;

public class CriterionVoid extends CriterionAlgorithm {

  private final static HashSet<Integer> ASPECTS = new HashSet<Integer>();

  static {
    ASPECTS.add(0);
    ASPECTS.add(30);
    ASPECTS.add(60);
    ASPECTS.add(90);
    ASPECTS.add(120);
    ASPECTS.add(180);
  }

  public CriterionVoid() {
    super(ALGORITHM_VOID);
  }

  public boolean accepts(Criterion criterion) {
    if (criterion.getAction() != 0) {
      return false;
    }
    if (criterion.getFactor() != 0) {
      return false;
    }

    for (int factor: SolarSystem.PLANETS_IDS) {
      if (criterion.getActor() == factor) {
        return true;
      }
    }
    return false;
  }

  public String[] getActorTypes() {
    return new String[] { Criterion.TYPE_PLANET };
  }

  protected String toStringAfterActor(Criterion criterion) {
    return Text.getText("void");
  }

  public boolean isMarkPositive() {
    return false;
  }

  public int calculateMark(Criterion criterion, SpacetimeEvent periodStart, SpacetimeEvent periodEnd) {
    int actor = criterion.getActor();
    int aspect = 0;

    for (int factor: SolarSystem.PLANETS_IDS) {
      if (actor != factor) {
        aspect = CriteriaUtilities.hasAspectsBetween(actor, factor, periodStart, periodEnd, Modifier.MODIFIER_ACTOR_SELF);
  
        if (ASPECTS.contains(aspect)) {
          return 0;
        }
      }
    }

    return 1;
  }

}
