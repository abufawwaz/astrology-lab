package astrolab.project;

import astrolab.web.HTMLDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayRecordsTable extends HTMLDisplay {

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    Project project = Projects.getProject();
    ProjectDataKey[] keys = project.getKeys();

    buffer.append("<table border='1'>");
    buffer.append("<tr>");
    for (int i = 0; i < keys.length; i++) {
      buffer.append("<th>");
      buffer.localize(keys[i].getName());
      buffer.append("</th>");
    }
    buffer.append("</tr>");

    ProjectData data = project.getData();
    if (data.begin()) {
       do {
        buffer.append("<tr>");
        for (int i = 0; i < keys.length; i++) {
          buffer.append("<th>");
          buffer.localize(data.getString(keys[i].getName()));
          buffer.append("</th>");
        }
        buffer.append("</tr>");
      } while (data.move());
    }

    buffer.append("</table>");
  }

}