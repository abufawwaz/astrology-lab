package astrolab.criteria;

import astrolab.astronom.SpacetimeEvent;
import astrolab.astronom.planet.SolarSystem;
import astrolab.db.Text;
import astrolab.web.server.content.LocalizedStringBuffer;

public class CriterionTimeOfWeek extends Criterion {

  private final static int SUNDAY = Text.getId(SolarSystem.SUN);
  private final static int MONDAY = Text.getId(SolarSystem.MOON);
  private final static int TUESDAY = Text.getId(SolarSystem.MARS);
  private final static int WEDNESDAY = Text.getId(SolarSystem.MERCURY);
  private final static int THURSDAY = Text.getId(SolarSystem.JUPITER);
  private final static int FRIDAY = Text.getId(SolarSystem.VENUS);
  private final static int SATURDAY = Text.getId(SolarSystem.SATURN);

  CriterionTimeOfWeek() {
    super();
  }

  public CriterionTimeOfWeek(int id, int weekDayRuler) {
    super(id, Criterion.TYPE_TIME_OF_WEEK, weekDayRuler);
  }

  public int getMark(SpacetimeEvent periodStart, SpacetimeEvent periodEnd) {
    int ruler = getActor();
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

  public String getName() {
    return "TimeOfWeek";
  }

  public String[] getActorTypes() {
    return new String[] { "'weekday'", "Planet" };
  }

  protected void store(String[] inputValues) {
    new CriterionTimeOfWeek(getId(), Integer.parseInt(inputValues[1])).store();
  }

  public void toString(LocalizedStringBuffer output) {
    int ruler = getActor();

    output.localize("Week day");
    output.append(" ");

    if (ruler == SUNDAY) {
      output.localize("Sunday");
    } else if (ruler == MONDAY) {
      output.localize("Monday");
    } else if (ruler == TUESDAY) {
      output.localize("Tuesday");
    } else if (ruler == WEDNESDAY) {
      output.localize("Wednesday");
    } else if (ruler == THURSDAY) {
      output.localize("Thursday");
    } else if (ruler == FRIDAY) {
      output.localize("Friday");
    } else if (ruler == SATURDAY) {
      output.localize("Saturday");
    } else {
      output.localize("unknown");
    }
  }

}
