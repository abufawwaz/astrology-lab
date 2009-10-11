package open.osiva.core.persistency;

import java.util.HashMap;

public class InMemoryPersistence implements PersistenceSpi {

  private static HashMap<String, Memento> records = new HashMap<String, Memento>();

  public Memento read(String key) {
    return records.get(key);
  }

  public void write(String key, Memento value) {
    records.put(key, value);
    System.out.println("[In-MEMORY] " + records);
  }

}