package open.osiva.core.view.html;

import open.osiva.core.controller.Controller;
import open.osiva.core.view.PageSourceBuilder;

public class ReadonlyPrimitiveView extends AbstractView {

  public ReadonlyPrimitiveView(Controller controller) {
    super(controller);
  }

  public void populateContents(PageSourceBuilder page) {
    page.startLine(controller.getModel());
  }

}