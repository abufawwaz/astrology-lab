package astrolab.astronom.planet;

import java.util.Calendar;

public class SolarSystem extends PlanetSystem {

  public static final String SUN = "Sun";
  public static final String MOON = "Moon";
  public static final String MERCURY = "Mercury";
  public static final String VENUS = "Venus";
  public static final String EARTH = "Earth";
  public static final String MARS = "Mars";
  public static final String JUPITER = "Jupiter";
  public static final String SATURN = "Saturn";
  public static final String URANUS = "Uranus";
  public static final String NEPTUNE = "Neptune";
  public static final String PLUTO = "Pluto";

  public static final String[] PLANETS = {
    SUN, MOON, MERCURY, VENUS, EARTH, MARS, JUPITER, SATURN, URANUS, NEPTUNE, PLUTO
  };

  public SolarSystem(Calendar calendar) {
    super(calendar);

    planets.put(EARTH, new Earth(this));
    planets.put(SUN, new Sun(this));
    planets.put(MOON, new Moon(this));
    planets.put(MERCURY, new Mercury(this));
    planets.put(VENUS, new Venus(this));
    planets.put(MARS, new Mars(this));
    planets.put(JUPITER, new Jupiter(this));
    planets.put(SATURN, new Saturn(this));
    planets.put(URANUS, new Uranus(this));
    planets.put(NEPTUNE, new Neptune(this));
    planets.put(PLUTO, new Pluto(this));
  }

  public Planet getPlanet(String name) {
    return (Planet) planets.get(name);
  }

}