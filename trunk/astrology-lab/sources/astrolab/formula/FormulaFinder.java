package astrolab.formula;

import java.util.ArrayList;

import astrolab.db.Personalize;
import astrolab.db.Text;
import astrolab.formula.display.ModifyFormulae;
import astrolab.project.statistics.StatisticsRecord;
import astrolab.project.statistics.StatisticsRecordIterator;
import astrolab.tools.Log;

public class FormulaFinder extends Thread {

  public FormulaFinder() {
    setName("FormulaFinder");
    setPriority(Thread.MIN_PRIORITY);
  }

  public synchronized void run() {
    // TODO: get those from the database
    Personalize.set(2000001, Personalize.LANGUAGE_EN);
    Personalize.addFavourite(-1, 3000027, Text.getId("user.session.project"));

    double score = 0;
    Element[] formulaElements = ElementSet.getDefault().getElements();
    FormulaGeneratorElement[] formulaSource = new FormulaGeneratorElement[formulaElements.length];

    for (int i = 0; i < formulaSource.length; i++) {
      formulaSource[i] = new FormulaGeneratorElement(formulaElements[i].getId());
    }

    Formulae formulae = FormulaGenerator.generateNext(formulaSource); // get first formulae from database
    ArrayList<ElementData> records = new ArrayList<ElementData>();

// TODO: get the score?
//    FormulaIterator own = FormulaIterator.iterateOwn();
//    while (own.hasNext()) {
//      Formulae candidate = (Formulae) own.next();
//      if (candidate.getScore() > score) {
//        score = candidate.getScore();
//      }
//    }

    StatisticsRecordIterator iterator = StatisticsRecordIterator.iterate();
    while (iterator.hasNext()) {
      StatisticsRecord stat = (StatisticsRecord) iterator.next();
      records.add(new ElementData(ElementSet.getDefault(), stat, stat.getValue()));
    }

    if (records.isEmpty()) {
      Log.log("No statistics records available.");
      return;
    }

    while (true) {
      for (int iteration = 0; iteration < 100; iteration++) {
        formulae = FormulaGenerator.generateNext(formulaSource);
  
        if (formulae.getScore(records) > score) {
          // store the best formulae so far
          ModifyFormulae.storeFormulae(formulae.getText());
          score = formulae.getScore();
        } else {
          // store the current formulae for monitoring
          try {
            Log.beSilent(true);
            ModifyFormulae.storeFormulae(formulae.getText());
          } finally {
            Log.beSilent(false);
          }
        }
      }

      try {
        wait(100);
      } catch (Exception e) {
        Log.log("Exception on FormulaFinder.wait", e);
      }
    }
  }

}