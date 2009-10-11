package open.osiva.core.view.html;

import open.osiva.core.controller.ControllerLabel;
import open.osiva.core.view.PageSourceBuilder;
import open.osiva.core.view.View;
import open.osiva.core.view.text.Labels;

public class Label implements View {

  private ControllerLabel controller;

  public Label(ControllerLabel controller) {
    this.controller = controller;
  }

  public void populateView(PageSourceBuilder page) {
    page.append(getLabel());
  }

  private String getLabel() {
    return Labels.format((String) controller.getModel());
  }

}