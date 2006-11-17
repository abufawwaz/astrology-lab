package astrolab.web.project.archive.location;

import java.util.Calendar;

import astrolab.db.Event;
import astrolab.web.HTMLDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class TransformEventEditTime extends HTMLDisplay {

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
		Calendar calendar = Calendar.getInstance();
		Event event = Event.getSelectedEvent();
		calendar.setTime(event.getTime().getTime());
		buffer.append("transform event edit time not implemented");
//		buffer.append("Time: <input id=\"" + Request.TEXT_INPUT_1 + "\" type='TEXT' name='" + Request.TEXT_INPUT_1 + "' value='" + calendar.get(Calendar.HOUR) + "' size='2' />");
//		buffer.append(" : ");
//		buffer.append("<input id=\"" + Request.TEXT_INPUT_2 + "\" type='TEXT' name='" + Request.TEXT_INPUT_2 + "' value='" + calendar.get(Calendar.MINUTE) + "' size='2' />");
//		buffer.append(" : ");
//		buffer.append("<input id=\"" + Request.TEXT_INPUT_3 + "\" type='TEXT' name='" + Request.TEXT_INPUT_3 + "' value='" + calendar.get(Calendar.SECOND) + "' size='2' />");
//		buffer.append("<br />");
//		buffer.append("Date: <input id=\"" + Request.TEXT_INPUT_4 + "\" type='TEXT' name='" + Request.TEXT_INPUT_4 + "' value='" + calendar.get(Calendar.DATE) + "' size='2' />");
//		buffer.append(" ");
//		buffer.append("<input id=\"" + Request.TEXT_INPUT_5 + "\" type='TEXT' name='" + Request.TEXT_INPUT_5 + "' value='" + (calendar.get(Calendar.MONTH) + 1) + "' size='2' />");
//		buffer.append(" ");
//		buffer.append("<input id=\"" + Request.TEXT_INPUT_6 + "\" type='TEXT' name='" + Request.TEXT_INPUT_6 + "' value='" + calendar.get(Calendar.YEAR) + "' size='4' />");
	}

}