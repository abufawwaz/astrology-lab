package astrolab.criteria;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.SpacetimeEvent;
import astrolab.astronom.houses.HouseSystem;
import astrolab.astronom.util.Zodiac;
import astrolab.db.Text;

public class CriterionHouse extends CriterionAlgorithm {

  public CriterionHouse() {
    super(CriterionAlgorithm.ALGORITHM_HOUSE);
  }

  public boolean accepts(Criterion criterion) {
    if (criterion.getAction() != 0) {
      return false;
    }
    return true;
  }

  public String[] getActorTypes() {
    return new String[] { Criterion.TYPE_PLANET };
  }

  public String[] getFactorTypes() {
    return new String[] { Criterion.TYPE_HOUSE };
  }

  public int calculateMark(Criterion criterion, SpacetimeEvent periodStart, SpacetimeEvent periodEnd) {
    double positionStart = ActivePoint.getActivePoint(criterion.getActor(), periodStart).getPosition();
    double positionEnd = ActivePoint.getActivePoint(criterion.getActor(), periodEnd).getPosition();
    double house1Start = ActivePoint.getActivePoint(criterion.getFactor(), periodStart).getPosition();
    double house1End = ActivePoint.getActivePoint(criterion.getFactor(), periodEnd).getPosition();
    double house2Start = ActivePoint.getActivePoint(HouseSystem.getNextHouse(criterion.getFactor()), periodStart).getPosition();
    double house2End = ActivePoint.getActivePoint(HouseSystem.getNextHouse(criterion.getFactor()), periodEnd).getPosition();

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

  protected String toStringBeforeFactor(Criterion criterion) {
    return Text.getText("in");
  }

}
