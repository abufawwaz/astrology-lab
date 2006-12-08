package astrolab.formula;

import java.util.ArrayList;

import astrolab.db.Personalize;
import astrolab.db.Text;
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
    Element[] formulaSource = (Element[]) ElementSet.getDefault().getElements().clone();
    Formulae formulae = FormulaGenerator.generateNext(formulaSource); // get first formulae from database
    ArrayList<ElementData> records = new ArrayList<ElementData>();

    FormulaIterator own = FormulaIterator.iterateOwn();
    while (own.hasNext()) {
      Formulae candidate = (Formulae) own.next();
      if (candidate.getScore() > score) {
        score = candidate.getScore();
      }
    }

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
System.err.println(" Next formulae: " + formulae + " -> " + formulae.getScore(records));
  
        if (formulae.getScore(records) > score) {
          Formulae.store(8, formulae);
          score = formulae.getScore();
System.err.println(" Store formulae: " + formulae + " -> " + formulae.getScore());
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