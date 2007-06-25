package astrolab.astronom.houses;

import astrolab.astronom.ActivePoint;
import astrolab.db.Text;

public class HouseCusp extends ActivePoint {

  private double position;

  protected HouseCusp(int id, double position) {
    super(id);
    this.position = position;
  }

  public String getIcon() {
    return null;
  }

  public String getName() {
    return Text.getText(getId());
  }

  public double getPosition() {
    return position;
  }

  protected int getSystemType() {
    return ActivePoint.SYSTEM_HOUSE;
  }

}
