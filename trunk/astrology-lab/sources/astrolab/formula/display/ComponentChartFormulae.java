package astrolab.formula.display;

import astrolab.formula.ElementData;
import astrolab.formula.ElementSet;
import astrolab.formula.FormulaIterator;
import astrolab.formula.Formulae;
import astrolab.project.statistics.ComponentChartStatistics;
import astrolab.project.statistics.StatisticsRecord;

public class ComponentChartFormulae extends ComponentChartStatistics {

  Formulae formulae = null;

  public ComponentChartFormulae() {
    FormulaIterator iterator = FormulaIterator.iterateBest();
    if (iterator.hasNext()) {
      formulae = (Formulae) iterator.next();
    }
  }

  protected String decorateLine() {
    return "stroke:orange;stroke-width:2";
  }

  protected int calculateValue(StatisticsRecord record) {
    // TODO: optimize use of ElementData
    return (formulae != null) ? (int) formulae.calculateValue(new ElementData(ElementSet.getDefault(), record, -1)) : 0;
  }

}
