package astrolab.perspective.election;

import java.util.Calendar;

import astrolab.astronom.SpacetimeEvent;
import astrolab.db.Personalize;

public class ElectiveEnvironment {

  protected final static void moveStartingTime(int monthDirection) {
    SpacetimeEvent now = getStartingTime().getMovedSpacetimeEvent(SpacetimeEvent.MONTH, monthDirection);
    Personalize.addFavourite(DisplayElectionaryCriteriaList.ID, now.get(SpacetimeEvent.YEAR), 0);
    Personalize.addFavourite(DisplayElectionaryCriteriaList.ID, now.get(SpacetimeEvent.MONTH) + 1, 1);
  }

  protected final static SpacetimeEvent getStartingTime() {
    Calendar now = Calendar.getInstance();
    int year = Personalize.getFavourite(DisplayElectionaryCriteriaList.ID, 0, now.get(SpacetimeEvent.YEAR));
    int month = Personalize.getFavourite(DisplayElectionaryCriteriaList.ID, 1, now.get(SpacetimeEvent.MONTH));

    now.set(Calendar.YEAR, year);
    now.set(Calendar.MONTH, month);
    now.set(Calendar.DAY_OF_MONTH, 0);
    now.set(Calendar.HOUR_OF_DAY, 0);
    now.set(Calendar.MINUTE, 0);
    now.set(Calendar.SECOND, 0);
    now.set(Calendar.MILLISECOND, 0);

    return new SpacetimeEvent(now.getTimeInMillis());
  }

}