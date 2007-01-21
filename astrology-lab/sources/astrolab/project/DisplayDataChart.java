package astrolab.project;

import astrolab.formula.FormulaDirectory;
import astrolab.formula.Formulae;
import astrolab.web.SVGDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayDataChart extends SVGDisplay {

  private ProjectData data = null;

  protected float strokeWidth = 1;

  private long minx = Long.MAX_VALUE;
  private long miny = Long.MAX_VALUE;
  private long maxx = 0;
  private long maxy = 0;
//  private String xFormula = "round(Sun % 10)";
//  private String[] yFormula = new String[] { "sum(sunspots)", "avg(sunspots)" };
//  private String xFormula = "time";
//  private String[] yFormula = new String[] { "sunspots" };
  private Formulae xaxis = null;
  private String[] yFormula = new String[] { "Sun", "Moon", "Mercury" };

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("<g style='fill:none;");
    buffer.append(decorateLine());
    buffer.append("'>");

    int x, y;
    for (int yf = 0; yf < yFormula.length; yf++) {
      if (data.begin()) {
        buffer.append("<polyline points='");

        do {
          x = (int) (data.getNumeric(xaxis.getAsText()) - minx);
          y = (int) (miny - data.getNumeric(yFormula[yf]));
          buffer.append(x);
          buffer.append(",");
          buffer.append(y);
          buffer.append(" ");
        } while (data.move());

        buffer.append("' />");
      }
    }

    buffer.append("</g>");
  }

  public void fillViewBox(Request request, LocalizedStringBuffer buffer) {
    xaxis = FormulaDirectory.getFormulae(FormulaDirectory.X_AXIS);
    data = Projects.getProject().getData(yFormula, xaxis.getAsText());

    if (data.begin()) {
      do {
        long value = (long) data.getNumeric(xaxis.getAsText());
        minx = (value < minx) ? value : minx;
        maxx = (value > maxx) ? value : maxx;
  
        for (int i = 0; i < yFormula.length; i++) {
          value = (long) data.getNumeric(yFormula[i]);
          miny = (value < miny) ? value : miny;
          maxy = (value > maxy) ? value : maxy;
        }
      } while (data.move());
    }

    strokeWidth = ((float) Math.min(maxx - minx, maxy - miny)) / 300;
    buffer.append("preserveAspectRatio='none' viewBox='0 " + (miny - maxy) + " " + (maxx - minx) + " " + (maxy - miny) + "'");
  }

  protected String decorateLine() {
    return "stroke:green;stroke-width:" + strokeWidth;
  }

}
