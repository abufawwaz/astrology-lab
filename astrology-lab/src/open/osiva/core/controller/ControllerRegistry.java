package open.osiva.core.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import open.osiva.core.persistency.MementoRegistry;

public final class ControllerRegistry {

  private static ThreadLocal<HashMap<String, Controller>> instance = new ThreadLocal<HashMap<String, Controller>>();

  public static void activate(boolean flag) {
    if (flag) {
      HashMap<String, Controller> controllers = instance.get();
  
      if (controllers != null) {
        controllers.clear();
      } else {
        instance.set(new HashMap<String, Controller>());
      }
    } else {
      instance.remove();
    }
  }

  public static Controller get(String id) {
    Controller c = instance.get().get(id);

    if (c != null) {
      MementoRegistry.keep(c);
    }

    return c;
  }

  public static void put(String key, Controller controller) {
    HashMap<String, Controller> controllers = instance.get();
    if (controller != null) {
      controllers.put(key, controller);
      controllers.put(controller.getId(), controller);
    } else {
      controllers.remove(key);
    }
  }

  public static Collection<Controller> getChildren(Controller controller) {
    return getChildren(controller.getId());
  }

  // TODO: Optimize this by maintaining an index tree
  public static Collection<Controller> getChildren(String id) {
    int idLength = id.length();
    ArrayList<Controller> result = new ArrayList<Controller>();

    for (String key: instance.get().keySet()) {
      if (key.startsWith(id)
          && (key.length() > idLength)
          && (key.charAt(idLength) == Controller.PATH_DELIMITER)
          && (key.lastIndexOf(Controller.PATH_DELIMITER) <= idLength)) {
        result.add(get(key));
      }
    }

    return result;
  }

  public static Controller getParent(Controller controller) {
    return getParent(controller.getId());
  }

  public static Controller getParent(String id) {
    return get(id.substring(0, id.lastIndexOf(Controller.PATH_DELIMITER)));
  }

}