package astrolab.perspective.statistics;

import java.util.ArrayList;

import astrolab.formula.Formulae;
import astrolab.project.Projects;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayZodiacDataChart extends DisplayDataChart {

  private final static int SCALE = 360;
  private final static String[] COLOR = { "red", "green", "yellow", "blue" };

  private IteratorProjectZodiacData data = null;

  private double miny = Long.MAX_VALUE;
  private double maxy = 0;

  public DisplayZodiacDataChart() {
    super(0, 0, SCALE, SCALE, "none");
    data = new IteratorProjectZodiacData(Projects.getProject());
    Formulae[] series = data.getSeries();

    if (data.begin()) {
      do {
        for (Formulae s: series) {
          double[] value = data.getValue(s.getText());
          if (value != null) {
            maxy = (value[2] > maxy) ? value[2] : maxy;
            miny = (value[0] < miny) ? value[0] : miny;
          }
        }
      } while (data.move());
    }
  }

  private void fillGrid(LocalizedStringBuffer buffer) {
    buffer.append("<g style='fill:blue;fill-opacity:0.2'>");
    for (int i = 0, x = 0; i < 12; i++) {
      buffer.append("<rect x='");
      buffer.append(String.valueOf(x));
      buffer.append("' width='30' y='0'  height='");
      buffer.append(String.valueOf(SCALE));
      buffer.append("' style='fill:");
      buffer.append(COLOR[i % COLOR.length]);
      buffer.append("' />");
      x += 30;
    }
    buffer.append("</g>");
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    fillGrid(buffer);

    if (data != null) {
      buffer.append("<g style='fill:none;stroke-width:1'>");

      Formulae[] series = data.getSeries();
      for (int yf = 0; yf < series.length; yf++) {
        String color = "black";

        if ((color != null) && data.begin()) {
          fillSeriesLine(buffer, series[yf], color);
        }
      }

      buffer.append("</g>");
    }

    ComponentCorrectionFormulaeBuilder correctionBuilder = new ComponentCorrectionFormulaeBuilder(this, miny, maxy, SCALE);
    correctionBuilder.fill(buffer);
  }

  private final double position(double y) {
    return (maxy > miny) ? (maxy - y) / (maxy - miny) * SCALE : 0;
  }

  private void fillSeriesLine(LocalizedStringBuffer buffer, Formulae series, String color) {
    ArrayList<double[]> dots = new ArrayList<double[]>();
    int x;
    int lastDrawn = -1;

    do {
      double[] rawy = data.getValue(series.getText());

      if (rawy != null) {
        x = ((int) data.getBaseValue());

        if (lastDrawn < 0) {
          dots.add(new double[] { x - 1, SCALE, SCALE, SCALE });
        }

        lastDrawn = x;
        dots.add(new double[] { x, position(rawy[0]), position(rawy[1]), position(rawy[2]) });
      }
    } while (data.move());

    if (lastDrawn >= 0) {
      dots.add(new double[] { lastDrawn +  1, SCALE, SCALE, SCALE });
    }

    fillSeriesPolygon(buffer, dots, color);
    fillSeriesCenter(buffer, dots, color);
  }

  private void fillSeriesPolygon(LocalizedStringBuffer buffer, ArrayList<double[]> dots, String color) {
    int index = 0;

    while (index < dots.size()) {
      int oldindex = index;

      buffer.append("<polygon points='");
      for (int oldx = -1; index < dots.size(); index++) {
        int x = (int) dots.get(index)[0];

        if ((oldx == -1) || (x == oldx + 1)) {
          buffer.append(String.valueOf(x));
          buffer.append(",");
          buffer.append(String.valueOf((int) dots.get(index)[1]));
          buffer.append(" ");

          oldx = x;
        } else {
          break;
        }
      }
      for (int i = index - 1; i >= oldindex; i--) {
        buffer.append(String.valueOf((int) dots.get(i)[0]));
        buffer.append(",");
        buffer.append(String.valueOf((int) dots.get(i)[3]));
        buffer.append(" ");
      }

      buffer.append("' style='stroke:none;fill:");
      buffer.append(color);
      buffer.append(";opacity:0.3' />");

      index++;
    }
  }

  private void fillSeriesCenter(LocalizedStringBuffer buffer, ArrayList<double[]> dots, String color) {
    buffer.append("<polyline points='");

    for (double[] dot: dots) {
      buffer.append(String.valueOf((int) dot[0]));
      buffer.append(",");
      buffer.append(String.valueOf((int) dot[2]));
      buffer.append(" ");
    }

    buffer.append("' style='stroke:");
    buffer.append(color);
    buffer.append("' />");
  }

}
