package open.osiva.core.persistency;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 *  The class remembers the contents of an instance of an object.
 * It is used to check whether this instance has changed and need
 * to persist the new contents.
 */
public class Memento extends HashMap<String, Integer> {

  private static final long serialVersionUID = 1L;

  private boolean isChanged = false;

  public Memento(Object object) {
    if (object instanceof String) {
      initializeString(object);
    } else if (object != null) {
      try {
        initializeObject(object);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public boolean isChanged(Object object) {
    if (isChanged) {
      return true;
    }

    if (object != null) {
      isChanged = !equals(new Memento(object));
    }

    return isChanged;
  }

  public void populate(Object object) throws IllegalAccessException {
    Class<?> c = object.getClass();

    for (Field f: c.getDeclaredFields()) {
      if (containsKey(f.getName())) {
        f.setAccessible(true);
        f.set(object, get(f.getName()));
      }
    }
  }

  private final void initializeString(Object object) {
   put("value", object.hashCode());
  }

  private final void initializeObject(Object object) throws IllegalArgumentException, IllegalAccessException {
    Class<?> c = object.getClass();

    for (Field f: c.getDeclaredFields()) {
      f.setAccessible(true);
      Object v = f.get(object);

      if (v != null) {
        put(f.getName(), v.hashCode());
      }
    }
  }
}