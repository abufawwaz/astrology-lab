package astrolab.formula;

import astrolab.astronom.houses.HouseSystem;
import astrolab.astronom.planet.Planet;
import astrolab.astronom.planet.SolarSystem;
import astrolab.db.Text;

public class Element {

  private int id;
  private double coefficient = Double.NaN;

  private String text;

  public Element(int id) {
    this.id = id;
    this.text = Text.getDescriptiveId(id);
  }

  public Element(int id, double coefficient) {
    this(id);
    this.coefficient = coefficient;
  }

  public double getCoefficient() {
    return coefficient;
  }

  public int getId() {
    return id;
  }

  public double getPosition(SolarSystem solar, HouseSystem houses, Planet center) {
    Planet planet = solar.getPlanet(text);
    if (planet != null) {
      return (planet != center) ? planet.positionAround(center) : 0;
    } else {
      throw new RuntimeException("houses.getHouse(x) not implemented yet!");
    }
  }

  public String getText() {
    return text;
  }

  void setCoefficient(double coefficient) {
    this.coefficient = coefficient;
  }

  public String toString() {
    if (Double.isNaN(coefficient)) {
      return text;
    } else if (coefficient >= 0) {
      return "+" + coefficient + "x" + text;
    } else {
      return coefficient + "x" + text;
    }
  }

}