package astrolab.formula;

public final class FormulaGeneratorElement extends Element {

  private final static double[] COEFINDEX = new double[] {
    0, 1, 1.0 / 2, 1.0 / 3, 2.0 / 3, 1.0 / 4, 3.0 / 4,
    1.0 / 5, 2.0 / 5, 3.0 / 5, 4.0 / 5, 1.0 / 6, 5.0 / 6,
    1.0 / 7, 2.0 / 7, 3.0 / 7, 4.0 / 7, 5.0 / 7, 6.0 / 7,
    1.0 / 8, 3.0 / 8, 5.0 / 8, 7.0 / 8,
    1.0 / 9, 2.0 / 9, 4.0 / 9, 5.0 / 9, 7.0 / 9, 8.0 / 9,
    1.0 / 10, 3.0 / 10, 7.0 / 10, 9.0 / 10,
    1.0 / 11, 2.0 / 11, 3.0 / 11, 4.0 / 11, 5.0 / 11, 6.0 / 11, 7.0 / 11, 8.0 / 11, 9.0 / 11, 10.0 / 11
  };

  private int coeffindex = 0;

  public FormulaGeneratorElement(int id) {
    super(id, 0.0);
  }

  public double getCoefficient() {
    return COEFINDEX[Math.abs(coeffindex)] * Math.signum(coeffindex);
  }

  public int getCoefficientIndex() {
    return coeffindex;
  }

  void setCoefficientIndex(int index) {
    this.coeffindex = index;
  }

  public String toString() {
    return getText() + "(x" + coeffindex + ")";
  }

}