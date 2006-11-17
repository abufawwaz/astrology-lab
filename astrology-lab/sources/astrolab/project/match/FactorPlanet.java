package astrolab.project.match;

import astrolab.astronom.util.Zodiac;

public class FactorPlanet extends Factor {

  public FactorPlanet(String name, double generalPower) {
    super(name, generalPower);
  }

  public final boolean isPlanet() {
    return true;
  }

//  protected double calculateOwnPower() {
//    FactorHouse house = getHouse();
//
//    double powerOfHouse = house.getGeneralPower();
//    double positionInHouse = 1.0 - Zodiac.degree(getPosition() - house.getPosition()) / house.getSize();
//
//    return powerOfHouse * positionInHouse;
//  }
//
//  public FactorHouse getHouse() {
//    Factor[] factors = getFactors().toArray();
//    for (int i = 0; i < factors.length; i++) {
//      if (factors[i] instanceof FactorHouse) {
//        FactorHouse house = (FactorHouse) factors[i];
//
//        if (Zodiac.isBetween(house.getPosition(), getPosition(), house.getPosition() + house.getSize())) {
//          return house;
//        }
//      }
//    }
//    return null;
//  }

}
