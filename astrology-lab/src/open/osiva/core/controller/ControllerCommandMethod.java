package open.osiva.core.controller;

import java.lang.reflect.Method;

import open.osiva.core.log.Log;
import open.osiva.core.view.html.CommandButton;

public class ControllerCommandMethod extends Controller {

  private Method method;

  public ControllerCommandMethod(String id, Method method) {
    super(id, null);

    this.method = method;
    this.setView(new CommandButton(this));
  }

  public Method getCommandMethod() {
    return method;
  }

  public void call(String argId) {
    try {
      method.invoke(ControllerRegistry.getParent(this).getModel(), ControllerRegistry.get(argId).getModel());
    } catch (Exception e) {
      Log.log("Unable to call method of " + getId(), e);
    }
  }

}