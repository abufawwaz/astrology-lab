package astrolab.project.archive;

import astrolab.db.Event;
import astrolab.db.EventIterator;
import astrolab.db.Personalize;
import astrolab.web.Display;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.component.ComponentSelectText;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayArchiveRecordList extends HTMLFormDisplay {

  private final static String KEY_SEARCH_TEXT = "astrolab.web.project.archive.natal.DisplayNatalRecord.search";

  public final static int ID = Display.getId(DisplayArchiveRecordList.class);

  public DisplayArchiveRecordList() {
    super("Archive Search", ID, true);
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    String searchText = getSearchText(request);

    // Search bar
    buffer.append("<table width='100%'>");
    buffer.append("<tr>");
    buffer.append("<td class='class_input'>");
    ComponentSelectText.fill(buffer, KEY_SEARCH_TEXT, searchText, "Enter part of name, birthplace or a keyword.");
    buffer.append("</td>");
    buffer.append("<td>");
    super.addSubmit(buffer, "Go");
    buffer.append("</td>");
    buffer.append("</tr>");
    buffer.append("</table>");
    buffer.append("<hr />");

    // Result bar
    EventIterator iterator = EventIterator.iterate(searchText);
    if (iterator.hasNext()) {
      list(request, buffer, iterator);
    } else {
      buffer.localize("No results found.");
      buffer.localize("Try a new search!");
    }
  }

  private final static String getSearchText(Request request) {
    String text = request.get(KEY_SEARCH_TEXT);

    if ((text != null) && (text.trim().length() > 0)) {
      return text;
    } else {
      return ""; // get it from the database and if there is none get the current name or keyword 'popular'
    }
  }

  private final static void list(Request request, LocalizedStringBuffer buffer, EventIterator iterator) {
    while (iterator.hasNext()) {
      buffer.append("\r\n");
      Event event = (Event) iterator.next();
      String controlId = "natal_record_" + event.getId(); 

//      ComponentController.fill(buffer, controlId, "Event", String.valueOf(event.getId()));
      buffer.append("<div id='");
      buffer.append(controlId);
      buffer.append("' title='");
      buffer.localize(event.getSubject());
      buffer.append("; "); //TODO: add a new line here
      buffer.append(event.toSimpleString());
      buffer.append("; ");
      buffer.localize(event.getLocation().getId());
      buffer.append("'>");

      // TODO: put this into a separate class
      buffer.append("<a href='javascript:top.fireEvent(window, \"event\",\"" + event.getId() + "\")'>");
      if (event.getId() == Personalize.getUser()) {
        buffer.append("(");
        buffer.localize("You");
        buffer.append(") ");
      }
      buffer.localize(event.getId());
      buffer.append("</a>");

      buffer.append("</div>");
    }
  }
}
