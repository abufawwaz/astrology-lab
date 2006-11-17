package astrolab.formula;

public class FormulaGenerator {

  public final static Formulae generateNext(Formulae f1) {
    boolean skipper = false;
    Formulae f2 = f1;
    Formulae f3 = f1;
    Element[] e;
    if (f1 == null) {
      return next(null);
    }
    while (true) {
      f3 = next(f2);
      e = f3.getElements();

      skipper = false;
      for (int i = 0; i < e.length; i++) {
        skipper = !accept(e, i, e[i]);
        if (skipper) {
          break;
        }
      }
      if (!skipper) {
        break;
      }
      f2 = f3;
    }
    return f3;
  }

  private final static Formulae next(Formulae f1) {
    Element first = Element.getElements()[0];
    if (f1 == null) {
      return new Formulae(new Element[] { first }, 1);
    } else if (f1.getDivedBy() < f1.getElements().length / 2 - 1) {
      return new Formulae(f1.getElements(), f1.getDivedBy() + 1);
    } else {
      Element[] e = f1.getElements();
      for (int i = 0; i < e.length; i++) {
        Element n = next(e, i);
        if (n != null) {
          Element[] ne = (Element[]) e.clone();
          for (int j = 0; j < i; j++) {
            ne[j] = first;
          }
          ne[i] = n;
          return new Formulae(ne, 1);
        }
      }
      Element[] ne = new Element[e.length + 1];
      for (int i = 0; i < ne.length; i++) {
        ne[i] = first;
      }
      return new Formulae(ne, 1);
    }
  }

  private final static Element next(Element[] e, int index) {
    Element[] all = Element.getElements();
    int i = e[index].getIndex() + 1;
    //check if already in formulae
    while (i < all.length && !accept(e, index, all[i])) i++;

    return (i < all.length) ? all[i] : null;
  }

  private final static boolean accept(Element[] e, int index, Element newElement) {
    for (int i = index + 1; i < e.length; i += 2) {
      if (e[i] == newElement) {
        return false;
      }
    }
    for (int i = index + 2; i < e.length; i += 2) {
      if (e[i].getIndex() < newElement.getIndex()) {
        return false;
      }
    }
    return true;
  }

}