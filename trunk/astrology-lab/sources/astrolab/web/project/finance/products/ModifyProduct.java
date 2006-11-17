package astrolab.web.project.finance.products;

import astrolab.db.Text;
import astrolab.web.Modify;
import astrolab.web.server.Request;

public class ModifyProduct extends Modify {

	public void operate(Request request) {
    try {
      String name = request.get(Request.TEXT_NAME);
      if (Text.getId(name) == 0) {
        return;
      }
      int category = ComponentSelectProduct.retrieve(request);
      Product.store(name, category);
    } catch (Exception e) {
      e.printStackTrace();
    }
	}

}