package astrolab.formula;

public class FormulaGenerator {

  public final static Formulae generateNext(Element[] elements) {
    int max_used_coef = 0;
    int max_coef_pos = 0;

    for (int i = 0; i < elements.length; i++) {
      if (max_used_coef < elements[i].getCoefficientIndex()) {
        max_used_coef = elements[i].getCoefficientIndex();
        max_coef_pos = i;
      }
    }

    boolean isNew = false;
    for (int pos = 0; pos < elements.length; pos++) {
      if (elements[pos].getCoefficientIndex() < max_used_coef) {
        if (max_coef_pos > pos) {
          elements[pos].setCoefficientIndex(elements[pos].getCoefficientIndex() + 1);
        } else {
          elements[pos].setCoefficientIndex(max_used_coef);
        }
        isNew = true;
        break;
      } else {
        elements[pos].setCoefficientIndex(0);
      }
    }

    if (!isNew) {
      elements[0].setCoefficientIndex(max_used_coef + 1);
    }

    return new Formulae(elements);
  }

}