package astrolab.web.project.finance.balance;

import astrolab.db.Action;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayPurchaseList extends HTMLFormDisplay {

  public DisplayPurchaseList() {
    super(Action.getAction(-1, HTMLFormDisplay.getId(DisplayPurchaseList.class), -1));
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    // Search bar
    buffer.append("<hr>");
    buffer.append(" ...");
    buffer.localize("Search");
    buffer.append(" ...");
    buffer.append("<hr>");

    buffer.append("<table>");
    buffer.append("<tr><th>Operation</th><th>Item</th><th>Quantity</th><th>Price</th><th>Time</th></tr>");

    PurchaseIterator iterator = PurchaseIterator.iterate(request.getUser());
    double sum_bgn = 0.0;
    double sum_eur = 0.0;
    double sum_usd = 0.0;

    while(iterator.hasNext()) {
      Purchase purchase = (Purchase) iterator.next();
      int gain_lose = "buy".equals(purchase.getOperation()) ? -1 : 1;
      String currency = purchase.getCurrency();
      if ("BGN".equals(currency)) {
        sum_bgn = sum_bgn + purchase.getPrice() * gain_lose;
      } else if ("EUR".equals(currency)) {
        sum_eur = sum_eur + purchase.getPrice() * gain_lose;
      } else if ("USD".equals(currency)) {
        sum_usd = sum_usd + purchase.getPrice() * gain_lose;
      }

      buffer.append("<tr>");

      buffer.append("<td>");
      buffer.localize(purchase.getOperation());
      buffer.append("</td>");
      buffer.append("<td>");
      buffer.localize(purchase.getType());
      buffer.append("</td>");
      buffer.append("<td>");
      buffer.append(purchase.getQuantity());
      buffer.append("&nbsp;");
      buffer.localize(purchase.getMeasure());
      buffer.append("</td>");
      buffer.append("<td>");
      buffer.append(purchase.getPrice());
      buffer.append("&nbsp;");
      buffer.localize(purchase.getCurrency());
      buffer.append("</td>");
      buffer.append("<td>");
      buffer.append(purchase.getTime().toSimpleString());
      buffer.append("</td>");

      buffer.append("</tr>");
    }

    buffer.append("<tr><th><hr /></th><th><hr /></th><th><hr /></th><th>");
    if (sum_bgn != 0.0) {
      buffer.append(sum_bgn);
      buffer.append("&nbsp;BGN");
    }
    if (sum_eur != 0.0) {
      buffer.append(sum_eur);
      buffer.append("&nbsp;BGN");
    }
    if (sum_usd != 0.0) {
      buffer.append(sum_usd);
      buffer.append("&nbsp;BGN");
    }
    buffer.append("</th><th><hr /></th></tr>");
    buffer.append("</table>");
	}

}
