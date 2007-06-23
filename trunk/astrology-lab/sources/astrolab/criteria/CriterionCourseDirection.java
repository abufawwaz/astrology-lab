package astrolab.criteria;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.SpacetimeEvent;
import astrolab.db.Text;
import astrolab.web.server.content.LocalizedStringBuffer;

public class CriterionCourseDirection extends Criterion {

  public final static String DIRECTION_DIRECT = "direct";
  public final static String DIRECTION_STATIONARY = "stationary";
  public final static String DIRECTION_RETROGRADE = "retrograde";

  public final static int ID_DIRECT = Text.getId(DIRECTION_DIRECT);
  public final static int ID_STATIONARY = Text.getId(DIRECTION_STATIONARY);
  public final static int ID_RETROGRADE = Text.getId(DIRECTION_RETROGRADE);

  private int direction;

  public CriterionCourseDirection() {
    super();
  }

  public CriterionCourseDirection(int id, int activePoint, int direction) {
    super(id, TYPE_COURSE_DIRECTION, activePoint);
    this.direction = direction;
  }

  public int getMark(SpacetimeEvent periodStart, SpacetimeEvent periodEnd) {
    double position = ActivePoint.getActivePoint(getActivePoint(), periodStart).getPosition();
    int limitFrom = (int) Math.floor(position / 30) * 30;
    int limitTo = limitFrom + 30;
    SpacetimeEvent c = periodStart;

    do {
      if (hasDirectionAt(c)) {
        return 1;
      }

      // move a day ahead
      c = c.getMovedSpacetimeEvent(SpacetimeEvent.DAY_OF_YEAR, 1);
      position = ActivePoint.getActivePoint(getActivePoint(), c).getPosition();
    } while ((position > limitFrom) && (position < limitTo));

    return 0;
  }

  public boolean hasDirectionAt(SpacetimeEvent calendar) {
    double position1 = ActivePoint.getActivePoint(getActivePoint(), calendar).getPosition();
    double position2 = ActivePoint.getActivePoint(getActivePoint(), calendar.getMovedSpacetimeEvent(SpacetimeEvent.HOUR_OF_DAY, 1)).getPosition();

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
    new CriterionCourseDirection(getId(), Integer.parseInt(inputValues[0]), direction).store();
  }

  public void toString(LocalizedStringBuffer output) {
    output.localize(getActor());
    output.append(" ");
    output.localize("is");
    output.append(" ");

    if (direction == ID_DIRECT) {
      output.localize(DIRECTION_DIRECT.toLowerCase());
    } else if (direction == ID_STATIONARY) {
      output.localize(DIRECTION_STATIONARY.toLowerCase());
    } else if (direction == ID_RETROGRADE) {
      output.localize(DIRECTION_RETROGRADE.toLowerCase());
    }

    output.append(" ");
    output.localize("of course");
  }

}
