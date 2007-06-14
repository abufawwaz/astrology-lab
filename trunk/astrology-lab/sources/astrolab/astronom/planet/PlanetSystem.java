package astrolab.astronom.planet;

import java.util.*;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.Time;

public class PlanetSystem {

  protected Hashtable<String, ActivePoint> planets = new Hashtable<String, ActivePoint>();
  protected Hashtable<Integer, ActivePoint> planetIndex = new Hashtable<Integer, ActivePoint>();

  protected double standardYearTime;

  protected PlanetSystem(Calendar calendar) {
    standardYearTime = new Time(calendar.getTime()).getStandardYearTime();
  }

  public Enumeration getPlanetNames() {
    return planets.keys();
  }

  protected void registerPlanet(int id, Planet planet) {
    planetIndex.put(id, planet);
  }

  public Planet getPlanet(int id) {
    return (Planet) planetIndex.get(id);
  }

  public Planet getPlanet(String name) {
    return (Planet) planets.get(name);
  }

}