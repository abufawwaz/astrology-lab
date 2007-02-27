package astrolab.formula.display;

import astrolab.db.Action;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.Modify;
import astrolab.web.component.ComponentSelectText;
import astrolab.web.component.ComponentSubmitButton;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class FormEditFormulae extends HTMLFormDisplay {

  public FormEditFormulae() {
    super(Action.getAction(-1, -1, Modify.getId(ModifyFormulae.class)));
  }

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    buffer.localize("Enter");
    buffer.localize(":");
    ComponentSelectText.fill(buffer, "formulae");
    ComponentSubmitButton.fill(buffer);
	}

}