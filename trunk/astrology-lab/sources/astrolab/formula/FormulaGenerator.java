package astrolab.formula;

public class FormulaGenerator {

  private final static double[] COEFROW = new double[] {
    0, 1, 1.0 / 2, 1.0 / 3, 2.0 / 3, 1.0 / 4, 3.0 / 4,
    1.0 / 5, 2.0 / 5, 3.0 / 5, 4.0 / 5, 1.0 / 6, 5.0 / 6,
    1.0 / 7, 2.0 / 7, 3.0 / 7, 4.0 / 7, 5.0 / 7, 6.0 / 7,
    1.0 / 8, 3.0 / 8, 5.0 / 8, 7.0 / 8,
    1.0 / 9, 2.0 / 9, 4.0 / 9, 5.0 / 9, 7.0 / 9, 8.0 / 9,
    1.0 / 10, 3.0 / 10, 7.0 / 10, 9.0 / 10,
    1.0 / 11, 2.0 / 11, 3.0 / 11, 4.0 / 11, 5.0 / 11, 6.0 / 11, 7.0 / 11, 8.0 / 11, 9.0 / 11, 10.0 / 11
  };

  public final static Formulae generateNext(Formulae f1, ElementSet elements) {
//    boolean skipper = false;
//    Formulae f2 = f1;
//    Formulae f3 = f1;
//    Element[] e;
//    if (f1 == null) {
//      return next(null, elements);
//    }
//    while (true) {
//      f3 = next(f2, elements);
//      e = f3.getElements();
//
//      skipper = false;
//      for (int i = 0; i < e.length; i++) {
//        skipper = !accept(e, i, e[i]);
//        if (skipper) {
//          break;
//        }
//      }
//      if (!skipper) {
//        break;
//      }
//      f2 = f3;
//    }
//    return f3;
    throw new RuntimeException("To be corrected");
  }

  private final static Formulae next(Formulae f1, ElementSet elements) {
    Element first = elements.getElements()[0];
    if (f1 == null) {
      return new Formulae(new Element[] { first });
//    } else if (f1.getDivedBy() < f1.getElements().length / 2 - 1) {
//      return new Formulae(f1.getElements(), f1.getDivedBy() + 1);
    } else {
      Element[] e = f1.getElements();
      for (int i = 0; i < e.length; i++) {
        Element n = next(e, i, elements);
        if (n != null) {
          Element[] ne = (Element[]) e.clone();
          for (int j = 0; j < i; j++) {
            ne[j] = first;
          }
          ne[i] = n;
          return new Formulae(ne);
        }
      }
      Element[] ne = new Element[e.length + 1];
      for (int i = 0; i < ne.length; i++) {
        ne[i] = first;
      }
      return new Formulae(ne);
    }
  }

  private final static Element next(Element[] e, int index, ElementSet elements) {
    Element[] all = elements.getElements();
    int i = 0; //e[index].getIndex() + 1;
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
//    for (int i = index + 2; i < e.length; i += 2) {
//      if (e[i].getIndex() < newElement.getIndex()) {
//        return false;
//      }
//    }
    return true;
  }

}