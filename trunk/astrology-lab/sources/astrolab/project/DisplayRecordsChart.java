package astrolab.project;

import astrolab.db.Personalize;
import astrolab.db.Text;
import astrolab.web.SVGDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayRecordsChart extends SVGDisplay {

  private ProjectData data = null;

  protected int strokeWidth = 1;

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("<g style='fill:none;");
    buffer.append(decorateLine());
    buffer.append("'>");

    Project project = Projects.getProject();
    ProjectDataKey[] keys = project.getKeys();

    for (int k = 0; k < keys.length; k++) {
      if (data.begin()) {
        int y = (int) data.getNumeric(keys[k].getName());
        buffer.append("<polyline points='0,");
        buffer.append(y);
        buffer.append(" ");

        while (data.move()) {
          int x = (int) (data.getTime().getTimeInMillis() - data.getFromTime().getTimeInMillis());
          y = (int) data.getNumeric(keys[k].getName());
          buffer.append(x);
          buffer.append(",");
          buffer.append(y);
          buffer.append(" ");
        }
        buffer.append("' />");
      }
    }

    buffer.append("</g>");
  }

  public void fillViewBox(Request request, LocalizedStringBuffer buffer) {
    Project project = Projects.getProject();
    ProjectDataKey[] keys = project.getKeys();

    int maxValue = Personalize.getFavourite(-1, Text.getId("user.selection.y2"), -1);
    boolean yChanged = false;

    data = project.getData();

    while (data.move()) {
      int value;
      for (int i = 0; i < keys.length; i++) {
        value = (int) data.getNumeric(keys[i].getName());

        if (value > maxValue) {
          maxValue = value;
          yChanged = true;
        }
      }
    }

    int timespan = (int) (data.getToTime().getTimeInMillis() - data.getFromTime().getTimeInMillis());

    if (yChanged) {
      Personalize.addFavourite(-1, maxValue, Text.getId("user.selection.y2"));
    }

    buffer.append("preserveAspectRatio='none' viewBox='0 0 " + timespan + " " + maxValue + "'");
  }

  protected String decorateLine() {
    return "stroke:green;stroke-width:" + strokeWidth;
  }

}
