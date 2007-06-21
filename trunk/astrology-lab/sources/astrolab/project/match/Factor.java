package astrolab.project.match;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.houses.HouseSystem;
import astrolab.astronom.houses.PlacidusSystem;
import astrolab.astronom.util.Zodiac;
import astrolab.db.Event;

public abstract class Factor {

  private String name;
  private double position;

  private double generalInfluence;
  double relativeGeneralInfluence = -1.0;

  private double individualInfluence = -1.0;
  double relativeIndividualInfluence = -1.0;

  private double imageOfPartner = -1.0;
  double relativeImageOfPartner = -1.0;

  private double realityOfPartner = -1.0;
  double relativeRealityOfPartner = -1.0;

  private Factors factors;

  public Factor(String name, double generalPower) {
    this.name = name;
    this.generalInfluence = generalPower;
  }

  public final String getName() {
    return name;
  }

  public final double getGeneralInfluence() {
    return generalInfluence;
  }

  public final double getRelativeGeneralInfluence() {
    factors.calculateRelativeGeneralInfluence();
    return relativeGeneralInfluence;
  }

  public final double getIndividualInfluence() {
    if (individualInfluence < 0.0) {
      double result = 0.0;
      Factor[] array = factors.toArray();

      for (int i = 0; i < array.length; i++) {
        if (array[i] != this) {
          result += array[i].getGeneralInfluence() * FactorMatch.getMatch(this, array[i]);
        }
      }

      individualInfluence = result * getGeneralInfluence();
    }
    return individualInfluence;
  }

  public final double getRelativeIndividualInfluence() {
    factors.calculateRelativeIndividualInfluence();
    return relativeIndividualInfluence;
  }

  public double getVisionOfPartner(Factors partner) {
    return getIndividualInfluence();
  }

  public final double getRelativeVisionOfPartner(Factors partner) {
    return getRelativeIndividualInfluence();
  }

  public double getImageOfPartner(Factors partner) {
    if (imageOfPartner < 0.0) {
      double result = 0.0;
      Factor[] self = factors.toArray();
      Factor[] array = partner.toArray();

      for (int i = 0; i < array.length; i++) {
        result += self[i].getRelativeIndividualInfluence() * 100 * FactorMatch.getMatch(array[i], this);
      }

      imageOfPartner = result * getRelativeIndividualInfluence() * 100;
    }
    return imageOfPartner;
  }

  public final double getRelativeImageOfPartner(Factors partner) {
    factors.calculateRelativeImageOfPartner(partner);
    return relativeImageOfPartner;
  }

  public double getRealityOfPartner(Factors partner) {
    if (realityOfPartner < 0.0) {
      double result = 0.0;
      Factor[] array = partner.toArray();
  
      for (int i = 0; i < array.length; i++) {
        result += array[i].getIndividualInfluence() * FactorMatch.getMatch(array[i], this);
      }
  
      realityOfPartner = result * getRelativeImageOfPartner(partner);
    }

    return realityOfPartner;
  }

  public final double getRelativeRealityOfPartner(Factors partner) {
    factors.calculateRelativeRealityOfPartner(partner);
    return relativeRealityOfPartner;
  }

  public double getVisionVsImage(Factors partner) {
    // TODO: the relative value extracts the essence and loses the dominance ofone potential partner over another
    // return this.getRelativeVisionOfPartner(partner) * 100 * this.getRelativeImageOfPartner(partner) * 100;
    return this.getVisionOfPartner(partner) / 100 * this.getImageOfPartner(partner) / 100;
  }

  public double getImageVsReality(Factors partner) {
    // TODO: the relative value extracts the essence and loses the dominance ofone potential partner over another
    // return this.getRelativeImageOfPartner(partner) * 100 * this.getRelativeRealityOfPartner(partner) * 100;
    return this.getImageOfPartner(partner) / 100 * this.getRealityOfPartner(partner) / 100;
  }

  public final double getPosition() {
    return position;
  }

  protected final Factors getFactors() {
    return factors;
  }

  public abstract boolean isPlanet();

  Factor copy(Event event, Factors factors) {
    throw new IllegalStateException("TODO: fix this");
//    Factor factor;
//
//    if (isPlanet()) {
//      factor = new FactorPlanet(name, generalInfluence);
//
//      factor.position = ActivePoint.getActivePoint(name, event.getTime()).getPosition();
//    } else {
//
//      int index = Integer.parseInt(name.substring(name.indexOf(" ") + 1));
//      HouseSystem houses = new PlacidusSystem(event);
//
//      double start = houses.getHouse(index);
//      double size = houses.getHouse(index + ((index < 12) ? 1 : -11));
//      factor = new FactorHouse(name, generalInfluence, Zodiac.degree(size - start));
//      factor.position = start;
//    }
//
//    factor.factors = factors;
//    return factor;
  }

  public String toString() {
    return getName();
  }
  
}
