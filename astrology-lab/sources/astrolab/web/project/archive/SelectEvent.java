package astrolab.web.project.archive;

import astrolab.db.Event;
import astrolab.web.Modify;
import astrolab.web.server.Request;

public class SelectEvent extends Modify {

	public void operate(Request request) {
		int[] selection = request.getSelection();
    for (int i = 0; i < selection.length; i++) {
      if (selection[i] > 0) {
        Event.setSelectedEvent(selection[i], i + 1);
      }
    }
	}

}
