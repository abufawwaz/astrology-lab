package astrolab.criteria;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.SpacetimeEvent;
import astrolab.astronom.planet.SolarSystem;
import astrolab.db.Text;

public class Course {

  public final static int[] MAJOR_POINTS = new int[] {
      Text.getId(SolarSystem.SUN),
      Text.getId(SolarSystem.MOON),
      Text.getId(SolarSystem.MERCURY),
      Text.getId(SolarSystem.VENUS),
      Text.getId(SolarSystem.MARS),
      Text.getId(SolarSystem.JUPITER),
      Text.getId(SolarSystem.SATURN),
  };

  public final static int[] MAJOR_ASPECTS = new int[] { 0, 60, 90, 120, 180 };

  private int activePoint;
  private int[] points;
  private SpacetimeEvent periodStart;
  private SpacetimeEvent periodEnd;
  private Hashtable<SpacetimeEvent, Set<Integer>> calendarAspects = new Hashtable<SpacetimeEvent, Set<Integer>>();

  private int shift = 24;

  protected Course(int point, int[] points, SpacetimeEvent periodStart, SpacetimeEvent periodEnd) {
    this.activePoint = point;
    this.periodStart = periodStart;
    this.periodEnd = periodEnd;

    ArrayList<Integer> ps = new ArrayList<Integer>();
    for (int i = 0; i < points.length; i++) {
      if (points[i] != activePoint) {
        ps.add(points[i]);
      }
    }
    this.points = new int[ps.size()];
    for (int i = 0; i < this.points.length; i++) {
      this.points[i] = ps.get(i);
    }

    analyze();
  }

  public boolean occurAspect(int[] aspects) {
    for (Set<Integer> set: calendarAspects.values()) {
      for (int a: aspects) {
        if (set.contains(a)) {
          return true;
        }
      }
    }

    return false;
  }

  private SpacetimeEvent geNextCalendar(SpacetimeEvent c) {
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
          throw new IllegalStateException("Cannot find next calendar");
        }
      }

      result = c.getMovedSpacetimeEvent(SpacetimeEvent.HOUR_OF_DAY, hourOffset2);
      positionCurrent = (int) ActivePoint.getActivePoint(activePoint, result).getPosition();
    }

    shift = hourOffset2;
//System.err.println(" " + ActivePoint.getActivePoint(activePoint, result).getName() + " is at " + ActivePoint.getActivePoint(activePoint, result).getPosition());
    return result;
  }

  private final int diffPositions(int p1, int p2) {
    int result = Math.abs(p1 - p2);
    if (result > 180) {
      result -= 360;
      result = Math.abs(result);
    }
    return result;
  }

  private void analyze() {
//System.err.println(" analyze " + ActivePoint.getActivePoint(activePoint, periodStart).getName() + " " + periodStart.getTime());
    SpacetimeEvent miniPeriodStart = periodStart;
    SpacetimeEvent miniPeriodEnd = geNextCalendar(miniPeriodStart);
    int sign = (int) (ActivePoint.getActivePoint(activePoint, miniPeriodStart).getPosition() / 30);

    while (miniPeriodStart.isBefore(periodEnd) || (sign == (int) (ActivePoint.getActivePoint(activePoint, miniPeriodEnd).getPosition() / 30))) {
      analyze(miniPeriodStart, miniPeriodEnd);

      // get to next point
      miniPeriodStart = miniPeriodEnd;
      miniPeriodEnd = geNextCalendar(miniPeriodStart);
    }
  }

  private void analyze(SpacetimeEvent miniPeriodStart, SpacetimeEvent miniPeriodEnd) {
//System.err.println(" analysis: " + miniPeriodStart.toMySQLString() + " -> " + miniPeriodEnd.toMySQLString());
    double positionStart = ActivePoint.getActivePoint(activePoint, miniPeriodStart).getPosition();
    double positionEnd = ActivePoint.getActivePoint(activePoint, miniPeriodEnd).getPosition();

    for (int i = 0; i < points.length; i++) {
      int aspect = CriteriaUtilities.hasAspectsBetween(points[i], positionStart, miniPeriodStart, positionEnd, miniPeriodEnd);
      if (aspect >= 0) {
        ensureAspectSet(miniPeriodStart).add(aspect);
      }
    }
  }

  private Set<Integer> ensureAspectSet(SpacetimeEvent calendar) {
    Set<Integer> result = calendarAspects.get(calendar);
    if (result == null) {
      result = new HashSet<Integer>();
      calendarAspects.put(calendar, result);
    }
    return result;
  }

}