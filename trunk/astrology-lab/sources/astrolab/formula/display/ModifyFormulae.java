package astrolab.formula.display;

import java.util.ArrayList;

import astrolab.formula.Element;
import astrolab.formula.Formulae;
import astrolab.web.Modify;
import astrolab.web.component.ComponentSelectChoice;
import astrolab.web.component.ComponentSelectNumber;
import astrolab.web.server.Request;

public class ModifyFormulae extends Modify {

  private final static Element[] EMPTY_LIST = new Element[0];

  public void operate(Request request) {
    try {
      int index = 0;
      double formulae_coefficient;
      String slot = ComponentSelectChoice.retrieve(request, "formulae_slot");
      String formulae_element;
      ArrayList<Element> elements = new ArrayList<Element>();

      while (true) {
        formulae_element = ComponentSelectChoice.retrieve(request, "formulae_element_" + index);
        if ((formulae_element == null) || (formulae_element.trim().length() == 0)) {
          break;
        }

        formulae_coefficient = ComponentSelectNumber.retrieve(request, "formulae_element_" + index + "_coefficient");
        if (formulae_coefficient == 0) {
          break;
        }

        elements.add(new Element(Integer.parseInt(formulae_element), formulae_coefficient));

        index++;
      }

      Formulae.store(Integer.parseInt(slot), new Formulae(elements.toArray(EMPTY_LIST)));
    } catch (Exception e) {
      e.printStackTrace();
    }
	}

}