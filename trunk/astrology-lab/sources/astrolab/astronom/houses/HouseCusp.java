package astrolab.astronom.houses;

import astrolab.astronom.ActivePoint;

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
    return HouseSystem.NAMES_MAP.get(getId());
  }

  public double getPosition() {
    return position;
  }

  protected int getSystemType() {
    return ActivePoint.SYSTEM_HOUSE;
  }

}
