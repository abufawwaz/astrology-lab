package astrolab.formula;

import astrolab.formula.score.FormulaScore;
import astrolab.formula.score.FormulaScoreFactory;

public class FormulaData {

  public final static int NULL = -Integer.MAX_VALUE;

  private Formulae formula;
  private FormulaScore score;
  private double[] value = new double[360];
  private int[] count = new int[360];

  FormulaData(Formulae formula, int scoreType) {
    this.formula = formula;
    this.score = FormulaScoreFactory.getScore(scoreType, this);
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

  public double[] getValue() {
    double[] result = new double[360];
    for (int i = 0; i < result.length; i++) {
      result[i] = getValue(i);
    }
    return result;
  }

  public double getValue(int index) {
    return (count[index] > 0) ? value[index] / count[index] : NULL;
  }

  // TODO: deprecate in favour of StatisticsRecord
  public void feed(ElementData data) {
    // check for collision
    double v = formula.calculateSlot(data);
    score.feed(v, data.getTarget());
    value[(int) v] = data.getTarget();
    count[(int) v]++;
  }

//  public void feed(StatisticsRecord record) {
//    feed(new ElementData(ElementSet.getDefault(), record, record.getValue()));
//  }

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
