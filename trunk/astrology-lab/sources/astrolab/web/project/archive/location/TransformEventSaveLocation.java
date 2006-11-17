package astrolab.web.project.archive.location;

import astrolab.db.*;
import astrolab.web.Modify;
import astrolab.web.server.Request;

public class TransformEventSaveLocation extends Modify {

	public void operate(Request request) {
		int event = Event.getSelectedEvent().getEventId();
		EventModifier.modifyLocation(event, request.getSelection()[0]);
	}

}