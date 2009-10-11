package open.osiva.core.persistency;

public class Persistence {

  private static PersistenceSpi spi = new InMemoryPersistence();

  public static void read(String key, Object object) {
    System.out.println("[PERSIST] read: " + key);
    Memento persisted = spi.read(key);
    if (persisted != null) {
      try {
        persisted.populate(object);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static void write(String key, Object value) {
    System.out.println("[PERSIST] write: " + key);
    spi.write(key, new Memento(value));
  }

}