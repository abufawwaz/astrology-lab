package open.osiva.core.controller;

import open.osiva.core.view.html.Label;

public class ControllerLabel extends Controller {

  public ControllerLabel(String id, String label) {
    super(id + Controller.SPECIAL_DELIMITER + "label", label);
    this.setModifiable(false);
    this.setView(new Label(this));
  }

  public String getModel() {
    return (String) super.getModel();
  }

}