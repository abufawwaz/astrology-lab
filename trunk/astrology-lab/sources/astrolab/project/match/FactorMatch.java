package astrolab.project.match;

import astrolab.astronom.util.Zodiac;

public class FactorMatch {

  public static double getMatch(Factor f1, Factor f2) {
    if (f1 instanceof FactorPlanet) {
      if (f2 instanceof FactorPlanet) {
        return getMatch((FactorPlanet) f1, (FactorPlanet) f2);
      } else {
        return getMatch((FactorPlanet) f1, (FactorHouse) f2);
      }
    } else {
      if (f2 instanceof FactorPlanet) {
        return getMatch((FactorHouse) f1, (FactorPlanet) f2);
      } else {
        return getMatch((FactorHouse) f1, (FactorHouse) f2);
      }
    }
  }

  public static double getMatch(FactorPlanet p1, FactorPlanet p2) {
    return getAspect(p1, p2);
  }

  public static double getMatch(FactorPlanet planet, FactorHouse house) {
    double positionInHouse = 0;
    if (house.containsPlanet(planet)) {
      if (planet.getPosition() > house.getPosition() - 30) {
        positionInHouse = 15.0 - Zodiac.degree(Math.abs(planet.getPosition() - house.getPosition())) / house.getSize() * 10;
      } else {
        positionInHouse = 15.0 - Zodiac.degree(Math.abs(planet.getPosition() + 360 - house.getPosition())) / house.getSize() * 10;
      }
    }

    // TODO: ruling planet should get a bonus
    // TODO: planet with aspect to house cusp should get a bonus

    double aspect = (positionInHouse > 0) ? 0.0 : getAspect(planet, house) / 5;

    double rulership = house.isRuledBy(planet) ? 10.0 : 0.0;

    double result = positionInHouse + aspect + rulership;
    return result;
  }

  public static double getMatch(FactorHouse house, FactorPlanet planet) {
    return getMatch(planet, house);
  }

  public static double getMatch(FactorHouse h1, FactorHouse h2) {
    // the houses do not have match bonuses
    return 0.0;
  }

  // TODO: get this from the database
  public static double getMatch(int sign1, int sign2) {
    double result = 0;
    if ((sign1 % 4) == (sign2 % 4)) {
      result += 5;
    }
    if ((sign1 % 3) == (sign2 % 3)) {
      result += 3;
    }
    if ((sign1 % 2) == (sign2 % 2)) {
      result += 2;
    }
    return result;
  }

  private static double getAspect(Factor f1, Factor f2) {
    double distance = Math.abs(f1.getPosition() - f2.getPosition());
    int sign_distance = (int) ((distance / 30) + (((distance % 30) >= 15) ? 1 : 0));
    double raw_power = getMatch(0, sign_distance);
    double coefficient = (10 - (distance % 30));

    coefficient = Math.max(coefficient, 0) / 3;

    return raw_power * coefficient;
  }

}
