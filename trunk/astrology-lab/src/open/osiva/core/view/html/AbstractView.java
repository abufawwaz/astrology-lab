package open.osiva.core.view.html;

import open.osiva.core.controller.Controller;
import open.osiva.core.view.PageSourceBuilder;
import open.osiva.core.view.View;

abstract class AbstractView implements View {

  protected Controller controller;

  protected AbstractView(Controller controller) {
    this.controller = controller;
  }

  public void populateView(PageSourceBuilder page) {
    page.startLine("<div id='");
    page.append(controller.getId());
    page.append("' onMouseOver=\"select('");
    page.append(controller.getId());
    page.append("', '");
    page.append(controller.getModel().getClass().getName());
    page.append("');return false;\">");

    page.goIn();
    populateContents(page);
    page.goOut();
    page.startLine("</div>");
  }

  protected abstract void populateContents(PageSourceBuilder page);

}