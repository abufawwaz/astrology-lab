package astrolab.web.project.finance.balance;

import astrolab.astronom.Time;
import astrolab.db.Text;
import astrolab.web.Modify;
import astrolab.web.component.ComponentSelectChoice;
import astrolab.web.component.ComponentSelectNumber;
import astrolab.web.project.archive.relocation.RelocationRecord;
import astrolab.web.project.finance.products.ComponentSelectProduct;
import astrolab.web.server.Request;

public class ModifyPurchase extends Modify {

	public void operate(Request request) {
    try {
      String name = request.get(Request.TEXT_NAME);
      if (Text.getId(name) == 0) {
        return;
      }
      int user = request.getUser();
      String operation = ComponentSelectChoice.retrieve(request, "_operation");
      int item_type = ComponentSelectProduct.retrieve(request);
      double price = ComponentSelectNumber.retrieve(request, "_price");
      String currency = ComponentSelectChoice.retrieve(request, "_currency");
      double quantity = ComponentSelectNumber.retrieve(request, "_quantity");
      String measure = ComponentSelectChoice.retrieve(request, "_measure");
      int location = RelocationRecord.getLocationOfPerson(user, new Time(request.get(Request.TEXT_DATE), 0).getTimeInMillis());
      long timestamp = new Time(request.get(Request.TEXT_DATE), location).getTimeInMillis();

      Purchase.store(user, operation, 0, item_type, price, currency, quantity, measure, timestamp);
    } catch (Exception e) {
      e.printStackTrace();
    }
	}

}