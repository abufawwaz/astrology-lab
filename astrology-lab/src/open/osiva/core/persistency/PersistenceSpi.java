package open.osiva.core.persistency;

public interface PersistenceSpi {

  public Memento read(String key);

  public void write(String key, Memento value);

}