package open.osiva.core.view.html;

import open.osiva.core.controller.Controller;
import open.osiva.core.view.PageSourceBuilder;
import open.osiva.core.view.View;

public class ModifiableNumberView implements View {

  private Controller controller;

  public ModifiableNumberView(Controller controller) {
    this.controller = controller;
  }

  public void populateView(PageSourceBuilder page) {
    page.startLine("<input type='text' name='a' id='");
    page.append(controller.getId());
    page.append("' value='");
    page.append(controller.getModel());
    page.append("' />");
  }

}