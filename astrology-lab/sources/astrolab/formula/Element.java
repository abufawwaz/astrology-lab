package astrolab.formula;

import astrolab.astronom.ActivePoint;
import astrolab.db.Event;
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

  public double getPosition(Event event) {
    ActivePoint point = ActivePoint.getActivePoint(text, event);
    return point.getPosition();
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