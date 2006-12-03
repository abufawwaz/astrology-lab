package astrolab.formula.score;

import java.util.ArrayList;

import astrolab.formula.FormulaData;

class FormulaScoreAccumulative implements FormulaScore {

  private double score = 0;
  private boolean scoreCalculated = false;

  private double[] partialScore;
  private int[] partialCount;
  private double sumValue = 0;
  private int countValue = 0;

  private ArrayList<Double> values; // TODO: optimize this - remove the array
  
  FormulaScoreAccumulative(FormulaData formula) {
    partialScore = new double[formula.getValue().length];
    partialCount = new int[partialScore.length];
    values = new ArrayList<Double>();
  }

  public double getScore() {
    if (!scoreCalculated) {
      double maxScore = 0;
      double average = sumValue / countValue;

      for (int i = 0; i < values.size(); i++) {
        maxScore += Math.abs(values.get(i) - average);
      }

      score = 0;
      for (int i = 0; i < partialScore.length; i++) {
        score += Math.abs(partialScore[i] - average * partialCount[i]);
      }

      if (maxScore > 0) {
        score /= maxScore;
        score *= 100;
      } else {
        score = 100;
      }
      scoreCalculated = true;
    }

    return score;
  }

  public void feed(double result, double target) {
    sumValue += target;
    countValue++;

    partialScore[(int) result] += target;
    partialCount[(int) result]++;

    values.add(target);
  }

  public String toString() {
    return String.valueOf(((double) ((int) (getScore() * 100)) / 100)) + "%";
  }

}