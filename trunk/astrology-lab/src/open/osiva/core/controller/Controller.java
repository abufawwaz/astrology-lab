package open.osiva.core.controller;

import open.osiva.core.view.View;
import open.osiva.core.view.ViewFactory;

/**
 * A controller is part of the MVC model used by OSIVA.
 * It links the models - instances of Java objects - to the views.
 */
public class Controller {

  public final static char PATH_DELIMITER = '.';
  public final static char SPECIAL_DELIMITER = '*';

  private String id;
  private Object model;
  private View view;

  private boolean isModifiable = true;

  public Controller(String id, Object model) {
    this.id = id;
    this.model = model;

    ControllerRegistry.put(id, this);
  }

  public String getId() {
    return id;
  }

  // Used only when re-indexing collections
  void setId(String newId) {
    this.id = newId;
  }

  public boolean isModifiable() {
    return isModifiable;
  }

  public void setModifiable(boolean isModifiable) {
    this.isModifiable = isModifiable;
  }

  public Object getModel() {
    return model;
  }

  /**
   *   Sets the value of the model.
   *   Subclasses that represent Java methods will invoke the method with
   * the argument.
   *
   * @param args - Java object that is the new value of the model.
   */
  public void setModel(Object args) {
    this.model = args;
  }

  public View getView() {
    if (view == null) {
      view = ViewFactory.createView(this);
    }

    return view;
  }

  protected void setView(View view) {
    this.view = view;
  }

  public Iterable<Controller> children() {
    return ControllerRegistry.getChildren(id);
  }

  public String toString() {
    return "[" + hashCode() + ":" + getId() + ": " + getModel() + "]";
  }

}