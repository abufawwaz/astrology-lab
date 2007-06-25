package astrolab.criteria;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.SpacetimeEvent;
import astrolab.astronom.houses.HouseSystem;
import astrolab.astronom.util.Zodiac;
import astrolab.web.server.content.LocalizedStringBuffer;

public class CriterionPositionPlanetInHouse extends Criterion {

  public CriterionPositionPlanetInHouse() {
    super();
  }

  public CriterionPositionPlanetInHouse(int id, int activePoint, int house) {
    super(id, TYPE_POSITION_PLANET_HOUSE, activePoint, house);
  }

  public int getMark(SpacetimeEvent periodStart, SpacetimeEvent periodEnd) {
    double positionStart = ActivePoint.getActivePoint(getActivePoint(), periodStart).getPosition();
    double positionEnd = ActivePoint.getActivePoint(getActivePoint(), periodEnd).getPosition();
    double house1Start = ActivePoint.getActivePoint(getFactor(), periodStart).getPosition();
    double house1End = ActivePoint.getActivePoint(getFactor(), periodEnd).getPosition();
    double house2Start = ActivePoint.getActivePoint(HouseSystem.getNextHouse(getFactor()), periodStart).getPosition();
    double house2End = ActivePoint.getActivePoint(HouseSystem.getNextHouse(getFactor()), periodEnd).getPosition();

    if (Zodiac.isBetween(house1Start, positionStart, house2Start)) {
      // the planet is in the house at first time
      return 1;
    } else if (Zodiac.isBetween(house1End, positionEnd, house2End)) {
      // the planet is in the house at last time
      return 1;
    } else if (Zodiac.isBetween(house2Start, positionStart, house2Start + 150) && Zodiac.isBetween(house1End - 150, positionEnd, house1End)) {
      // the planet passed the house
      return 1;
    } else {
      return 0;
    }
  }

  public String getName() {
    return "PositionPlanetInHour";
  }

  public String[] getActorTypes() {
    return new String[] { "Planet", "'position'", "House" };
  }

  protected void store(String[] inputValues) {
    new CriterionPositionPlanetInHouse(getId(), Integer.parseInt(inputValues[0]), Integer.parseInt(inputValues[2])).store();
  }

  public void toString(LocalizedStringBuffer output) {
    output.localize(getActor());
    output.append(" ");
    output.localize("is");
    output.append(" ");
    output.localize("in");
    output.append(" ");
    output.localize(getFactor());
  }

}
