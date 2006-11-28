package astrolab.web.project.archive.natal;

import astrolab.db.Action;
import astrolab.db.EventIterator;
import astrolab.db.Personalize;
import astrolab.web.Display;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.component.ComponentSelectEvent;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayNatalRecords extends HTMLFormDisplay {

	public final static int ID = Display.getId(DisplayNatalRecords.class);

  public DisplayNatalRecords() {
    super(Action.getAction(-1, ID, -1));
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    buffer.localize("The Archive contains");
    buffer.append(" ");
    buffer.append(NatalRecord.count());
    buffer.append(" ");
		buffer.localize("records of births.");
    buffer.append("<hr />");

    // Search bar
    buffer.append("<input type='text' id='" + Request.TEXT_NAME + "' name='" + Request.TEXT_NAME + "' value='' title='");
    buffer.localize("Enter part of name or birthplace");
    buffer.append("' />");
    buffer.append("<input type='submit' value='");
    buffer.localize("Search");
    buffer.append("' />");

    buffer.append("<hr />");

    String text = request.get(Request.TEXT_NAME);

    if ((text != null) && (text.trim().length() > 0)) {
      ComponentSelectEvent.fill(buffer, request, EventIterator.iterate(text));
    } else {
      buffer.localize("Your favourites are:");

      int[] events = Personalize.getFavourites(ID);

      if ((request.getUser() > 0) && !contains(events, request.getUser())) {
        int[] new_events = new int[events.length + 1];
        System.arraycopy(events, 0, new_events, 1, events.length);
        new_events[0] = request.getUser();
        events = new_events;
      }

      ComponentSelectEvent.fill(buffer, request, events);
    }

	}

  private final static boolean contains(int[] events, int event) {
    for (int i = 0; i < events.length; i++) {
      if (events[i] == event) {
        return true;
      }
    }
    return false;
  }

}
