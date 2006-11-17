package astrolab.web.project.archive.test;

import java.util.Vector;

import astrolab.db.Event;
import astrolab.db.EventIterator;
import astrolab.web.SVGDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class Test2Statistics extends SVGDisplay {

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    int test = 2; //TODO: hmmmm
    int user = request.getUser();
    EventIterator iterator = null; //EventIterator.iterateSimilarSubject("Test" + test + ":" + user + ":", EventIterator.SORT_BY_DATE_ASC, 0);
    Event event;
    Vector times = new Vector();
    double time;
    double maxtime = 0;
    double sum = 0;

    while (iterator.hasNext()) {
      event = (Event) iterator.next();
      time = event.getTime().getTimeInMillis();
      times.add(new Double(time));
      sum += time;
      if (time > maxtime) {
        maxtime = time;
      }
    }

    double x = 10;
    double stepx = 80.0 / times.size();
    double stepy = 50.0 / maxtime;
    buffer.append("<path d=\"M");
    for (int i = 0; i < times.size(); i++, x += stepx) {
      if (i > 0) buffer.append("L");
      buffer.append((int) x);
      buffer.append(" ");
      buffer.append((int) (70 - ((Double) times.get(i)).doubleValue() * stepy));
    }
    buffer.append("\" style=\"stroke:black;stroke-width:0.1;fill:none\" />");

    int average = (int) (70 - (sum / times.size()) * stepy);
    buffer.append("<path d=\"M10 " + average + "L90 " + average + "\" style=\"stroke:blue;stroke-width:0.1;fill:none\" />");
    buffer.append("<rect x='1' y='1' width='99' height='99' style='stroke:red;stroke-width:1;fill:none' />");
	}

}
