package astrolab.astronom;

import java.util.Calendar;

import astrolab.astronom.planet.PlanetSystemPool;
import astrolab.astronom.track.ActivePointTrajectory;
import astrolab.db.Event;

public abstract class ActivePoint {

  public final static Calendar ZERO_TIME = Calendar.getInstance();

  private int id;
  private Event birth;

  protected ActivePoint(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public void initialize(Event birth) {
		this.birth = birth;
	}

	public abstract String getIcon();

	public abstract String getName();

	public abstract double getPosition();

	public abstract ActivePointTrajectory getTrajectory(Calendar start, Calendar end);

	protected Event getBirth() {
		return birth;
	}

  public static ActivePoint getActivePoint(int id, Calendar calendar) {
    return PlanetSystemPool.getPlanetSystems().getPlanetSystem(calendar).getPlanet(id);
  }

  public static ActivePoint getActivePoint(String name, Calendar calendar) {
    return PlanetSystemPool.getPlanetSystems().getPlanetSystem(calendar).getPlanet(name);
  }

}
