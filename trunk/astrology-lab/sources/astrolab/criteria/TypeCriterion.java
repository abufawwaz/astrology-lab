package astrolab.criteria;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public abstract class TypeCriterion {

  private final static TypeCriterion[] TYPES = new TypeCriterion[] {
    null
  };

  public final static Map<String, Set<String>> getCriteria(String[] data) {
    String[] keys;
    Hashtable<String, Set<String>> result = new Hashtable<String, Set<String>>();

    for (int type = 0; type < TYPES.length; type++) {
      keys = match(data, TYPES[type].getActorTypes());
      if (keys != null) {
        put(result, TYPES[type].getName(), keys);
      }
    }
    return null;
  }

  public abstract String getName();

  public abstract String[] getActorTypes();

  private final static String[] match(String[] data, String[] actorTypes) {
    for (int i = 0; i < data.length; i++) {
      boolean found = false;
      for (int j = 0; j < actorTypes.length; j++) {
        if (data[i].equals(actorTypes[j])) {
          found = true;
          break;
        }
      }
      if (!found) {
        return null;
      }
    }

    HashSet<String> result = new HashSet<String>();
    for (int i = 0; i < actorTypes.length; i++) {
      boolean found = false;
      for (int j = 0; j < data.length; j++) {
        if (data[j].equals(actorTypes[i])) {
          found = true;
          break;
        }
      }
      if (!found) {
        result.add(actorTypes[i]);
      }
    }
    return result.toArray(new String[0]);
  }

  private final static void put(Map<String, Set<String>> map, String criterionType, String[] actorTypes) {
    Set<String> result = map.get(criterionType);
    if (result == null) {
      result = new HashSet<String>();
      map.put(criterionType, result);
    }

    for (int i = 0; i < actorTypes.length; i++) {
      result.add(actorTypes[i]);
    }
  }
}