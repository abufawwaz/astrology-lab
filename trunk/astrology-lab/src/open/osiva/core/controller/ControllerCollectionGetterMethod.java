package open.osiva.core.controller;

import java.lang.reflect.Method;
import java.util.Collection;

import open.osiva.core.log.Log;

public class ControllerCollectionGetterMethod extends ControllerCollection {

  private Method method;

  public ControllerCollectionGetterMethod(String id, Method method) {
    super(id, null);

    this.method = method;
    this.setModifiable(false);
  }

  public Collection<?> getModel() {
    try {
      return (Collection<?>) method.invoke(ControllerRegistry.getParent(this).getModel());
    } catch (Exception e) {
      Log.log("Unable to get data for " + getId(), e);
      return null;
    }
  }

}