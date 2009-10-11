package open.osiva.core.persistency;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import open.osiva.core.controller.Controller;
import open.osiva.core.controller.ControllerRegistry;

public class MementoRegistry {

  private static ThreadLocal<HashMap<String, Memento>> instance = new ThreadLocal<HashMap<String, Memento>>();

  public static void activate(boolean flag) {
    if (flag) {
      HashMap<String, Memento> memento = instance.get();
  
      if (memento != null) {
        memento.clear();
      } else {
        instance.set(new HashMap<String, Memento>());
      }
    } else {
      instance.remove();
    }
  }

  public static Memento getMemento(String id) {
    HashMap<String, Memento> memento = instance.get();

    return (memento != null) ? memento.get(id) : null;
  }

  public static void keep(Controller controller) {
    HashMap<String, Memento> memento = instance.get();

    if ((memento != null) && !memento.containsKey(controller.getId())) {
      memento.put(controller.getId(), new Memento(controller.getModel()));

      for (Controller c: controller.children()) {
        keep(c);
      }
    }
  }

  public static Collection<String> identifyChanged() {
    HashMap<String, Memento> memento = instance.get();
    Collection<String> result = new ArrayList<String>();

    if (memento != null) {
      for (Entry<String, Memento> entry: memento.entrySet()) {
        if (entry.getValue().isChanged(ControllerRegistry.get(entry.getKey()).getModel())) {
          result.add(entry.getKey());
        }
      }
    }
    
    return result;
  }

}