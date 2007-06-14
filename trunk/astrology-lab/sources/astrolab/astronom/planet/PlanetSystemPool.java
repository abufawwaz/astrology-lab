package astrolab.astronom.planet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

public class PlanetSystemPool {

  private Hashtable<Calendar, PlanetSystem> pool = new Hashtable<Calendar, PlanetSystem>();
  private ArrayList<Calendar> index = new ArrayList<Calendar>();

  private final static PlanetSystemPool POOL = new PlanetSystemPool();

  private PlanetSystemPool() {
  }

  public PlanetSystem getPlanetSystem(Calendar calendar) {
    PlanetSystem result = pool.get(calendar);

    if (result == null) {
      result = new SolarSystem(calendar);
      pool.put(calendar, result);
      index.add(0, calendar);

      if (index.size() > 300) {
        synchronized (index) {
          while (index.size() > 300) {
            Calendar c = index.remove(300);
            if (c != null) {
              pool.remove(c);
            }
          }
        }
      }
    } else {
      synchronized (index) {
        index.remove(calendar);
        index.add(0, calendar);
      }
    }

    return result;
  }

  public static PlanetSystemPool getPlanetSystems() {
    return POOL;
  }

}