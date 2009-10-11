package open.osiva.core.controller;

import java.util.ArrayList;
import java.util.Collection;

import open.osiva.core.view.html.ReadonlyCollectionView;

public abstract class ControllerCollection extends Controller {

  public ControllerCollection(String id, Collection<?> model) {
    super(id, model);
    this.setView(new ReadonlyCollectionView(this));
  }

  public Collection<?> getModel() {
    return (Collection<?>) super.getModel();
  }

  public Iterable<Controller> children() {
    synchronizeChildControllers();
    return super.children();
  }

  public void synchronizeChildControllers() {
    ArrayList<Controller> children = new ArrayList<Controller>();
    for (Controller c: super.children()) {
      children.add(c);
    }

    int index = -1;
    for (Object o: getModel()) {
      index++;

      boolean found = false;
      for (Controller child: children) {
        if (child.getModel() == o) {
          if (!child.getId().endsWith("." + index)) {
            updateRegistry(getId() + Controller.PATH_DELIMITER + index, child);
          }

          found = true;
          break;
        }
      }

      if (!found) {
        Controller c = ControllerFactory.createController(getId() + Controller.PATH_DELIMITER + index, o);
        ControllerFactory.createControllerTree(c);
      }
    }

    // Remove all extra children
    index++;
    for (; index < children.size(); index++) {
      ControllerRegistry.put(getId() + Controller.PATH_DELIMITER + index, null);
    }
  }

  private void updateRegistry(String newId, Controller c) {
    for (Controller child: c.children()) {
      String childId = child.getId().substring(c.getId().length()); 
      updateRegistry(newId + childId, child);
    }

    ControllerRegistry.put(c.getId(), null);
    ControllerRegistry.put(newId, c);
    c.setId(newId);

    // Clear the current view as it may contains indices of another position in the collection
    c.setView(null);
  }
}