package astrolab.formula;

public class FormulaScore {

  private int weakCollision = 0;
  private int strongCollision = 0;
  private int confirmed = 0;
  private double supported = 0;
  private double supportCollision = 0;
  private double score = 0;
  private boolean scoreCalculated = false;
  private FormulaData formula;

  FormulaScore(FormulaData formula) {
    this.formula = formula;
  }

  public double getScore() {
    if (!scoreCalculated) {
      if (strongCollision > 0) {
        score = -strongCollision * 200;
        scoreCalculated = true;
      } else {
        calculate(formula.getValue());
      }
    }
    return score;
  }

  public void feed(int[] value, double result, int target) {
    int index = (int) result;
    if (value[index] != FormulaData.NULL) {
      int collision = collision(value[index], target);
      if (collision == 0) {
        confirmed++;
      } else if (collision == 1) {
        weakCollision++;
      } else {
        strongCollision += collision;
      }
    }
    scoreCalculated = false;
  }

  public String toString() {
    return "" + (int) score + " (sc: " + strongCollision + " wc: " + weakCollision + " +: " + confirmed + " s+: " + supported + " s-: " + supportCollision + ")";
  }

  private final int collision(int result, int target) {
    int collision = Math.abs(result - target);
    if (collision > 5) collision = 10 - collision;
    return collision;
  }

  private final void calculate(int[] value) {
    int collision;

    supported = 0;
    supportCollision = 0;
    for (int index = 0; index < value.length; index++) {
      if (value[index] != FormulaData.NULL) {
        for (int i = 1; i < 20; i++) {
          if ((index + i < value.length) && (value[index + i] != FormulaData.NULL)) {
            collision = collision(value[index + i], value[index]);
            if (collision <= 1) {
              supported += 1.0 / i / i;
            } else if (i < 5 && collision <= i) {
              supported += 1.0 / i / i;
            } else if (i < 5) { // too harsh
              supportCollision += 1;
            } else {
              supportCollision += 1.0 / i / i;
            }
            break;
          }
        }
      }
    }

    score = -strongCollision * 200 - weakCollision * 1 + confirmed * 2 + supported - supportCollision;
    scoreCalculated = true;
  }

}