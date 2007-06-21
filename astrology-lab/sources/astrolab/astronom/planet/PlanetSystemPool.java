package astrolab.astronom.planet;

import java.util.Hashtable;
import java.util.LinkedList;

import astrolab.astronom.SpacetimeEvent;

public class PlanetSystemPool {

  private long puts = 0;
  private long gets = 0;
  private long cleans = 0;
  private Hashtable<SpacetimeEvent, PlanetSystem> pool = new Hashtable<SpacetimeEvent, PlanetSystem>();
  private LinkedList<SpacetimeEvent> index = new LinkedList<SpacetimeEvent>();

  private final static PlanetSystemPool POOL = new PlanetSystemPool();
  private final static int POOL_SIZE = 1000;

  private PlanetSystemPool() {
    getPlanetSystem(new SpacetimeEvent(System.currentTimeMillis()));
  }

  public synchronized PlanetSystem getPlanetSystem(SpacetimeEvent spacetime) {
    PlanetSystem result = pool.get(spacetime);

    if (result == null) {
      puts++;
      result = new SolarSystem(spacetime);
      pool.put(spacetime, result);
      index.addFirst(spacetime);

      cleanPool();
    } else {
      gets++;
      index.remove(spacetime);
      index.addFirst(spacetime);
    }

    return result;
  }

  public static PlanetSystemPool getPlanetSystems() {
    return POOL;
  }

  public final synchronized void cleanPool() {
    int indexSize = index.size();
    if (indexSize > POOL_SIZE) {
      for (int i = (int) (POOL_SIZE * 0.75); i < indexSize; i++) {
        pool.remove(index.removeLast());
      }
      cleans++;
    }
  }

  public String toString() {
    double hitRatio = 1;
    if (puts > 0) {
      hitRatio = ((double) gets) / (gets + puts);
    }
    return "Planet System Pool: hit ratio: " + hitRatio + " index: " + index.size() + " cleans: " + cleans;
  }
}