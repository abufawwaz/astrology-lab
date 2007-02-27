package astrolab.formula;

public class FormulaGenerator {

  public final static Formulae generateNext(FormulaGeneratorElement[] elements) {
    int max_used_coef = -1;
    int max_coef_pos = -1;

    for (int i = elements.length - 1; i >= 0; i--) {
      if (max_used_coef < elements[i].getCoefficientIndex()) {
        max_used_coef = elements[i].getCoefficientIndex();
        max_coef_pos = i;
      }
    }

    boolean isNew = false;
    for (int pos = 0; pos < elements.length; pos++) {
      if (pos == max_coef_pos) {
        continue;
      }
      if ((pos < max_coef_pos) && (elements[pos].getCoefficientIndex() < max_used_coef)) {
        elements[pos].setCoefficientIndex(elements[pos].getCoefficientIndex() + 1);
        isNew = true;
        break;
      } else if ((pos > max_coef_pos) && (elements[pos].getCoefficientIndex() < max_used_coef - 1)) {
        elements[pos].setCoefficientIndex(elements[pos].getCoefficientIndex() + 1);
        isNew = true;
        break;
      } else {
        elements[pos].setCoefficientIndex(- max_used_coef);
      }
    }

    if (!isNew) {
      if (max_coef_pos == elements.length - 1) {
        max_coef_pos = 0;
        max_used_coef++;
      } else {
        max_coef_pos ++;
      }
      for (int pos = 0; pos < elements.length; pos++) {
        if (pos != max_coef_pos) {
          elements[pos].setCoefficientIndex(-max_used_coef);
        } else {
          elements[pos].setCoefficientIndex(max_used_coef);
        }
      }
    }

//    return new Formulae(elements);
    return null;
  }

}