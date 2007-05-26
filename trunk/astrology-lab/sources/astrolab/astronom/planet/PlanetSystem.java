package astrolab.astronom.planet;

import java.util.*;

import astrolab.db.*;
import astrolab.astronom.Time;
import astrolab.astronom.util.Zodiac;

public class PlanetSystem {

  protected Hashtable planets = new Hashtable();

  private double standardYearTime = -1;
  private Vector waiting = new Vector();

  public PlanetSystem() {
  }

  public Enumeration getPlanetNames() {
    return planets.keys();
  }

  public Planet getPlanet(String name) {
    return (Planet) planets.get(name);
  }

  public void calculate(Event event) {
    calculate(event.getTime());
  }

  public void calculate(Time time) {
    calculate(time.getStandardYearTime());
  }

  public void calculate(Calendar calendar) {
    calculate(new Time(calendar.getTime()));
  }

  public void calculate(double standardYearTime) {
    if (standardYearTime == this.standardYearTime) {
      return;
    }

    Enumeration objects = planets.elements();
    Planet planet;
    boolean goon = true;

    waiting.clear();

    while (objects.hasMoreElements()) {
      planet = (Planet) objects.nextElement();
      planet.markToBePositioned();
      waiting.add(planet);
    }

    while (goon) {
      goon = false;

      for (int i = 0; i < waiting.size(); i++) {
        planet = (Planet) waiting.elementAt(i);

        if (planet.position(standardYearTime)) {
          waiting.remove(planet);
          goon = true;
        }
      }
    }

    if (waiting.size() > 0) {
      System.err.println(" Unable to calculate position of planets. Circular dependencies.");
    }

    waiting.clear();
    this.standardYearTime = standardYearTime;
  }

  // get rid of this
  public void dumpZodiac(Event event) {
    Planet center = getPlanet(SolarSystem.EARTH);
    calculate(event);

    Enumeration objects = planets.elements();
    Planet planet;

    while (objects.hasMoreElements()) {
      planet = (Planet) objects.nextElement();
      System.out.println(" planet " + planet.getName() + " :  " + Zodiac.toString(planet.positionAround(center), "DD ZZZ MM SS") + " - " + planet.positionAround(center));
    }
  }

}