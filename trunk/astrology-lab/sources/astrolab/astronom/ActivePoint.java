package astrolab.astronom;

import java.util.Calendar;
import java.util.Enumeration;

import astrolab.astronom.planet.SolarSystem;
import astrolab.astronom.track.ActivePointTrajectory;
import astrolab.db.Event;
import astrolab.db.Text;

public abstract class ActivePoint {

	private Event birth;

	public void initialize(Event birth) {
		this.birth = birth;
	}

	public abstract String getIcon();

	public abstract String getName();

	public abstract double getPosition(Calendar calendar);

	public abstract ActivePointTrajectory getTrajectory(Calendar start, Calendar end);

	protected Event getBirth() {
		return birth;
	}

  public static ActivePoint getActivePoint(int id) {
    SolarSystem solar = new SolarSystem();
    Enumeration planets = solar.getPlanetNames();
    while (planets.hasMoreElements()) {
      String key = (String) planets.nextElement();
      if (id == Text.getId(key)) {
        return solar.getPlanet(key);
      }
    }
    return null;
  }

}
