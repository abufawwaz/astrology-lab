package astrolab.astronom;

import java.util.Hashtable;

import astrolab.astronom.planet.PlanetSystemPool;
import astrolab.db.Event;

public abstract class ActivePoint {

  protected final static int SYSTEM_UNKNOWN = -1;
  protected final static int SYSTEM_PLANET = 0;
  protected final static int SYSTEM_HOUSE = 1;
  protected final static int SYSTEM_OTHER = 2;

  private int id;
  private Event birth;

  private static Hashtable<Integer, Integer> SYSTEM_ID = new Hashtable<Integer, Integer>();
  private static Hashtable<String, Integer> SYSTEM_NAME = new Hashtable<String, Integer>();

  static {
    PlanetSystemPool.getPlanetSystems(); // initializes the solar system
  }

  protected ActivePoint(int id) {
    this.id = id;

    SYSTEM_ID.put(id, getSystemType());
    SYSTEM_NAME.put(getName(), getSystemType());
  }

  public int getId() {
    return id;
  }

  // TODO: is this ok?
  public void initialize(Event birth) {
		this.birth = birth;
	}

  protected abstract int getSystemType();

  public abstract String getIcon();

	public abstract String getName();

	public abstract double getPosition();

  // TODO: is this ok?
	protected Event getBirth() {
		return birth;
	}

  public static ActivePoint getActivePoint(int id, SpacetimeEvent time) {
    int system = (SYSTEM_ID.containsKey(id)) ? SYSTEM_ID.get(id) : SYSTEM_UNKNOWN;

    switch (system) {
      case SYSTEM_PLANET: {
        return PlanetSystemPool.getPlanetSystems().getPlanetSystem(time).getPlanet(id);
      }
      case SYSTEM_HOUSE: {
// TODO: fix it        return new PlacidusSystem(time).getHouse(id);
        return null;
      }
      default: {
        throw new IllegalStateException("No active point with id " + id);
      }
    }
  }

  public static ActivePoint getActivePoint(String name, SpacetimeEvent time) {
    switch (SYSTEM_NAME.get(name)) {
      case SYSTEM_PLANET: {
        return PlanetSystemPool.getPlanetSystems().getPlanetSystem(time).getPlanet(name);
      }
      case SYSTEM_HOUSE: {
        throw new IllegalStateException("House cannot be accessed by name.");
      }
      default: {
        throw new IllegalStateException("No active point with name " + name);
      }
    }
  }

}
