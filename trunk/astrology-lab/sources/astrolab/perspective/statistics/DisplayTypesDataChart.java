package astrolab.perspective.statistics;

import astrolab.project.Projects;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayTypesDataChart extends DisplayDataChart {

  private final static int CHART_HEIGHT = 10;

  private int typesCount = 0;
  private int maxValue = 0;
  private IteratorProjectTypeData data = null;

  public DisplayTypesDataChart() {
    data = new IteratorProjectTypeData(Projects.getProject());

    if (data.begin()) {
      do {
        int value = data.getValue();
        if (value > maxValue) {
          maxValue = value;
        }
        typesCount++;
      } while (data.move());
    }
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    if (typesCount > 0) {
      fillGrid(buffer);

      buffer.append("<g style='stroke:none;fill:green'>");
  
      data.begin();
      for (int x = 0; x < typesCount; x++) {
        double height = ((double) data.getValue()) * CHART_HEIGHT / maxValue;
        buffer.append("<rect x='");
        buffer.append(x);
        buffer.append(".1' width='0.8' y='");
        buffer.append(String.valueOf(CHART_HEIGHT - height));
        buffer.append("' height='");
        buffer.append(String.valueOf(height));
        buffer.append("' />");
        data.move();
      }
      buffer.append("</g>");

      fillTypes(buffer);
    }
  }

  public void fillViewBox(Request request, LocalizedStringBuffer buffer) {
    buffer.append("preserveAspectRatio='none' viewBox='0 0 ");
    buffer.append(String.valueOf(typesCount));
    buffer.append(" ");
    buffer.append(String.valueOf(CHART_HEIGHT));
    buffer.append("'");
  }

  private void fillGrid(LocalizedStringBuffer buffer) {
    buffer.append("<g style='fill:blue;fill-opacity:0.05'>");
    for (int x = 0; x < typesCount; x += 2) {
      buffer.append("<rect x='" + x + "' width='1' height='" + CHART_HEIGHT + "' />");
    }
    buffer.append("</g>");
  }

  private void fillTypes(LocalizedStringBuffer buffer) {
    data.begin();
    buffer.append("<g style='font-size:0.3pt;fill:black;fill-opacity:0.5'>");
    for (int x = 0; x < typesCount; x++) {
      buffer.append("<g transform='translate(");
      buffer.append(String.valueOf(x));
      buffer.append(",1) rotate(90)'>");
      buffer.append("<text>");
      buffer.localize(data.getType());
      buffer.append("</text>");
      buffer.append("</g>");
      data.move();
    }
    buffer.append("</g>");
  }

}