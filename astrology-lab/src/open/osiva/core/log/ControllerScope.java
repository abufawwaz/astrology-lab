package open.osiva.core.log;

import open.osiva.core.controller.Controller;

public class ControllerScope {

  public static void show(Controller controller) {
    Log.log("[controller: " + controller);
    Log.log("\tmodel: " + controller.getModel());
    Log.log("\tview: " + controller.getView());
    Log.log("]");
  }

}