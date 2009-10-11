package open.osiva.core.controller;

import java.lang.reflect.Method;

import open.osiva.core.log.Log;
import open.osiva.core.view.html.ActionButton;

public class ControllerActionMethod extends Controller {

  private Method method;

  public ControllerActionMethod(String id, Method method) {
    super(id, null);

    this.method = method;
    this.setView(new ActionButton(this));
  }

  public Method getActionMethod() {
    return method;
  }

  public void call() {
    try {
      method.invoke(ControllerRegistry.getParent(this).getModel());
    } catch (Exception e) {
      Log.log("Unable to call method of " + getId(), e);
    }
  }

}