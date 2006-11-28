package astrolab.project.statistics;

import astrolab.astronom.Time;
import astrolab.web.SVGDisplay;
import astrolab.web.project.archive.relocation.RelocationRecord;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentChartStatistics extends SVGDisplay {

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    int user = request.getUser();
    // TODO: fix this!
    int location = RelocationRecord.getLocationOfPerson(user, new Time(request.get("_select_from_time"), 0).getTimeInMillis());
    Time from_time = new Time(request.get("_select_from_time"), location);
    Time to_time = new Time(request.get("_select_to_time"), location);

//    StatisticsRecordIterator iterator = StatisticsRecordIterator.iterate(from_time, to_time);

    int size = 0;
//    while (iterator.hasNext()) {
//      iterator.next();
//      size++;
//    }

    buffer.append("<svg:svg version=\"1.1\" baseProfile=\"full\" width=\"300px\" height=\"300px\">");
    buffer.append("<svg:circle cx=\"150px\" cy=\"100px\" r=\"50px\" fill=\"#ff0000\" stroke=\"#000000\" stroke-width=\"5px\"/>");
//    buffer.append("<svg:rect width=\"100\" height=\"100\" style=\"stroke:black;stroke-width:3;fill:none\"></svg:rect>");
    buffer.append("<svg:text x='100' y='100'>");
    buffer.append(from_time.toSimpleString());
    buffer.append(" - ");
    buffer.append(to_time.toSimpleString());
    buffer.append("</svg:text>");
    buffer.append("</svg:svg>");

    buffer.append(size);
  }

}
