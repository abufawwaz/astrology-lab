package astrolab.formula;

public class FormulaData {

  final static int NULL = -Integer.MAX_VALUE;

  private Formulae formula;
  private FormulaScore score;
  private int[] value = new int[360];

  FormulaData(Formulae formula) {
    this.formula = formula;
    this.score = new FormulaScore(this);
    for (int i = 0; i < value.length; i++) {
      value[i] = NULL;
    }
  }

  public FormulaScore getScore() {
    return score;
  }

  public Formulae getFormulae() {
    return formula;
  }

  int[] getValue() {
    return value;
  }

  public void feed(ElementData data) {
    // check for collision
    double v = formula.calculate(data);
    score.feed(value, v, data.getTarget());
    value[(int) v] = data.getTarget();
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer(formula.toString());
    buffer.append("[");
    buffer.append(score);
    buffer.append("]");
    for (int i = 0; i < value.length; i++) {
      buffer.append(" ");
      buffer.append((value[i] != NULL) ? String.valueOf(value[i]) : "?");
    }
    return buffer.toString();
  }
}
