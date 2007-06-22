package astrolab.criteria;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.SpacetimeEvent;
import astrolab.db.Text;
import astrolab.web.server.content.LocalizedStringBuffer;

public class CriterionPositionDirection extends Criterion {

  public final static String DIRECTION_DIRECT = "direct";
  public final static String DIRECTION_STATIONARY = "stationary";
  public final static String DIRECTION_RETROGRADE = "retrograde";

  public final static int ID_DIRECT = Text.getId(DIRECTION_DIRECT);
  public final static int ID_STATIONARY = Text.getId(DIRECTION_STATIONARY);
  public final static int ID_RETROGRADE = Text.getId(DIRECTION_RETROGRADE);

  private int direction;

  public CriterionPositionDirection() {
    super();
  }

  public CriterionPositionDirection(int id, int activePoint, int direction, String color) {
    super(id, TYPE_POSITION_DIRECTION, activePoint, color);
    this.direction = direction;
  }

  public int getMark(SpacetimeEvent periodStart, SpacetimeEvent periodEnd) {
    double position1 = ActivePoint.getActivePoint(getActivePoint(), periodStart).getPosition();
    double position2 = ActivePoint.getActivePoint(getActivePoint(), periodStart.getMovedSpacetimeEvent(SpacetimeEvent.HOUR_OF_DAY, 1)).getPosition();

    if (position2 < position1 - 180) {
      position2 += 360;
    }
    if (position2 > position1 + 180) {
      position2 -= 360;
    }
 
    if (direction == ID_DIRECT) {
      return (position2 > position1) ? 1 : 0;
    } else if (direction == ID_STATIONARY) {
      return (Math.abs(position2 - position1) < 0.0001) ? 1 : 0;
    } else if (direction == ID_RETROGRADE) {
      return (position2 < position1) ? 1 : 0;
    } else {
      return 0;
    }
  }

  public String getName() {
    return "PositionDirection";
  }

  public String[] getActorTypes() {
    return new String[] { "Planet", "'position'", "Direction" };
  }

  public int getFactor() {
    return direction;
  }

  protected void store(String[] inputValues) {
    int direction = Integer.parseInt(inputValues[2]);
    new CriterionPositionPhase(getId(), Integer.parseInt(inputValues[0]), direction, "blue").store();
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
  }

}
