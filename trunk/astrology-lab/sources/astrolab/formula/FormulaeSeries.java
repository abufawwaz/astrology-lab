package astrolab.formula;

public class FormulaeSeries extends Formulae {

  private String color;

  public FormulaeSeries(int id, int project, int owner, String text, double score, String color) {
    super(id, project, owner, text, score);
    this.color = color;
  }

  public String getColor() {
    return color;
  }

}