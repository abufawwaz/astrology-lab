package astrolab.project.match;

import astrolab.astronom.planet.SolarSystem;

public class FactorHouse extends Factor {

  private double size = 0;

  public FactorHouse(String name, double generalPower) {
    super(name, generalPower);
  }

  public FactorHouse(String name, double generalPower, double size) {
    super(name, generalPower);

    this.size = size;
  }

  public final boolean isPlanet() {
    return false;
  }

  public double getSize() {
    return size;
  }

  public boolean containsPlanet(FactorPlanet planet) {
    double p = planet.getPosition() + ((planet.getPosition() < this.getPosition()) ? 360 : 0);
    if (p < this.getPosition() + this.getSize()) {
      // the planet is between the cusp end end of the house 
      return true;
    } else if (Math.abs(planet.getPosition() - this.getPosition()) < ((("House 1".equals(this.getName())) || ("House 10".equals(this.getName()))) ? 5 : 3)) {
      // TODO: if Aries 0 is in between this will not return true
      // the planet is on the cusp
      return true;
    } else {
      return false;
    }
  }

  public boolean isRuledBy(FactorPlanet planet) {
    int sign = (int) (this.getPosition() % 30);
    switch (sign) {
    case 0: {
      return SolarSystem.MARS.equals(planet.getName());
    }
    case 1: {
      return SolarSystem.VENUS.equals(planet.getName());
    }
    case 2: {
      return SolarSystem.MERCURY.equals(planet.getName());
    }
    case 3: {
      return SolarSystem.MOON.equals(planet.getName());
    }
    case 4: {
      return SolarSystem.SUN.equals(planet.getName());
    }
    case 5: {
      return SolarSystem.MERCURY.equals(planet.getName());
    }
    case 6: {
      return SolarSystem.VENUS.equals(planet.getName());
    }
    case 7: {
      return SolarSystem.MARS.equals(planet.getName()) || SolarSystem.PLUTO.equals(planet.getName());
    }
    case 8: {
      return SolarSystem.JUPITER.equals(planet.getName());
    }
    case 9: {
      return SolarSystem.SATURN.equals(planet.getName());
    }
    case 10: {
      return SolarSystem.SATURN.equals(planet.getName()) || SolarSystem.URANUS.equals(planet.getName());
    }
    case 11: {
      return SolarSystem.JUPITER.equals(planet.getName()) || SolarSystem.NEPTUNE.equals(planet.getName());
    }
    }
    return false;
  }

}
