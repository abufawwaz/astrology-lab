package astrolab.formula;

import astrolab.astronom.util.Zodiac;

public class Formulae {

  private int divide;
  private Element[] element;

  Formulae(Element[] element, int divide) {
    this.element = element;
    this.divide = divide;
  }

  Element[] getElements() {
    return element;
  }

  int getDivedBy() {
    return divide;
  }

  public double calculate(ElementData data) {
    double result = 0;
    for (int i = 0; i < element.length; i += 2) {
      result += data.getValue(element[i]);
    }
    for (int i = 1; i < element.length; i += 2) {
      result -= data.getValue(element[i]);
    }
    return Zodiac.degree(result / divide);
  }

  public String toString() {
    StringBuffer text = new StringBuffer();
    for (int i = 0; i < element.length; i++) {
      text.append((i % 2 == 0) ? "+" : "-");
      text.append(element[i]);
      text.append(" ");
    }
    if (divide > 1) {
      text.append("/ ");
      text.append(divide);
      text.append(" ");
    }
    return text.toString();
  }
}