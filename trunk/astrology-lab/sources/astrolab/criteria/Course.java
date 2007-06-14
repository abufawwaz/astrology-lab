package astrolab.criteria;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import astrolab.astronom.ActivePoint;
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
  private Calendar periodStart;
  private Calendar periodEnd;
  private Hashtable<Calendar, Set<Integer>> calendarAspects = new Hashtable<Calendar, Set<Integer>>();

  protected Course(int point, int[] points, Calendar periodStart, Calendar periodEnd) {
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
    Calendar moment = periodStart;
    int sign = (int) (ActivePoint.getActivePoint(activePoint, moment).getPosition() / 30);

    while (sign == (int) (ActivePoint.getActivePoint(activePoint, moment).getPosition() / 30)) {
      for (int a: aspects) {
        Set<Integer> set = calendarAspects.get(moment);
        if ((set != null) && set.contains(a)) {
          return true;
        }
      }

      // get to next point
      moment = geNextCalendar(moment);
    }

    return false;
  }

  private Calendar geNextCalendar(Calendar c) {
    Calendar result = Calendar.getInstance();
    result.setTime(c.getTime());
    result.add(Calendar.HOUR_OF_DAY, 1);
    return result;
  }

  private void analyze() {
    Calendar miniPeriodStart = periodStart;
    Calendar miniPeriodEnd = geNextCalendar(miniPeriodStart);
    int sign = (int) (ActivePoint.getActivePoint(activePoint, miniPeriodStart).getPosition() / 30);

    while ((miniPeriodStart.compareTo(periodEnd) < 0) || (sign == (int) (ActivePoint.getActivePoint(activePoint, miniPeriodEnd).getPosition() / 30))) {
      analyze(miniPeriodStart, miniPeriodEnd);

      // get to next point
      miniPeriodStart = miniPeriodEnd;
      miniPeriodEnd = geNextCalendar(miniPeriodStart);
    }
  }

  private void analyze(Calendar miniPeriodStart, Calendar miniPeriodEnd) {
    double positionStart = ActivePoint.getActivePoint(activePoint, miniPeriodStart).getPosition();
    double positionEnd = ActivePoint.getActivePoint(activePoint, miniPeriodEnd).getPosition();

    for (int i = 0; i < points.length; i++) {
      int aspect = hasAspectsWith(points[i], positionStart, miniPeriodStart, positionEnd, miniPeriodEnd);
      if (aspect >= 0) {
        ensureAspectSet(miniPeriodStart).add(aspect);
      }
    }
  }

  private int hasAspectsWith(int point, double positionStart, Calendar periodStart, double positionEnd, Calendar periodEnd) {
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

  private final int getSignDistance(double distance) {
    int result = (int) (Math.abs(distance) / 30);
    if (result > 5) {
      result = 11 - result;
    }
    return result * 30;
  }

  private Set<Integer> ensureAspectSet(Calendar calendar) {
    Set<Integer> result = calendarAspects.get(calendar);
    if (result == null) {
      result = new HashSet<Integer>();
      calendarAspects.put(calendar, result);
    }
    return result;
  }

}