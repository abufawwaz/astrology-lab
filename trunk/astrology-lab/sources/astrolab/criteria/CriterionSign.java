package astrolab.criteria;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.SpacetimeEvent;
import astrolab.astronom.util.Zodiac;
import astrolab.db.Text;

public class CriterionSign extends CriterionAlgorithm {

  public final static int[] SIGNS = new int[Zodiac.SIGN.length];

  static {
    for (int i = 0; i < SIGNS.length; i++) {
      SIGNS[i] = Text.getId(Zodiac.SIGN[i]);
    }
  }

  public CriterionSign() {
    super(CriterionAlgorithm.ALGORITHM_SIGN);
  }

  public boolean accepts(Criterion criterion) {
    if (criterion.getAction() != 0) {
      return false;
    }
    if (criterion.getFactor() == 0) {
      return true;
    }
    for (int sign: SIGNS) {
      if (criterion.getFactor() == sign) {
        return true;
      }
    }
    return false;
  }

  public String[] getActorTypes() {
    return new String[] { Criterion.TYPE_PLANET, Criterion.TYPE_HOUSE };
  }

  public String[] getFactorTypes() {
    return new String[] { Criterion.TYPE_SIGN };
  }

  public int calculateMark(Criterion criterion, SpacetimeEvent periodStart, SpacetimeEvent periodEnd) {
    double position1 = ActivePoint.getActivePoint(criterion.getActor(), periodStart).getPosition();
    double position2 = ActivePoint.getActivePoint(criterion.getActor(), periodEnd).getPosition();
    double sign = (criterion.getFactor() - SIGNS[0]) * 30;

    if (Zodiac.isBetween(sign, position1, sign + 30)) {
      return 1;
    }
    if (Zodiac.isBetween(position1, sign, position2)) {
      return 1;
    }
    if (Zodiac.isBetween(position1, sign + 30, position2)) {
      return 1;
    }

    return 0;
  }

  protected String toStringBeforeFactor(Criterion criterion) {
    return Text.getText("in");
  }

}
