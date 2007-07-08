package astrolab.criteria;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.SpacetimeEvent;

public class CriteriaUtilities {

  final static int hasAspectsBetween(int point1, int point2, SpacetimeEvent periodStart, SpacetimeEvent periodEnd, int modifiers) {
    double position1Start = ActivePoint.getActivePoint(point1, periodStart).getPosition();
    double position1End = ActivePoint.getActivePoint(point1, periodEnd).getPosition();
    double position2Start = ActivePoint.getActivePoint(point2, periodStart).getPosition();
    double position2End = ActivePoint.getActivePoint(point2, periodEnd).getPosition();

    if (Math.abs(position1Start - position1End) > Math.abs(position2Start - position2End)) {
      return hasAspectsBetweenPoints(point2, point1, periodStart, periodEnd, modifiers);
    } else {
      return hasAspectsBetweenPoints(point1, point2, periodStart, periodEnd, modifiers);
    }
  }

  final static int hasAspectsBetweenPoints(int point1, int point2, SpacetimeEvent periodStart, SpacetimeEvent periodEnd, int modifiers) {
    double positionStart = ActivePoint.getActivePoint(point1, periodStart).getPosition();
    double positionEnd = ActivePoint.getActivePoint(point1, periodEnd).getPosition();

    if ((modifiers & Modifier.MODIFIER_ACTION_ASPECT_APPLYING) != 0) {
      if (positionStart <= positionEnd) {
        positionStart -= 5;
      } else {
        positionStart += 5;
      }
    }
    if ((modifiers & Modifier.MODIFIER_ACTION_ASPECT_SEPARATING) != 0) {
      if (positionStart <= positionEnd) {
        positionEnd += 5;
      } else {
        positionEnd -= 5;
      }
    }
    return hasAspectsBetween(point2, positionStart, periodStart, positionEnd, periodEnd);
  }

  private final static int hasAspectsBetween(int point, double positionStart, SpacetimeEvent periodStart, double positionEnd, SpacetimeEvent periodEnd) {
    double position1 = ActivePoint.getActivePoint(point, periodStart).getPosition();
    double position2 = ActivePoint.getActivePoint(point, periodEnd).getPosition();
    double distance1 = position1 - positionStart;
    double distance2 = position2 - positionEnd;
    int signDistance1 = getSignDistance(distance1);
    int signDistance2 = getSignDistance(distance2);

    if (signDistance1 != signDistance2) {
      return Math.max(signDistance1, signDistance2);
    } else if ((int) Math.signum(distance1) != (int) Math.signum(distance2)) {
      return 0;
    } else if ((int) Math.signum(distance1 - 180) != (int) Math.signum(distance2 - 180)) {
      return 180;
    } else {
      return -1;
    }
  }

  private final static int getSignDistance(double distance) {
    int result = (int) (Math.abs(distance) / 30);
    if (result > 5) {
      result = 11 - result;
    }
    return result * 30;
  }

}
