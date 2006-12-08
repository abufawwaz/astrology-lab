package astrolab.formula;

import astrolab.astronom.houses.HouseSystem;
import astrolab.astronom.planet.Planet;
import astrolab.astronom.planet.SolarSystem;
import astrolab.db.Text;

public final class Element {

  private final static double[] COEFINDEX = new double[] {
    0, 1, - 1, 1.0 / 2, -1.0 / 2, 1.0 / 3, 2.0 / 3, 1.0 / 4, 3.0 / 4,
    1.0 / 5, 2.0 / 5, 3.0 / 5, 4.0 / 5, 1.0 / 6, 5.0 / 6,
    1.0 / 7, 2.0 / 7, 3.0 / 7, 4.0 / 7, 5.0 / 7, 6.0 / 7,
    1.0 / 8, 3.0 / 8, 5.0 / 8, 7.0 / 8,
    1.0 / 9, 2.0 / 9, 4.0 / 9, 5.0 / 9, 7.0 / 9, 8.0 / 9,
    1.0 / 10, 3.0 / 10, 7.0 / 10, 9.0 / 10,
    1.0 / 11, 2.0 / 11, 3.0 / 11, 4.0 / 11, 5.0 / 11, 6.0 / 11, 7.0 / 11, 8.0 / 11, 9.0 / 11, 10.0 / 11
  };

  private int id;
  private int coeffindex = 0;
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
    return (coefficient == Double.NaN) ? coefficient : COEFINDEX[coeffindex];
  }

  public int getCoefficientIndex() {
    return coeffindex;
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

  void setCoefficient(double coefficient) {
    this.coefficient = coefficient;
  }

  void setCoefficientIndex(int index) {
    this.coeffindex = index;
  }

  public String toString() {
    return text;
  }

}