package open.osiva.core.controller;

import java.lang.reflect.Method;

import open.osiva.core.log.Log;

public class ControllerGetterMethod extends Controller {

  private Method method;

  public ControllerGetterMethod(String id, Method method) {
    super(id, null);

    this.method = method;
    this.setModifiable(false);
  }

  public Object getModel() {
    try {
      return method.invoke(ControllerRegistry.getParent(this).getModel());
    } catch (Exception e) {
      Log.log("Unable to get data for " + getId(), e);
      return null;
    }
  }

}