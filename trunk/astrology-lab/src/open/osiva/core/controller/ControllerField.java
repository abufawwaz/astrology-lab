package open.osiva.core.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import open.osiva.core.log.Log;

public class ControllerField extends Controller {

  private Field field;

  public ControllerField(String id, Field field) {
    super(id, null);

    this.field = field;
    this.setModifiable(!Modifier.isFinal(field.getModifiers()));
  }

  public Object getModel() {
    try {
      return field.get(ControllerRegistry.getParent(this).getModel());
    } catch (Exception e) {
      Log.log("Unable to get data for " + getId(), e);
      return null;
    }
  }

}