package astrolab.criteria;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.SpacetimeEvent;
import astrolab.astronom.planet.SolarSystem;
import astrolab.astronom.util.Zodiac;
import astrolab.db.Text;

public class CriterionPhase extends CriterionAlgorithm {

  public final static int PHASE_NEW = Text.getId("phase_new");
  public final static int PHASE_FIRST_QUATER = Text.getId("phase_first_quarter");
  public final static int PHASE_SECOND_QUATER = Text.getId("phase_second_quarter");
  public final static int PHASE_FULL = Text.getId("phase_full");
  public final static int PHASE_THIRD_QUATER = Text.getId("phase_third_quarter");
  public final static int PHASE_FOURTH_QUATER = Text.getId("phase_fourth_quarter");

  public final static int[] PHASES = {
    PHASE_NEW, PHASE_FIRST_QUATER, PHASE_SECOND_QUATER,
    PHASE_FULL, PHASE_THIRD_QUATER, PHASE_FOURTH_QUATER,
  };

  public CriterionPhase() {
    super(ALGORITHM_PHASE);
  }

  public boolean accepts(Criterion criterion) {
    if (criterion.getFactor() != 0) {
      return false;
    }
    if (criterion.getAction() == 0) {
      return true;
    }
    for (int phase: PHASES) {
      if (criterion.getAction() == phase) {
        return true;
      }
    }
    return false;
  }

  public String[] getActorTypes() {
    return new String[] { Criterion.TYPE_PLANET };
  }

  public String[] getActionTypes() {
    return new String[] { Criterion.TYPE_PHASE };
  }

  public String[] getFactorTypes() {
    return new String[0];
  }

  public int calculateMark(Criterion criterion, SpacetimeEvent periodStart, SpacetimeEvent periodEnd) {
    int phase = criterion.getAction();
    ActivePoint point = ActivePoint.getActivePoint(criterion.getActor(), periodStart);
    if (point.getName().equals(SolarSystem.SUN)) {
      // the Sun is always full
      return (phase == PHASE_FULL) ? 1 : 0;
    } else {
      double positionPoint = ActivePoint.getActivePoint(criterion.getActor(), periodStart).getPosition();
      double positionSun = ActivePoint.getActivePoint(SolarSystem.SUN, periodStart).getPosition();

      double distance = positionPoint - positionSun;
      double absDistance = Math.abs(distance);

      if (((absDistance <= 10) || (absDistance >= 350)) && (phase == PHASE_NEW)) {
        return 1;
      } else if ((absDistance >= 170) && (absDistance <= 190) && (phase == PHASE_FULL)) {
        return 1;
      } else {
        int phaseIndex = (int) (Zodiac.degree(distance) / 90);
        switch (phaseIndex) {
          case 0: return (phase == PHASE_FIRST_QUATER) ? 1 : 0;
          case 1: return (phase == PHASE_SECOND_QUATER) ? 1 : 0;
          case 2: return (phase == PHASE_THIRD_QUATER) ? 1 : 0;
          case 3: return (phase == PHASE_FOURTH_QUATER) ? 1 : 0;
        }
      }
      return 0;
    }
  }

}
