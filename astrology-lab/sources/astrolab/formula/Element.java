package astrolab.formula;

public final class Element {

  public final static Element PLANET_SUN = new Element("_SUN", 0);
  public final static Element PLANET_MOON = new Element("MOON", 1);
  public final static Element PLANET_MERCURY = new Element("MERC", 2);
  public final static Element PLANET_VENUS = new Element("VENU", 3);
  public final static Element PLANET_MARS = new Element("MARS", 4);
  public final static Element PLANET_JUPITER = new Element("JUPI", 5);
  public final static Element PLANET_SATURN = new Element("SATU", 6);
  public final static Element HOUSE_ASC = new Element("_ASC", 7);
  public final static Element HOUSE_2 = new Element("__II", 8);
  public final static Element HOUSE_3 = new Element("_III", 9);
  public final static Element HOUSE_4 = new Element("__IV", 10);
  public final static Element HOUSE_5 = new Element("___V", 11);
  public final static Element HOUSE_6 = new Element("__VI", 12);

  private final static Element[] ELEMENTS = new Element[] {
      PLANET_SUN, PLANET_MOON, PLANET_MERCURY, PLANET_VENUS, PLANET_MARS, PLANET_JUPITER, PLANET_SATURN,
      HOUSE_ASC, HOUSE_2, HOUSE_3, HOUSE_4, HOUSE_5, HOUSE_6
  };

  private int index;
  private String text;

  private Element(String text, int index) {
    this.text = text;
    this.index = index;
  }

  public int getIndex() {
    return index;
  }

  public String toString() {
    return text;
  }

  public static Element[] getElements() {
    return ELEMENTS;
  }

  public static int getElementCount() {
    return ELEMENTS.length;
  }

  public static Element getElement(int id) {
    return ELEMENTS[id];
  }

}