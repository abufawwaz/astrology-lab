package astrolab.project.relocation;

import astrolab.db.Text;
import astrolab.web.HTMLDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayRelocationRecords extends HTMLDisplay {

  public DisplayRelocationRecords() {
    super.addAction("relocation.new", "reload");
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    IteratorRelocationRecords iterator = IteratorRelocationRecords.iterate(request.getUser());

    buffer.append("<div class='class_title'>");
    buffer.append("Relocations of ");
    buffer.append(Text.getText(request.getUser()));
    buffer.append("</div>");

    buffer.append("<table>");
    while (iterator.hasNext()) {
      buffer.append("<tr>");
			RelocationRecord relocation = (RelocationRecord) iterator.next();

      buffer.append("<td>");
			buffer.localize(relocation.getLocation().getId());
      buffer.append("</td>");
      buffer.append("<td>");
      buffer.append(relocation.toSimpleString());
      buffer.append("</td>");
      buffer.append("</tr>");
		}
    buffer.append("</table>");
	}

}
