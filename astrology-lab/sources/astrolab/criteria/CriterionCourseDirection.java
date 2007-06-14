package astrolab.criteria;

import java.util.Calendar;

import astrolab.astronom.ActivePoint;
import astrolab.db.Text;

public class CriterionCourseDirection extends Criterion {

  public final static String DIRECTION_DIRECT = "Direct";
  public final static String DIRECTION_STATIONARY = "Stationary";
  public final static String DIRECTION_RETROGRADE = "Retrograde";

  public final static int ID_DIRECT = Text.getId(DIRECTION_DIRECT);
  public final static int ID_STATIONARY = Text.getId(DIRECTION_STATIONARY);
  public final static int ID_RETROGRADE = Text.getId(DIRECTION_RETROGRADE);

  private int direction;

  public CriterionCourseDirection() {
    super();
  }

  public CriterionCourseDirection(int id, int activePoint, int direction, String color) {
    super(id, TYPE_COURSE_DIRECTION, activePoint, color);
    this.direction = direction;
  }

  public int getMark(Calendar periodStart, Calendar periodEnd) {
    double position = ActivePoint.getActivePoint(getActivePoint(), periodStart).getPosition();
    int limitFrom = (int) Math.floor(position / 30) * 30;
    int limitTo = limitFrom + 30;
    Calendar c = Calendar.getInstance();

    c.setTime(periodStart.getTime());
    do {
      if (hasDirectionAt(c)) {
        return 1;
      }

      // move a day ahead
      c.add(Calendar.DAY_OF_YEAR, 1);
      position = ActivePoint.getActivePoint(getActivePoint(), c).getPosition();
    } while ((position > limitFrom) && (position < limitTo));

    return 0;
  }

  public boolean hasDirectionAt(Calendar calendar) {
    double position1 = ActivePoint.getActivePoint(getActivePoint(), calendar).getPosition();
    calendar.add(Calendar.HOUR, 1);
    double position2 = ActivePoint.getActivePoint(getActivePoint(), calendar).getPosition();
    calendar.add(Calendar.HOUR, -1);

    if (position2 < position1 - 180) {
      position2 += 360;
    }
    if (position2 > position1 + 180) {
      position2 -= 360;
    }
 
    if (direction == ID_DIRECT) {
      return (position2 > position1);
    } else if (direction == ID_STATIONARY) {
      return (Math.abs(position2 - position1) < 0.0001);
    } else if (direction == ID_RETROGRADE) {
      return (position2 < position1);
    } else {
      return false;
    }
  }

  public String getName() {
    return "CourseDirection";
  }

  public String[] getActorTypes() {
    return new String[] { "Planet", "'course'", "Direction" };
  }

  public int getFactor() {
    return direction;
  }

  protected void store(String[] inputValues) {
    int direction = Integer.parseInt(inputValues[2]);
    new CriterionCourseDirection(getId(), Integer.parseInt(inputValues[0]), direction, "black").store();
  }

}
