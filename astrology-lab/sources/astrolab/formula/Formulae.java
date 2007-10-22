package astrolab.formula;

import java.util.ArrayList;
import java.util.Iterator;

import astrolab.astronom.util.Zodiac;
import astrolab.db.Database;
import astrolab.db.Personalize;
import astrolab.formula.score.FormulaScoreFactory;
import astrolab.project.Projects;
import astrolab.web.server.content.LocalizedStringBuffer;

public class Formulae {

  private int id = -1;
  private int project = -1;
  private int owner = -1;
  private Element[] element;

  private double score = -1;
  private FormulaData scoreData = null;

  private String text;

  public Formulae(int id, int project, int owner, String text, double score) {
    this.id = id;
    this.project = (project > 0) ? project : Projects.getProject().getId();
    this.owner = (owner > 0) ? owner : Personalize.getUser();
    this.text = text;
    this.score = score;
    this.element = Formulae.readElements(id);
  }

  public Formulae(Element[] element) {
    this.element = element;
  }

  public Formulae(String formulae) {
    this.text = formulae;
  }

  public void fill(LocalizedStringBuffer buffer) {
    if (element == null || element.length == 0) {
      buffer.append(getText());
    } else {
      Element first = null;
      for (Element element: getElements()) {
        if (element.getCoefficient() > 0) {
          fillElement(buffer, element, true);
          first = element;
          break;
        }
      }
      for (Element element: getElements()) {
        if ((first == null) || (element.getId() != first.getId())) {
          fillElement(buffer, element, false);
        }
      }
    }
  }

  private static void fillElement(LocalizedStringBuffer buffer, Element element, boolean isFirst) {
    double coefficient = element.getCoefficient();
    if (coefficient == 0) {
      return;
    } else if (coefficient == 1.0) {
      if (!isFirst) {
        buffer.append("+");
      }
    } else if (coefficient == -1.0) {
      buffer.append("-");
    } else if (coefficient > 0) {
      if (!isFirst) {
        buffer.append("+");
      }
      buffer.append(String.valueOf((int) Math.abs(coefficient)));
    } else if (coefficient < 0) {
      buffer.append("-");
      buffer.append(String.valueOf((int) Math.abs(coefficient)));
    }
    buffer.localize(element.getId());
  }

  public String getText() {
    return text;
  }

  public String getSQL() {
    StringBuffer result = new StringBuffer();

    for (Element e: element) {
      result.append(" ");
      if (e.getCoefficient() == 0) {
        continue;
      } else if (e.getCoefficient() > 0) {
        result.append("+");
      }
      result.append(e.getCoefficient());
      result.append("*");
      result.append(e.getText());
    }

    String rawKey = result.toString().trim();
    return "floor(" + rawKey + " - floor((" + rawKey + ") / 360) * 360)";
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

//    StatisticsRecordIterator iterator = StatisticsRecordIterator.iterate();
//    while (iterator.hasNext()) {
//      scoreData.feed((StatisticsRecord) iterator.next());
//    }

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

  private final static Element[] readElements(int id) {
    String[][] data = Database.queryList(2, "SELECT element_coefficient, element_id FROM formula_elements WHERE formulae_id = " + id);
    Element[] result = new Element[data.length];

    for (int i = 0; i < result.length; i++) {
      result[i] = new Element(Integer.parseInt(data[i][1]), Double.parseDouble(data[i][0]));
    }

    return result;
  }

}