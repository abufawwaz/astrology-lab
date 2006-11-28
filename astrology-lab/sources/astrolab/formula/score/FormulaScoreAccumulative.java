package astrolab.formula.score;

import astrolab.formula.FormulaData;

class FormulaScoreAccumulative implements FormulaScore {

  private double score = 0;
  private int span = 0;
  private boolean scoreCalculated = false;
  private FormulaData formula;

  FormulaScoreAccumulative(FormulaData formula) {
    this.formula = formula;
  }

  public double getScore() {
    if (!scoreCalculated) {
      double[] value = formula.getValue();
      double difference = 0;
      double min = Double.POSITIVE_INFINITY;
      double max = Double.NEGATIVE_INFINITY;

      span = 0;
      for (int i = 0; i < value.length; i++) {
        if (value[i] != FormulaData.NULL) {
          span++;
          max = Math.max(value[i], max);
          min = Math.min(value[i], min);
        }
      }

      for (int i = 0; i < value.length; i++) {
        if (value[i] != FormulaData.NULL) {
          double distance = (value[i] * 2 - max + min) / (max - min);
          difference += distance * distance;
        }
      }

      score = difference * Math.max(max - min - 1, 0) * (span - 1 / value.length);
      scoreCalculated = true;
    }

    return score;
  }

  public void feed(double result, int target) {
    // no pre-processing
  }

  public String toString() {
    return (scoreCalculated) ? "" + (int) score + " (effective-span: " + span + ")" : "not scored yet";
  }

}