package astrolab.web.project.archive.relocation;

import astrolab.db.Event;
import astrolab.db.Text;
import astrolab.web.HTMLDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayRelocationRecords extends HTMLDisplay {

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    Event selectedEvent = Event.getSelectedEvent();
    int selectedEventId = (selectedEvent != null) ? selectedEvent.getEventId() : -1;
    IteratorRelocationRecords iterator = IteratorRelocationRecords.iterate(request.getUser());

    buffer.append("Relocations of ");
    buffer.append(Text.getText(request.getUser()));
    buffer.append(":<hr />");

    buffer.append("<table><tr><th>Relocated to</th><th>At</th><th>Accuracy</th><th>Source</th></tr>");
    while (iterator.hasNext()) {
      buffer.append("<tr>");
			RelocationRecord relocation = (RelocationRecord) iterator.next();
      boolean isSelected = request.isSelected(new int[] { relocation.getEventId() }) || (selectedEventId == relocation.getEventId());

      buffer.append("<td>");
      buffer.append("<a href=\"javascript:top.select(new Array('");
      buffer.append(relocation.getEventId());
      buffer.append("'))\">");
      if (isSelected) {
        buffer.append("<b><font color='green'>");
      }
			buffer.append(Text.getText(relocation.getLocation().getId()));
      if (isSelected) {
        buffer.append("</font></b>");
      }
      buffer.append("</a>");
      buffer.append("</td>");
      buffer.append("<td>");
      buffer.append(relocation.getTime().toString());
      buffer.append("</td>");
      buffer.append("<td>");
      buffer.append(relocation.getAccuracy());
      buffer.append("</td>");
      buffer.append("<td>");
      buffer.append(relocation.getSource());
      buffer.append("</td>");
      buffer.append("</tr>");
		}
    buffer.append("</table>");
	}

}
