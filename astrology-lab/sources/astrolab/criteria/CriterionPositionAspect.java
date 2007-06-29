package astrolab.criteria;

import astrolab.astronom.Aspects;
import astrolab.astronom.SpacetimeEvent;
import astrolab.web.server.content.LocalizedStringBuffer;

public class CriterionPositionAspect extends Criterion {

  public CriterionPositionAspect() {
    super();
  }

  public CriterionPositionAspect(int id, int activePoint, int passivePlanet, int aspect) {
    super(id, TYPE_POSITION_PLANET_ASPECT, activePoint, passivePlanet, aspect);
  }

  public int getMark(SpacetimeEvent periodStart, SpacetimeEvent periodEnd) {
    int aspect = CriteriaUtilities.hasAspectsBetween(getActor(), getFactor(), periodStart, periodEnd);

    if (getAction() == Aspects.CONJUNCT) {
      if (aspect == 0) {
        return 1;
      }
    } else if (getAction() == Aspects.SEXTILE) {
      if (aspect == 60) {
        return 1;
      }
    } else if (getAction() == Aspects.SQUARE) {
      if (aspect == 90) {
        return 1;
      }
    } else if (getAction() == Aspects.TRINE) {
      if (aspect == 120) {
        return 1;
      }
    } else if (getAction() == Aspects.OPPOSITION) {
      if (aspect == 180) {
        return 1;
      }
    }

    return 0;
  }

  public String getName() {
    return "PositionPlanetAspect";
  }

  public String[] getActorTypes() {
    return new String[] { "Planet", "'position'", "Aspect", "Planet" };
  }

  protected void store(String[] inputValues) {
    int active = Integer.parseInt(inputValues[0]);
    int aspect = Integer.parseInt(inputValues[2]);
    int passive = Integer.parseInt(inputValues[3]);
    new CriterionPositionAspect(getId(), active, passive, aspect).store();
  }

  public void toString(LocalizedStringBuffer output) {
    output.localize(getActor());
    output.append(" ");
    output.localize(getAction());
    output.append(" ");
    output.localize(getFactor());
  }

}
