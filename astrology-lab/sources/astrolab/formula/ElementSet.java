package astrolab.formula;

import astrolab.db.Database;
import astrolab.db.Text;

public class ElementSet {

  private static ElementSet defaultSet = null;

  private Element[] elements;

  public ElementSet(int[] factors) {
    elements = new Element[factors.length];
    for (int i = 0; i < factors.length; i++) {
      elements[i] = new Element(factors[i]);
    }
  }

  public Element[] getElements() {
    return elements;
  }

  public static synchronized ElementSet getDefault() {
    if (defaultSet == null) {
      int formulaeElementId = Text.getId("formulae_element"); 
      int[] elements = Database.queryIds("SELECT id FROM text, types WHERE text.id = types.element_id AND types.type_id = " + formulaeElementId);
      defaultSet = new ElementSet(elements);
    }
    return defaultSet;
  }

}