package open.osiva.core;

import java.util.Collection;
import java.util.HashSet;

import open.osiva.core.controller.Controller;
import open.osiva.core.controller.ControllerActionMethod;
import open.osiva.core.controller.ControllerCommandMethod;
import open.osiva.core.controller.ControllerFactory;
import open.osiva.core.controller.ControllerRegistry;
import open.osiva.core.log.ControllerScope;
import open.osiva.core.log.Log;
import open.osiva.core.persistency.Memento;
import open.osiva.core.persistency.MementoRegistry;
import open.osiva.core.persistency.Persistence;
import open.osiva.core.view.PageSourceBuilder;

public class Center {

  private static ThreadLocal<String> USER = new ThreadLocal<String>();

  public static String getUser() {
    return USER.get();
  }

  public static void view(String user, PageSourceBuilder page, String service) {
    USER.set(user);
    MementoRegistry.activate(false);
    ControllerRegistry.activate(true);

    Log.log("view: " + service);
    Controller controller = locateController(service);

    // Populate the view
    controller.getView().populateView(page);
  }

  public static void call(String user, PageSourceBuilder page, String service, String args) {
    USER.set(user);
    MementoRegistry.activate(true);
    ControllerRegistry.activate(true);

    Log.log("call: " + service);
    Controller controller = locateController(service);

    // Call the correct action/command
    if (controller instanceof ControllerActionMethod) {
      ((ControllerActionMethod) controller).call();
    } else if (controller instanceof ControllerCommandMethod) {
      ((ControllerCommandMethod) controller).call(args);
    }

    // Identify all affected controllers
    Collection<String> affected = MementoRegistry.identifyChanged();

    // for each affected controller, persist the change and return
    // the new value to the client
    HashSet<String> toView = new HashSet<String>();
    for (String id: affected) {
      System.out.println("affected: " + id);
      Controller c = ControllerRegistry.get(id);
      Persistence.write(c.getId(), new Memento(c.getModel()));

      boolean isToView = true;
      for (String other: affected) {
        if ((id != other) && (id.startsWith(other))) {
          isToView = false;
          break;
        }
      }
      if (isToView) {
        toView.add(id);
      }
    }
    for (String id: toView) {
      System.out.println("view: " + id);
      Controller c = ControllerRegistry.get(id);
      c.getView().populateView(page);
    }

    Log.log(page.toString());
  }

  private static Controller locateController(String service) {
    System.out.println(" locate: " + service);
    Controller controller = ControllerRegistry.get(service);

    if (controller == null) {
      controller = ControllerFactory.createController(service);
    }

    ControllerScope.show(controller);

    return controller;
  }

}