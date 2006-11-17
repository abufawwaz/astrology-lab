package astrolab.project.match;

import astrolab.db.Event;

public class Factors {

  private boolean isRelativeGeneralInfluenceCalculated = false;
  private boolean isRelativeIndividualInfluenceCalculated = false;
  private boolean isRelativeImageCalculated = false;
  private boolean isRelativeRealityCalculated = false;

  private Factor[] factors;

  Factors(Factor[] raw_factors, Event event) {
    factors = new Factor[raw_factors.length];

    for (int i = 0; i < factors.length; i++) {
      factors[i] = (Factor) raw_factors[i].copy(event, this);
    }
  }

  public void calculateRelativeGeneralInfluence() {
    if (!isRelativeGeneralInfluenceCalculated) {
      double sum = 0.0;
      for (int i = 0; i < factors.length; i++) {
        sum += factors[i].getGeneralInfluence();
      }
      for (int i = 0; i < factors.length; i++) {
        factors[i].relativeGeneralInfluence = factors[i].getGeneralInfluence() / sum;
      }
      isRelativeGeneralInfluenceCalculated = true;
    }
  }

  public void calculateRelativeIndividualInfluence() {
    if (!isRelativeIndividualInfluenceCalculated) {
      double sum = 0.0;
      for (int i = 0; i < factors.length; i++) {
        sum += factors[i].getIndividualInfluence();
      }
      for (int i = 0; i < factors.length; i++) {
        factors[i].relativeIndividualInfluence = factors[i].getIndividualInfluence() / sum;
      }
      isRelativeIndividualInfluenceCalculated = true;
    }
  }

  public void calculateRelativeImageOfPartner(Factors partner) {
    if (!isRelativeImageCalculated) {
      double sum = 0.0;
      for (int i = 0; i < factors.length; i++) {
        sum += factors[i].getImageOfPartner(partner);
      }
      for (int i = 0; i < factors.length; i++) {
        factors[i].relativeImageOfPartner = factors[i].getImageOfPartner(partner) / sum;
      }
      isRelativeImageCalculated = true;
    }
  }

  public void calculateRelativeRealityOfPartner(Factors partner) {
    if (!isRelativeRealityCalculated) {
      double sum = 0.0;
      for (int i = 0; i < factors.length; i++) {
        sum += factors[i].getRealityOfPartner(partner);
      }
      for (int i = 0; i < factors.length; i++) {
        factors[i].relativeRealityOfPartner = factors[i].getRealityOfPartner(partner) / sum;
      }
      isRelativeRealityCalculated = true;
    }
  }

  public Factor[] toArray() {
    return factors;
  }

}
