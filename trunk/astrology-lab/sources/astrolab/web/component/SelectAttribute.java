package astrolab.web.component;

import astrolab.db.Personalize;
import astrolab.web.Modify;
import astrolab.web.server.Request;

// Combine this and SelectEvent into personalize where the two components of the selection
// are put into personalization table - key, value
public class SelectAttribute extends Modify {

	public void operate(Request request) {
		int[] selection = request.getSelection();
		Personalize.addFavourite(-1, (selection.length > 1) ? selection[1] : Personalize.VALUE_NULL, Personalize.KEY_ATTRIBUTE);
	}

}
