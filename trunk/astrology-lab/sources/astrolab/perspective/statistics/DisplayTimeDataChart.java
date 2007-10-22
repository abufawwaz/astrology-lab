package astrolab.perspective.statistics;

import java.util.ArrayList;

import astrolab.astronom.SpacetimeEvent;
import astrolab.formula.Formulae;
import astrolab.project.Projects;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayTimeDataChart extends DisplayDataChart {

  private final static int SCALE = 300;
  private IteratorProjectTimeData data = null;

  private boolean polyline = false;
  private double minx = Long.MAX_VALUE;
  private double[] miny;
  private double maxx = 0;
  private double[] maxy;

  public DisplayTimeDataChart() {
    data = new IteratorProjectTimeData(Projects.getProject());
    Formulae[] series = data.getSeries();

    if (data.begin()) {
      miny = new double[series.length];
      maxy = new double[series.length];
      for (int s = 0; s < series.length; s++) {
        miny[s] = Long.MAX_VALUE;
        maxy[s] = 0;
      }

      do {
        double time = data.getTime().getTimeInMillis();
        maxx = (time > maxx) ? time : maxx;
        minx = (time < minx) ? time : minx;

        for (int i = 0; i < series.length; i++) {
          Formulae s = series[i];
          double[] value = data.getValue(s);
          if (value != null) {
            maxy[i] = (value[2] > maxy[i]) ? value[2] : maxy[i];
            miny[i] = (value[0] < miny[i]) ? value[0] : miny[i];
          }
        }
      } while (data.move());
      polyline = (miny != maxy);

      for (int s = 0; s < series.length; s++) {
        maxy[s] *= 1.1;
      }
    } else {
      System.err.println(" NO DATA !!!!!");
    }
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    fillGrid(buffer);

    double width = maxx - minx;

    if (data != null) {
      if (polyline) {
        buffer.append("<g style='fill:none;stroke-width:1'>");
  
        Formulae[] series = data.getSeries();
        for (int yf = 0; yf < series.length; yf++) {
          String color = "black";
  
          if ((color != null) && data.begin()) {
            fillSeriesLine(buffer, series[yf], yf, DisplayTimeConfiguration.COLORS[yf]);
          }
        }
  
        buffer.append("</g>");
      } else {
        buffer.append("<g style='");
        buffer.append(decorateLine());
        buffer.append("'>");
  
        Formulae[] series = data.getSeries();
        double x;
        for (int yf = 0; yf < series.length; yf++) {
          String color = "black";
  
          if ((color != null) && data.begin()) {
            do {
              x = (data.getTime().getTimeInMillis() - minx) / (maxx - minx) * SCALE;

              buffer.append("<rect x='");
              buffer.append(x - (width / 60));
              buffer.append("' width='");
              buffer.append(width / 30);
              buffer.append("' y='135' height='30' r='1' style='stroke:");
              buffer.append(color);
              buffer.append("' />");
            } while (data.move());
          }
        }
    
        buffer.append("</g>");
      }
    }
  }

  public void fillViewBox(Request request, LocalizedStringBuffer buffer) {
    buffer.append("preserveAspectRatio='none' viewBox='0 0 ");
    buffer.append(String.valueOf(SCALE));
    buffer.append(" ");
    buffer.append(String.valueOf(SCALE));
    buffer.append("'");
  }

  private String decorateLine() {
    if (polyline) {
      return "fill:none;stroke:green;stroke-width:1";
    } else {
      return "stroke:none;fill:green";
    }
  }

  private void fillGrid(LocalizedStringBuffer buffer) {
    double width = 20;
    buffer.append("<g style='fill:blue;fill-opacity:0.05'>");
    for (int i = 0; i < SCALE / width; i += 2) {
      double x = width * i;
      buffer.append("<rect x='" + x + "' width='" + width + "' height='" + SCALE + "' />");
    }
    buffer.append("</g>");

    fillTimes(buffer);
  }

  private void fillTimes(LocalizedStringBuffer buffer) {
    double width = 20;
    double timestep = (maxx - minx) * width / SCALE;
    buffer.append("<g style='font-size:5pt;fill:black;fill-opacity:0.5'>");
    for (int x = 0; x < SCALE / width; x++) {
      buffer.append("<g transform='translate(");
      buffer.append(String.valueOf(x * width));
      buffer.append(",20) rotate(90)'>");
      buffer.append("<text>");
      buffer.localize(new SpacetimeEvent((long) (minx + timestep * x)).toSimpleString());
      buffer.append("</text>");
      buffer.append("</g>");
    }
    buffer.append("</g>");
  }

  private final double position(double y, int series) {
    return (maxy[series] > miny[series]) ? (maxy[series] - y) / (maxy[series] - miny[series]) * SCALE : 0;
  }

  private void fillSeriesLine(LocalizedStringBuffer buffer, Formulae series, int index, String color) {
    ArrayList<double[]> dots = new ArrayList<double[]>();
    int x;
    int lastDrawn = -1;

    do {
      double[] rawy = data.getValue(series);

      if (rawy != null) {
        x = (int) ((data.getTime().getTimeInMillis() - minx) / (maxx - minx) * SCALE);

        if (lastDrawn < 0) {
          dots.add(new double[] { x - 1, SCALE, SCALE, SCALE });
        }

        lastDrawn = x;
        dots.add(new double[] { x, position(rawy[0], index), position(rawy[1], index), position(rawy[2], index) });
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

    double old = 0;
    for (double[] dot: dots) {
      if (Math.abs(dot[2] - old) > SCALE / 2) {
        buffer.append("' style='stroke:");
        buffer.append(color);
        buffer.append("' />");
        buffer.append("<polyline points='");
      }

      buffer.append(String.valueOf(dot[0]));
      buffer.append(",");
      buffer.append(String.valueOf(dot[2]));
      buffer.append(" ");
      old = dot[2];
    }

    buffer.append("' style='stroke:");
    buffer.append(color);
    buffer.append("' />");
  }
}
