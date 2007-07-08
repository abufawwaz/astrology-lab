package astrolab.criteria;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.SpacetimeEvent;

public class Course {

  private int activePoint;
  private int shift = 24;

  protected Course(int point) {
    this.activePoint = point;
  }

  public SpacetimeEvent getNextCalendar(SpacetimeEvent c) {
    int hourOffset1 = 0;
    int hourOffset2 = shift;
    SpacetimeEvent result = c.getMovedSpacetimeEvent(SpacetimeEvent.HOUR_OF_DAY, hourOffset2);

    int positionDiff;
    int positionStart = (int) ActivePoint.getActivePoint(activePoint, c).getPosition();
    int positionCurrent = (int) ActivePoint.getActivePoint(activePoint, result).getPosition();

    while ((positionDiff = diffPositions(positionStart, positionCurrent)) != 1) {
      int newOffset = (hourOffset1 + hourOffset2) / 2;

      if (positionDiff == 0) {
        hourOffset1 = hourOffset2;
        hourOffset2 += hourOffset2;
      } else {
        hourOffset2 = newOffset;
        if (hourOffset1 == hourOffset2) {
          return null;
        }
      }

      result = c.getMovedSpacetimeEvent(SpacetimeEvent.HOUR_OF_DAY, hourOffset2);
      positionCurrent = (int) ActivePoint.getActivePoint(activePoint, result).getPosition();
    }

    shift = hourOffset2;
//System.err.println(" " + ActivePoint.getActivePoint(activePoint, result).getName() + " is at " + ActivePoint.getActivePoint(activePoint, result).getPosition());
    return ((positionStart / 30) == (positionCurrent / 30)) ? result : null;
  }

  private final int diffPositions(int p1, int p2) {
    int result = Math.abs(p1 - p2);
    if (result > 180) {
      result -= 360;
      result = Math.abs(result);
    }
    return result;
  }

}