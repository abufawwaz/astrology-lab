package astrolab.formula.score;

public interface FormulaScore {

  public double getScore();

  public void feed(double result, double target);

  public String toString();

}