package astrolab.web.project.finance.products;

import astrolab.db.Action;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class FormAddProduct extends HTMLFormDisplay {

  public FormAddProduct() {
    super(Action.getAction(-1, -1, ModifyProduct.getId(ModifyProduct.class)));
  }

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("<table border='0'>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Product");
    buffer.append(":</td>");
    buffer.append("<td><input id='" + Request.TEXT_NAME + "' type='text' name='" + Request.TEXT_NAME + "' value='");
    buffer.localize("... not set ...");
    buffer.append("' /></td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Category");
    buffer.append(":</td>");
    buffer.append("<td>");
    ComponentSelectProduct.fill(buffer);
    buffer.append("</td>");
    buffer.append("</tr>");
    buffer.append("</table>");
    buffer.append("<input type='submit' value='");
    buffer.localize("Save");
    buffer.append("' />");
	}

}