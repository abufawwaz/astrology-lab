package astrolab.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class RecordIterator {

  protected ResultSet set;
  private boolean moved = false;

  protected RecordIterator(ResultSet set) {
    this.set = set;
  }

  protected abstract Object read() throws Exception;

  public final boolean hasNext() {
    if (moved) {
      return true;
    }
    try {
      if ((set != null) && (set.next())) {
        moved = true;
        return true;
      } else {
        close();
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public Object next() {
    if (set != null) {
      try {
        return read();
      } catch (Throwable e) {
        e.printStackTrace();
      } finally {
        moved = false;
      }
    }
    return null;
  }

  public final void close() {
  	if (set != null) {
  		try {
				set.close();
        moved = false;
	  		set = null;
			} catch (SQLException e) {
			}
  	}
  }
}