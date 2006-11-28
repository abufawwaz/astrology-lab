package astrolab.formula.score;

import astrolab.formula.FormulaData;

public class FormulaScoreFactory {

  public final static int SCORE_EXACT_VALUE = 1;
  public final static int SCORE_ACCUMULATIVE = 2;

  public static FormulaScore getScore(int scoreType, FormulaData formula) {
    switch (scoreType) {
    case SCORE_EXACT_VALUE: {
      return new FormulaScoreExactValue(formula);
    }
    case SCORE_ACCUMULATIVE: {
      return new FormulaScoreAccumulative(formula);
    }
    }
    return null;
  }
}