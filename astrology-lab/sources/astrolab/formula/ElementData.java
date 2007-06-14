package astrolab.formula;

import java.util.HashMap;

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

    for (int i = 0; i < elements.length; i++) {
      values.put(elements[i].getId(), elements[i].getPosition(event));
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