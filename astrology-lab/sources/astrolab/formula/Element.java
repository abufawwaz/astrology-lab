package astrolab.formula;

import astrolab.astronom.houses.HouseSystem;
import astrolab.astronom.planet.Planet;
import astrolab.astronom.planet.SolarSystem;
import astrolab.db.Text;

public final class Element {

  private int id;
  private double coefficient;

  private String text;

  public Element(int id, double coefficient) {
    this.id = id;
    this.coefficient = coefficient;
    this.text = Text.getDescriptiveId(id);
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

  public String toString() {
    return text;
  }

}