package astrolab.astronom.planet;

import java.util.Enumeration;
import java.util.Hashtable;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.SpacetimeEvent;

public class PlanetSystem {

  protected Hashtable<String, ActivePoint> planets = new Hashtable<String, ActivePoint>();
  protected Hashtable<Integer, ActivePoint> planetIndex = new Hashtable<Integer, ActivePoint>();

  protected SpacetimeEvent spacetimeEvent;

  protected PlanetSystem(SpacetimeEvent spacetime) {
    spacetimeEvent = spacetime;
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