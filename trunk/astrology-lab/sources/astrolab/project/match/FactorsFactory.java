package astrolab.project.match;

import astrolab.astronom.planet.SolarSystem;
import astrolab.db.Event;

public class FactorsFactory {

  private static Factor[] maleFactors;
  private static Factor[] femaleFactors;

  static {
    maleFactors = new Factor[] {
        new FactorPlanet(SolarSystem.SUN, 20),
        new FactorPlanet(SolarSystem.MOON, 15),
        new FactorPlanet(SolarSystem.MERCURY, 10),
        new FactorPlanet(SolarSystem.VENUS, 10),
        new FactorPlanet(SolarSystem.MARS, 15),
        new FactorPlanet(SolarSystem.JUPITER, 5),
        new FactorPlanet(SolarSystem.SATURN, 5),
        new FactorPlanet(SolarSystem.URANUS, 2),
        new FactorPlanet(SolarSystem.NEPTUNE, 2),
        new FactorPlanet(SolarSystem.PLUTO, 2),
        new FactorHouse("House 1", 8),
        new FactorHouse("House 2", 5),
        new FactorHouse("House 3", 6),
        new FactorHouse("House 4", 5),
        new FactorHouse("House 5", 8),
        new FactorHouse("House 6", 6),
        new FactorHouse("House 7", 10),
        new FactorHouse("House 8", 5),
        new FactorHouse("House 9", 5),
        new FactorHouse("House 10", 5),
        new FactorHouse("House 11", 5),
        new FactorHouse("House 12", 5)
    };
    femaleFactors = new Factor[] {
        new FactorPlanet(SolarSystem.SUN, 15),
        new FactorPlanet(SolarSystem.MOON, 20),
        new FactorPlanet(SolarSystem.MERCURY, 10),
        new FactorPlanet(SolarSystem.VENUS, 15),
        new FactorPlanet(SolarSystem.MARS, 10),
        new FactorPlanet(SolarSystem.JUPITER, 5),
        new FactorPlanet(SolarSystem.SATURN, 5),
        new FactorPlanet(SolarSystem.URANUS, 2),
        new FactorPlanet(SolarSystem.NEPTUNE, 2),
        new FactorPlanet(SolarSystem.PLUTO, 2),
        new FactorHouse("House 1", 8),
        new FactorHouse("House 2", 5),
        new FactorHouse("House 3", 6),
        new FactorHouse("House 4", 5),
        new FactorHouse("House 5", 8),
        new FactorHouse("House 6", 6),
        new FactorHouse("House 7", 10),
        new FactorHouse("House 8", 5),
        new FactorHouse("House 9", 5),
        new FactorHouse("House 10", 5),
        new FactorHouse("House 11", 5),
        new FactorHouse("House 12", 5)
    };
  }

  public static Factors getFactors(Event event) {
    Factor[] factors = (Event.TYPE_MALE.equals(event.getType())) ? maleFactors : femaleFactors;

    return new Factors(factors, event);
  }

}
