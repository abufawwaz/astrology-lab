package astrolab.web.project.archive.test;

import astrolab.db.Personalize;
import astrolab.db.Text;
import astrolab.web.Modify;
import astrolab.web.server.Request;

public class StoreTestResult extends Modify {

	public void operate(Request request) {
		int[] selection = request.getSelection();

		if (selection.length > 0) {
			long timestamp = System.currentTimeMillis();
      int test = Personalize.getFavourite(-1, Text.getId("user.session.project"), -1);
			int person = request.getUser();

      TestRecord.store(test, person, timestamp, selection);
    }
	}

}
