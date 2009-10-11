package open.osiva.core.view.html;

import open.osiva.core.controller.ControllerActionMethod;
import open.osiva.core.view.PageSourceBuilder;
import open.osiva.core.view.View;
import open.osiva.core.view.text.Labels;

public class ActionButton implements View {

  private ControllerActionMethod controller;

  public ActionButton(ControllerActionMethod controller) {
    this.controller = controller;
  }

  public void populateView(PageSourceBuilder page) {
    page.startLine("<input type='button' id='");
    page.append(controller.getId());
    page.append("' value='");
    page.append(getLabel());
    page.append("' onClick='call(\"");
    page.append(controller.getId());
    page.append("\")' />");
  }

  private String getLabel() {
    return Labels.format(controller.getActionMethod().getName());
  }

}