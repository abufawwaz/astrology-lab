package astrolab.formula;

import java.util.Vector;

import astrolab.db.Event;

public class FormulaPlot {

  private int size;
  private int scoreType;
  private Vector formula;
  private Vector dates;
  private Formulae lastFormula = null;
  private ElementSet elementSet = null;

  public FormulaPlot(int size, int scoreType, ElementSet elementSet) {
    this.size = size;
    this.scoreType = scoreType;
    this.elementSet = elementSet;
    this.formula = new Vector(size);
    this.dates = new Vector();

    feed();
  }

  public void feed(Event event, int number) {
    ElementData data = new ElementData(elementSet, event, number);
    dates.add(data);

    for (int i = 0; i < formula.size(); i++) {
      ((FormulaData) formula.get(i)).feed(data);
    }
  }

  public void step(int count) {
    for (int i = 0; i < count; i++) {
      sort();
      formula.setSize(Math.max(25, (int) (size * 0.25)));
      feed();
    }
  }

  private void feed() {
    FormulaData data;
    while (formula.size() < size) {
      lastFormula = FormulaGenerator.generateNext(lastFormula, elementSet);
      data = new FormulaData(lastFormula, scoreType);
      formula.add(data);

      for (int i = 0; i < dates.size(); i++) {
        data.feed((ElementData) dates.get(i));
      }
    }
  }

  private void sort() {
    for (int i = 0; i < formula.size() - 1; i++) {
      FormulaData best = (FormulaData) formula.get(i);
      FormulaData current;
      for (int j = i + 1; j < formula.size(); j++) {
        current = (FormulaData) formula.get(j);
        if (current.getScore().getScore() > best.getScore().getScore()) {
          best = current;
        }
      }
      formula.remove(best);
      formula.insertElementAt(best, i);
    }
  }

  public String toString() {
    return toString(size);
  }

  public String toString(int size) {
    StringBuffer buffer = new StringBuffer("\r\n");
    for (int i = 0; i < size && i < formula.size(); i++) {
      buffer.append(formula.get(i));
      buffer.append("\r\n");
    }
    buffer.append("--------\r\n");
    for (int i = formula.size() - 5; i < formula.size(); i++) {
      buffer.append(formula.get(i));
      buffer.append("\r\n");
    }
    return buffer.toString();
  }

}
