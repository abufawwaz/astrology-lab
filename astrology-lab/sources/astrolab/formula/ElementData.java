package astrolab.formula;

import java.util.HashMap;

import astrolab.astronom.houses.HouseSystem;
import astrolab.astronom.houses.PlacidusSystem;
import astrolab.astronom.planet.Planet;
import astrolab.astronom.planet.SolarSystem;
import astrolab.db.Event;

public class ElementData {

  private double target;
  private Event event;
  private HashMap<Integer, Double> values;

  public ElementData(ElementSet elementSet, Event event, double target) {
    Element[] elements = elementSet.getElements();
    this.event = event;
    this.target = target;
    this.values = new HashMap<Integer, Double>();

    HouseSystem houses = new PlacidusSystem(event);
    SolarSystem solar = new SolarSystem();
//    Planet center = solar.getPlanet(SolarSystem.EARTH);
    Planet center = solar.getPlanet(SolarSystem.SUN);
    solar.calculate(event);

    for (int i = 0; i < elements.length; i++) {
      values.put(elements[i].getId(), elements[i].getPosition(solar, houses, center));
    }
  }

  public double getValue(Element element) {
    return values.get(element.getId());
  }

  public double getTarget() {
    return target;
  }

  public Event getEvent() {
    return event;
  }

}