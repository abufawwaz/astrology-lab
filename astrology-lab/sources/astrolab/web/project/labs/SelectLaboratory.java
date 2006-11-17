package astrolab.web.project.labs;

import astrolab.db.Personalize;
import astrolab.db.Text;
import astrolab.web.Modify;
import astrolab.web.server.Request;

public class SelectLaboratory extends Modify {

	public void operate(Request request) {
		int[] selection = request.getSelection();

    if ((selection.length > 0) && (selection[0] > 3000000) && (selection[0] < 3005000)) { // TODO: check if laboratory
  		Personalize.addFavourite(-1, selection[0], Text.getId("user.session.laboratory"));
      Personalize.addFavourite(-1, Personalize.VALUE_NULL, Text.getId("user.session.project"));
    }
	}

}
