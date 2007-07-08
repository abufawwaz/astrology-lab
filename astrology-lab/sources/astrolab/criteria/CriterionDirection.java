package astrolab.criteria;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.SpacetimeEvent;
import astrolab.db.Text;

public class CriterionDirection extends CriterionAlgorithm {

  public final static String DIRECTION_DIRECT = "Dx";
  public final static String DIRECTION_STATIONARY = "Sx";
  public final static String DIRECTION_RETROGRADE = "Rx";

  public final static int ID_DIRECT = Text.getId(DIRECTION_DIRECT);
  public final static int ID_STATIONARY = Text.getId(DIRECTION_STATIONARY);
  public final static int ID_RETROGRADE = Text.getId(DIRECTION_RETROGRADE);

  private final double STATION = 1D / 60 / 60; // a second

  protected CriterionDirection() {
    super(ALGORITHM_DIRECTION);
  }

  public boolean accepts(Criterion criterion) {
    if (criterion.getFactor() != 0) {
      return false;
    }
    if (criterion.getAction() == 0) {
      return true;
    }
    if (criterion.getAction() == ID_DIRECT) {
      return true;
    }
    if (criterion.getAction() == ID_STATIONARY) {
      return true;
    }
    if (criterion.getAction() == ID_RETROGRADE) {
      return true;
    }
    return false;
  }

  public String[] getActorTypes() {
    return new String[] { Criterion.TYPE_PLANET };
  }

  public String[] getActionTypes() {
    return new String[] { Criterion.TYPE_DIRECTION };
  }

  public int calculateMark(Criterion criterion, SpacetimeEvent periodStart, SpacetimeEvent periodEnd) {
    int direction = criterion.getAction();
    double position1 = ActivePoint.getActivePoint(criterion.getActor(), periodStart).getPosition();
    double position2 = ActivePoint.getActivePoint(criterion.getActor(), periodStart.getMovedSpacetimeEvent(SpacetimeEvent.HOUR_OF_DAY, 1)).getPosition();

    if (position2 < position1 - 180) {
      position2 += 360;
    }
    if (position2 > position1 + 180) {
      position2 -= 360;
    }

System.err.println(" direction: " + direction + " (" + ID_RETROGRADE + ")");
    if (direction == ID_DIRECT) {
      return (position2 > position1) ? 1 : 0;
    } else if (direction == ID_STATIONARY) {
      return (Math.abs(position2 - position1) < STATION) ? 1 : 0;
    } else if (direction == ID_RETROGRADE) {
System.err.println(" is retrograde?: " + position1 + " " + position2 + " -> " + (position2 < position1));
      return (position2 < position1) ? 1 : 0;
    } else {
      throw new IllegalStateException("Direction " + direction + " is not valid!");
    }
  }

}
