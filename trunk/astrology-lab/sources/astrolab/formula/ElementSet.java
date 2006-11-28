package astrolab.formula;

public class ElementSet {

  private Element[] elements;

  public ElementSet(String[] factors) {
    elements = new Element[factors.length];
    for (int i = 0; i < factors.length; i++) {
      elements[i] = new Element(factors[i], i);
    }
  }

  public Element[] getElements() {
    return elements;
  }

}