package astrolab.web.project.finance.balance;

import astrolab.astronom.SpacetimeEvent;
import astrolab.db.Text;
import astrolab.project.relocation.RelocationRecord;
import astrolab.web.Modify;
import astrolab.web.component.ComponentSelectChoice;
import astrolab.web.component.ComponentSelectNumber;
import astrolab.web.component.time.ComponentSelectTime;
import astrolab.web.project.finance.products.ComponentSelectProduct;
import astrolab.web.server.Request;
import astrolab.web.server.RequestParameters;

public class ModifyPurchase extends Modify {

	public void operate(Request request) {
    try {
      String name = request.get(RequestParameters.TEXT_NAME);
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
      int location = RelocationRecord.getLocationOfPerson(user, new SpacetimeEvent(request.get(ComponentSelectTime.PARAMETER_KEY), 0).getTimeInMillis());
      long timestamp = new SpacetimeEvent(request.get(ComponentSelectTime.PARAMETER_KEY), location).getTimeInMillis();

      Purchase.store(user, operation, 0, item_type, price, currency, quantity, measure, timestamp);
    } catch (Exception e) {
      e.printStackTrace();
    }
	}

}