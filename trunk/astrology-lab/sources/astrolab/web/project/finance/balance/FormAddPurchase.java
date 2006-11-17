package astrolab.web.project.finance.balance;

import astrolab.db.Action;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.component.ComponentSelectChoice;
import astrolab.web.component.ComponentSelectNumber;
import astrolab.web.component.time.ComponentSelectTime;
import astrolab.web.project.finance.products.ComponentSelectProduct;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class FormAddPurchase extends HTMLFormDisplay {

  public FormAddPurchase() {
    super(Action.getAction(-1, -1, ModifyPurchase.getId(ModifyPurchase.class)));
  }

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("<table border='0'>");

    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Subject");
    buffer.append(":</td>");
    buffer.append("<td>");
    buffer.localize(request.getUser());
    buffer.append("</td>");
    buffer.append("</tr>");

    buffer.append("<tr>");
    buffer.append("<td>");
    ComponentSelectChoice.fill(buffer, new String[] { "buy", "sell" }, null, "_operation");
    buffer.append(":</td>");
    buffer.append("<td>");
    ComponentSelectProduct.fill(buffer);
    buffer.append("</td>");
    buffer.append("</tr>");

    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Quantity");
    buffer.append(":</td>");
    buffer.append("<td>");
    ComponentSelectNumber.fill(buffer, "_quantity");
    ComponentSelectChoice.fill(buffer, new String[] { "item", "kilogram", "liter" }, null, "_measure");
    buffer.append("</td>");
    buffer.append("</tr>");

    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Price");
    buffer.append(":</td>");
    buffer.append("<td>");
    ComponentSelectNumber.fill(buffer, "_price");
    ComponentSelectChoice.fill(buffer, new String[] { "BGN", "EUR", "USD" }, null, "_currency");
    buffer.append("</td>");
    buffer.append("</tr>");

    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Time");
    buffer.append(":</td>");
    buffer.append("<td>");
    ComponentSelectTime.fill(buffer, Request.TEXT_DATE);
    buffer.append("</td>");
    buffer.append("</tr>");

    buffer.append("</table>");
    buffer.append("<input type='submit' value='");
    buffer.localize("Save");
    buffer.append("'>");
	}

}