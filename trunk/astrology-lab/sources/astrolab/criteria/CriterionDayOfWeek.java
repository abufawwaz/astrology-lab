package astrolab.criteria;

import astrolab.astronom.SpacetimeEvent;
import astrolab.astronom.planet.SolarSystem;
import astrolab.db.Text;

public class CriterionDayOfWeek extends CriterionAlgorithm {

  private final static int SUNDAY = Text.getId(SolarSystem.SUN);
  private final static int MONDAY = Text.getId(SolarSystem.MOON);
  private final static int TUESDAY = Text.getId(SolarSystem.MARS);
  private final static int WEDNESDAY = Text.getId(SolarSystem.MERCURY);
  private final static int THURSDAY = Text.getId(SolarSystem.JUPITER);
  private final static int FRIDAY = Text.getId(SolarSystem.VENUS);
  private final static int SATURDAY = Text.getId(SolarSystem.SATURN);

  CriterionDayOfWeek() {
    super(CriterionAlgorithm.ALGORITHM_DAY_OF_WEEK);
  }

  public boolean accepts(Criterion criterion) {
    if (criterion.getAction() != 0) {
      return false;
    }
    if (criterion.getFactor() != 0) {
      return false;
    }
    return true;
  }

  public String[] getActorTypes() {
    return new String[] { Criterion.TYPE_PLANET };
  }

  public int calculateMark(Criterion criterion, SpacetimeEvent periodStart, SpacetimeEvent periodEnd) {
    int ruler = criterion.getActor();
    int weekDay = periodStart.get(SpacetimeEvent.DAY_OF_WEEK);

    if (ruler == SUNDAY) {
      return (weekDay == 1) ? 1 : 0;
    } else if (ruler == MONDAY) {
      return (weekDay == 2) ? 1 : 0;
    } else if (ruler == TUESDAY) {
      return (weekDay == 3) ? 1 : 0;
    } else if (ruler == WEDNESDAY) {
      return (weekDay == 4) ? 1 : 0;
    } else if (ruler == THURSDAY) {
      return (weekDay == 5) ? 1 : 0;
    } else if (ruler == FRIDAY) {
      return (weekDay == 6) ? 1 : 0;
    } else if (ruler == SATURDAY) {
      return (weekDay == 7) ? 1 : 0;
    }
    return 0;
  }

  protected String toStringAfterActor(Criterion criterion) {
    return Text.getText("rules the day of week");
  }

}
