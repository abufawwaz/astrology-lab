package astrolab.formula;

public class FormulaDirectory {

  public final static String X_AXIS = "user.chart.xaxis";

  public static Formulae getFormulae(String key) {
    // get from personalization
    return new Formulae("time");
  }

}