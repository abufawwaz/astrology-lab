package astrolab.formula;

import astrolab.astronom.houses.HouseSystem;
import astrolab.astronom.planet.Planet;
import astrolab.astronom.planet.SolarSystem;

public final class Element {

  private int index;
  private String text;

  Element(String text, int index) {
    this.text = text;
    this.index = index;
  }

  public int getIndex() {
    return index;
  }

  public double getPosition(SolarSystem solar, HouseSystem houses, Planet center) {
    Planet planet = solar.getPlanet(text);
    if (planet != null) {
      return planet.positionAround(center);
    } else {
      throw new RuntimeException("houses.getHouse(x) not implemented yet!");
    }
  }

  public String toString() {
    return text;
  }

}