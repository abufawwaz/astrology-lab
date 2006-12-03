package astrolab.formula;

import astrolab.db.Database;
import astrolab.db.Text;

public class ElementSet {

  private static ElementSet defaultSet = null;

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

  public static synchronized ElementSet getDefault() {
    if (defaultSet == null) {
      int formulaeElementId = Text.getId("formulae_element"); 
      String[] elements = Database.queryList("SELECT descrid FROM text, types WHERE text.id = types.element_id AND types.type_id = " + formulaeElementId);
      defaultSet = new ElementSet(elements);
    }
    return defaultSet;
  }

}