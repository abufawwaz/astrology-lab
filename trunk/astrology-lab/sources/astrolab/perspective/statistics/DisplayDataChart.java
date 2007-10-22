package astrolab.perspective.statistics;

import astrolab.web.SVGDisplay;

public abstract class DisplayDataChart extends SVGDisplay {

  public DisplayDataChart() {
    super.addAction("formula", "formula");
    super.addAction("_chart_type", "_d");
  }

  public DisplayDataChart(double x, double y, double width, double height, String aspectRatio) {
    super(x, y, width, height, aspectRatio);
    super.addAction("formula", "formula");
    super.addAction("_chart_type", "_d");
  }

}