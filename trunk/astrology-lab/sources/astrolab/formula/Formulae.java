package astrolab.formula;

import java.util.ArrayList;
import java.util.Iterator;

import astrolab.astronom.util.Zodiac;
import astrolab.db.Database;
import astrolab.db.Personalize;
import astrolab.db.Text;
import astrolab.formula.score.FormulaScoreFactory;
import astrolab.project.statistics.StatisticsRecord;
import astrolab.project.statistics.StatisticsRecordIterator;

public class Formulae {

  private int id;
  private int project;
  private int owner;
  private Element[] element;

  private double score;
  private FormulaData scoreData = null;

  public Formulae(int id, int project, int owner, double score) {
    this.id = id;
    this.project = project;
    this.owner = owner;
    this.score = score;
    this.element = Formulae.readElements(id);
  }

  public Formulae(Element[] element) {
    this.element = element;
  }

  public int getId() {
    return id;
  }

  public int getProject() {
    return project;
  }

  public int getOwner() {
    return owner;
  }

  public double getScore() {
    return score;
  }

  public double getScore(ArrayList<ElementData> records) {
    FormulaData scoreData = new FormulaData(this, FormulaScoreFactory.SCORE_ACCUMULATIVE);

    Iterator<ElementData> iterator = records.iterator();
    while (iterator.hasNext()) {
      scoreData.feed((ElementData) iterator.next());
    }

    return scoreData.getScore().getScore();
  }

  public Element[] getElements() {
    return element;
  }

  public double calculateSlot(ElementData data) {
    double result = 0;
    for (int i = 0; i < element.length; i++) {
      result += data.getValue(element[i]) * element[i].getCoefficient();
    }
    return Zodiac.degree(result);
  }

  public double calculateValue(ElementData elementData) {
    if (scoreData == null) {
      rescore();
    }
    return scoreData.getValue((int) calculateSlot(elementData));
  }

  public double rescore() {
    scoreData = new FormulaData(this, FormulaScoreFactory.SCORE_ACCUMULATIVE);

    StatisticsRecordIterator iterator = StatisticsRecordIterator.iterate();
    while (iterator.hasNext()) {
      scoreData.feed((StatisticsRecord) iterator.next());
    }

    score = scoreData.getScore().getScore();
    return getScore();
  }

  public String toString() {
    StringBuffer text = new StringBuffer();
    for (int i = 0; i < element.length; i++) {
      if (element[i].getCoefficient() != 0.0) {
        text.append(element[i]);
        text.append(" ");
      }
    }
    return text.toString();
  }

  public static void store(int formulae_slot, Formulae formulae) {
    if (formulae_slot > 9) {
      formulae_slot = 9;
    }
    int project_id = Personalize.getFavourite(-1, Text.getId("user.session.project"), -1);
    Element[] elements = formulae.getElements();
    int user = Personalize.getUser(true);
    int id = Text.reserve("Formulae:" + project_id + ":" + user + ":" + formulae_slot, Text.TYPE_FORMULAE);
    double score = formulae.rescore();

    Database.execute("DELETE FROM formula_description WHERE formulae_id = " + id);
    Database.execute("INSERT INTO formula_description VALUES (" + id + ", " + project_id + ", " + user + ", " + score + ")");

    Database.execute("DELETE FROM formula_elements WHERE formulae_id = " + id);
    for (int i = 0; i < elements.length; i++) {
      if (Math.abs(elements[i].getCoefficient()) > 0.0001) {
        Database.execute("INSERT INTO formula_elements VALUES (" + id + ", " + elements[i].getCoefficient() + ", " + elements[i].getId() + ")");
      }
    }
  }

  private final static Element[] readElements(int id) {
    String[][] data = Database.queryList(2, "SELECT element_coefficient, element_id FROM formula_elements WHERE formulae_id = " + id);
    Element[] result = new Element[data.length];

    for (int i = 0; i < result.length; i++) {
      result[i] = new Element(Integer.parseInt(data[i][1]), Double.parseDouble(data[i][0]));
    }

    return result;
  }

}