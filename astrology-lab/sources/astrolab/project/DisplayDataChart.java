package astrolab.project;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import astrolab.formula.FormulaIterator;
import astrolab.formula.FormulaeBase;
import astrolab.formula.FormulaePeriod;
import astrolab.formula.FormulaeSeries;
import astrolab.web.SVGDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayDataChart extends SVGDisplay {

  private ProjectData data = null;

  protected double strokeWidth = 1;

  private double minx = Long.MAX_VALUE;
  private double miny = Long.MAX_VALUE;
  private double maxx = 0;
  private double maxy = 0;

  private FormulaeBase base = null;
  private FormulaePeriod period = null;
  private FormulaeSeries[] series = null;

  private static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.###############", new DecimalFormatSymbols(Locale.ENGLISH));

  public DisplayDataChart() {
    super.addAction("formula", "formula");
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    double width = (maxx - minx) / 12;
    double height = maxy - miny;

    buffer.append("<g style='fill:blue;fill-opacity:0.05'>");
    for (int i = 0; i < 12; i += 2) {
      double x = width * i;
      buffer.append("<rect x='" + x + "' y='" + (miny - maxy) + "' width='" + width + "' height='" + height + "' />");
    }
    buffer.append("</g>");

    if (data != null) {
      buffer.append("<g style='fill:none;");
      buffer.append(decorateLine());
      buffer.append("'>");
  
      double x, y;
      for (int yf = 0; yf < series.length; yf++) {
        String color = series[yf].getColor();

        if ((color != null) && data.begin()) {
          buffer.append("<polyline points='");
  
          do {
            x = data.getNumeric(base.getText()) - minx;
            y = miny - data.getNumeric(series[yf].getText());
            buffer.append(x);
            buffer.append(",");
            buffer.append(y);
            buffer.append(" ");
          } while (data.move());

          buffer.append("' ");

          buffer.append("style='stroke:");
          buffer.append(color);
          buffer.append("'");

          buffer.append(" />");
        }
      }
  
      buffer.append("</g>");
    }
  }

  public void fillViewBox(Request request, LocalizedStringBuffer buffer) {
    series = FormulaIterator.getChartSeries(true);
    base = FormulaIterator.getChartBase();
    period = FormulaIterator.getChartPeriod();

    if (series.length > 0) {
      data = Projects.getProject().getData(series, base, period, FormulaIterator.getChartFromTime(), FormulaIterator.getChartToTime());

      if (data.begin()) {
        do {
          double value = (double) data.getNumeric(base.getText());
          minx = (value < minx) ? value : minx;
          maxx = (value > maxx) ? value : maxx;
    
          for (int i = 0; i < series.length; i++) {
            value = (double) data.getNumeric(series[i].getText());
            miny = (value < miny) ? value : miny;
            maxy = (value > maxy) ? value : maxy;
          }
        } while (data.move());

        miny -= 5;
        maxy += 5;
      } else {
        data = null;
      }
    }

    strokeWidth = ((double) (maxy - miny)) / 300;

    buffer.append("preserveAspectRatio='none' viewBox='0 " + (miny - maxy) + " " + (maxx - minx) + " " + (maxy - miny) + "'");
  }

  protected String decorateLine() {
    return "stroke:green;stroke-width:" + DECIMAL_FORMAT.format(strokeWidth);
  }

}
