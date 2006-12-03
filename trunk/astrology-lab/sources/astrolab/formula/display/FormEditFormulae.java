package astrolab.formula.display;

import astrolab.db.Action;
import astrolab.formula.Element;
import astrolab.formula.ElementSet;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.Modify;
import astrolab.web.component.ComponentSelectChoice;
import astrolab.web.component.ComponentSelectNumber;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class FormEditFormulae extends HTMLFormDisplay {

  private final static Element[] ELEMENTS = ElementSet.getDefault().getElements();
  private final static Object[] ELEMENT_IDS;

  static {
    ELEMENT_IDS = new Object[ELEMENTS.length];
    for (int i = 0; i < ELEMENT_IDS.length; i++) {
      ELEMENT_IDS[i] = new Integer(ELEMENTS[i].getId());
    }
  }

  public FormEditFormulae() {
    super(Action.getAction(-1, -1, Modify.getId(ModifyFormulae.class)));
  }

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("<table border='0'>");

    String[] slots = new String[10];
    for (int i = 0; i < slots.length; i++) {
      slots[i] = String.valueOf(i);
    }
    buffer.append("<tr>");
    buffer.append("<td>");
    buffer.localize("Slot");
    buffer.append("</td>");
    buffer.append("<td>");
    ComponentSelectChoice.fill(buffer, slots, slots[0], "formulae_slot");
    buffer.append("</td>");
    buffer.append("</tr>");

    for (int i = 0; i < 6; i++) {
      buffer.append("<tr>");
      buffer.append("<td>");
      ComponentSelectNumber.fill(buffer, "formulae_element_" + i + "_coefficient");
      buffer.append("</td>");
      buffer.append("<td>");
      ComponentSelectChoice.fill(buffer, ELEMENTS, ELEMENT_IDS, ELEMENTS[0], "formulae_element_" + i);
      buffer.append("</td>");
      buffer.append("</tr>");
    }

    buffer.append("</table>");
    buffer.append("<input type='submit' value='");
    buffer.localize("Save");
    buffer.append("' />");
	}

}