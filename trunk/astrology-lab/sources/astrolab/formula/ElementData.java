package astrolab.formula;

import astrolab.astronom.houses.HouseSystem;
import astrolab.astronom.houses.PlacidusSystem;
import astrolab.astronom.planet.Planet;
import astrolab.astronom.planet.SolarSystem;
import astrolab.db.Event;

public class ElementData {

  private int target;
  private Event event;
  private double[] values;

  public ElementData(ElementSet elementSet, Event event, int target) {
    Element[] elements = elementSet.getElements();
    this.event = event;
    this.target = target;
    this.values = new double[elements.length];

    HouseSystem houses = new PlacidusSystem(event);
    SolarSystem solar = new SolarSystem();
//    Planet center = solar.getPlanet(SolarSystem.EARTH);
    Planet center = solar.getPlanet(SolarSystem.SUN);
    solar.calculate(event);

    for (int i = 0; i < values.length; i++) {
      values[i] = elements[i].getPosition(solar, houses, center);
    }
  }

  public double getValue(Element element) {
    return values[element.getIndex()];
  }

  public int getTarget() {
    return target;
  }

  public Event getEvent() {
    return event;
  }

}