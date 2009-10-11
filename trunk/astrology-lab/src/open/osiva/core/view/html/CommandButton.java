package open.osiva.core.view.html;

import open.osiva.core.controller.ControllerCommandMethod;
import open.osiva.core.view.PageSourceBuilder;
import open.osiva.core.view.View;
import open.osiva.core.view.text.Labels;

public class CommandButton implements View {

  private ControllerCommandMethod controller;

  public CommandButton(ControllerCommandMethod controller) {
    this.controller = controller;
  }

  public void populateView(PageSourceBuilder page) {
    page.startLine("<script language='JavaScript'>");
    page.append("registerSelectionControl('");
    page.append(controller.getId());
    page.append("', '");
    page.append(controller.getCommandMethod().getParameterTypes()[0].getName());
    page.append("')");
    page.append("</script>");

    page.startLine("<input type='button' disabled='true' id='");
    page.append(controller.getId());
    page.append("' value='");
    page.append(getLabel());
    page.append("' onClick='call(\"");
    page.append(controller.getId());
    page.append("\", selection)' />");
  }

  private String getLabel() {
    return Labels.format(controller.getCommandMethod().getName());
  }

}