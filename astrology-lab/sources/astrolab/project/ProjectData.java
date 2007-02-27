package astrolab.project;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimeZone;

import astrolab.astronom.Time;
import astrolab.db.Database;
import astrolab.web.resource.CloseableResource;

public class ProjectData implements CloseableResource {

  private ResultSet data;

  private int size = 0;
  private Time fromTime;
  private Time toTime;

  ProjectData(Project project) {
    this(project, new String[0], null, project.getMinTime(), project.getMaxTime());
  }

  ProjectData(Project project, String[] keys, String grouping, Time fromTime, Time toTime) {
    // TODO: use fromTime and toTime within the SQL query
    StringBuffer keyselect = new StringBuffer();
    for (int i = 0; i < project.getKeys().length; i++) {
      if (grouping != null) {
        keyselect.append(project.getKeys()[i].getName());
      } else {
        keyselect.append(project.getKeys()[i].getName());
      }
      keyselect.append(", ");
    }
    for (int i = 0; i < keys.length; i++) {
      keyselect.append(keys[i]);
      keyselect.append(", ");
    }
    if (grouping != null) {
      keyselect.append(grouping);
      keyselect.append(", ");
    }
    keyselect.append("time"); // TODO: remove it :)

    String timing = "";
    if (fromTime != null || toTime != null) {
      timing = " WHERE";

      if (fromTime != null) {
        timing += " time >= '" + fromTime.toMySQLString() + "'";
      }

      if (toTime != null) {
        if (fromTime != null) {
          timing += " AND";
        }
        timing += " time <= '" + toTime.toMySQLString() + "'";
      }
    }

    int slots = 360; // allow change
    String groupby;
    if (grouping != null && !"time".equalsIgnoreCase(grouping)) {
      groupby = " GROUP BY floor(" + grouping + ")";
    } else {
      String fromSQLTime = fromTime.toMySQLString();
      String toSQLTime = toTime.toMySQLString();
      String dateDiff = "(datediff('" + toSQLTime + "', '" + fromSQLTime + "') / " + slots + ")";
      groupby = " GROUP BY floor(TO_DAYS(time) / " + dateDiff + ") * " + dateDiff;
    }

    this.data = Database.executeQuery("SELECT " + keyselect + " FROM " + Project.TABLE_PREFIX + project.getName() + timing + groupby + " LIMIT " + slots);

//    Resources.add(this);

    // determine the timespan
    if (begin()) {
      fromTime = getTime();
      size = 1;
      while (move()) {
        size++;
      }
      previous();
      toTime = getTime();
    } else {
      fromTime = toTime = new Time();
    }
  }

  public int getSize() {
    return size;
  }

  public Time getFromTime() {
    return fromTime;
  }

  public Time getToTime() {
    return toTime;
  }

  public Object get(String key) {
    try {
      Object object = data.getObject(key);
      if (object instanceof java.sql.Timestamp) {
        return new Time(((java.sql.Timestamp) object).getTime(), TimeZone.getDefault()); // TODO: get default time zone for project records
      } else {
        return object;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return "";
    }
  }

  public double getNumeric(String key) {
    Object raw = get(key);

    if (raw == null) {
      return 0;
    } else if (raw instanceof Number) {
      return ((Number) raw).doubleValue();
    } else if (raw instanceof Time) {
      return ((Time) raw).getTimeInMillis();
    } else {
      return 1.0;
    }
  }

  public String getString(String key) {
    Object raw = get(key);
    return (raw != null) ? String.valueOf(raw) : null;
  }

  public Time getTime() {
    try {
      return new Time(data.getTimestamp("time").getTime(), TimeZone.getDefault()); // TODO: fix the time zone
    } catch (SQLException e) {
      e.printStackTrace();
      return new Time();
    }
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