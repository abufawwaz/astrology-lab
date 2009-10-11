package open.osiva.core.view.html;

import open.osiva.core.controller.Controller;
import open.osiva.core.view.PageSourceBuilder;

public class ControllerView extends AbstractView {

  public ControllerView(Controller controller) {
    super(controller);
  }

  protected void populateContents(PageSourceBuilder page) {
    page.startLine("<div style='border: 10px solid DDDDFF;'>");

    page.goIn();
    for (Controller child: controller.children()) {
      page.startLine("");
      child.getView().populateView(page);
    }
    page.goOut();
    page.startLine("</div>");
  }

}