package astrolab.web.project.archive.location;

import astrolab.db.*;
import astrolab.web.Modify;
import astrolab.web.server.Request;

public class TransformEventSaveTime extends Modify {

	public void operate(Request request) {
    System.err.println("SAVE TIME not implemented!");
		int event = Event.getSelectedEvent().getEventId();
//		buffer.append("transform event save time not implemented");
//		EventModifier.modifyTime(event,
//				Integer.parseInt(request.get(Request.TEXT_INPUT_6)),
//				Integer.parseInt(request.get(Request.TEXT_INPUT_5)),
//				Integer.parseInt(request.get(Request.TEXT_INPUT_4)),
//				Integer.parseInt(request.get(Request.TEXT_INPUT_1)),
//				Integer.parseInt(request.get(Request.TEXT_INPUT_2)),
//				Integer.parseInt(request.get(Request.TEXT_INPUT_3))
//	  );
	}

}