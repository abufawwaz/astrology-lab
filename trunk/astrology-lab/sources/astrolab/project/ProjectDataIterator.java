package astrolab.project;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import astrolab.astronom.SpacetimeEvent;

public class ProjectDataIterator {

  private int size = 0;
  private Project project;
  private ResultSet data;
  private ArrayList types = new ArrayList();

  ProjectDataIterator(Project project, ResultSet data) {
    this.project = project;
    this.data = data;

    // determine the timespan
    if (begin()) {
      size = 1;
      while (move()) {
        size++;
      }
      previous();
    }
  }

  public int size() {
    return size;
  }

  public Object get(String key) {
    Object object = null;

    try {
      object = data.getObject(project.normalizeKey(key));
    } catch (SQLException e) {
      if ((object == null) && "time".equals(key)) {
        try {
          object = data.getObject("event_time");
        } catch (SQLException e1) {
          e1.printStackTrace();
        }
      } else {
        e.printStackTrace();
      }
    }

    if (object instanceof java.sql.Timestamp) {
      return new SpacetimeEvent(((java.sql.Timestamp) object).getTime(), SpacetimeEvent.GMT_TIME_ZONE); // TODO: check that project records are stored with GMT
    } else {
      return object;
    }
  }

  public double getNumeric(String key) {
    Object raw = get(key);

    if (raw == null) {
      return 0;
    } else if (raw instanceof Number) {
      return ((Number) raw).doubleValue();
    } else if (raw instanceof SpacetimeEvent) {
      return ((SpacetimeEvent) raw).getTimeInMillis();
    } else {
      int index = types.indexOf(raw);

      if (index < 0) {
        index = types.size();
        types.add(raw);
      }

      return index;
    }
  }

  public String getString(String key) {
    Object raw = get(key);
    return (raw != null) ? String.valueOf(raw) : null;
  }

  public SpacetimeEvent getTime() {
    return (SpacetimeEvent) get("time");
  }

  public boolean begin() {
    try {
      if (data != null) {
        data.beforeFirst();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return move();
  }

  public boolean move() {
    try {
      if ((data != null) && (data.next())) {
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  public boolean previous() {
    try {
      if ((data != null) && (data.previous())) {
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  public void close() {
    if (data != null) {
      try {
        data.close();
        data = null;
      } catch (SQLException e) {
      }
    }
  }
}