package astrolab.web.component;

import astrolab.web.HTMLDisplay;
import astrolab.web.server.Request;

public abstract class Selectable extends HTMLDisplay {

	private int id;

	protected Selectable(int id) {
		this.id = id;
	}

	protected void wrap(Request request, StringBuffer buffer, String data) {
    boolean isSelected = request.isSelected(new int[] { id });
    buffer.append("<a href='");
    buffer.append("javascript:execute(" + id + ", null)");
    buffer.append("'>");
    if (isSelected) {
    	buffer.append("<b><font color='red'>");
    }
    buffer.append(data);
    if (isSelected) {
    	buffer.append("</font></b>");
    }
    buffer.append("</a>");
	}

}