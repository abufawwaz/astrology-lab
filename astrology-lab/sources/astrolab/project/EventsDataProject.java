package astrolab.project;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import astrolab.astronom.SpacetimeEvent;
import astrolab.db.Database;
import astrolab.formula.FormulaeBase;
import astrolab.formula.FormulaePeriod;
import astrolab.formula.FormulaeSeries;

public class EventsDataProject extends Project {

  private ProjectDataKey[] keys = new ProjectDataKey[0];
  private SpacetimeEvent minTime = null;
  private SpacetimeEvent maxTime = null;

  public EventsDataProject(int id, String name) {
    super(id, name);
    refresh();
  }

  public ProjectDataKey[] getKeys() {
    return keys;
  }

  // project time is always GMT
  public SpacetimeEvent getMinTime() {
    if (minTime == null) {
      ResultSet set = null;
      try {
        set = Database.executeQuery("SELECT time FROM " + EventsDataProject.TABLE_PREFIX + getName() + " ORDER BY time ASC LIMIT 1");

        if ((set != null) && set.next()) {
          Date timestamp = set.getTimestamp(1);
          minTime = new SpacetimeEvent(timestamp.getTime(), SpacetimeEvent.GMT_TIME_ZONE);
        } else {
          minTime = new SpacetimeEvent(System.currentTimeMillis(), SpacetimeEvent.GMT_TIME_ZONE);
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        if (set != null) {
          try {
            set.close();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return minTime;
  }

  // project time is always GMT
  public SpacetimeEvent getMaxTime() {
    if (maxTime == null) {
      ResultSet set = null;
      try {
        set = Database.executeQuery("SELECT time FROM " + EventsDataProject.TABLE_PREFIX + getName() + " ORDER BY time DESC LIMIT 1");

        if ((set != null) && set.next()) {
          Date timestamp = set.getTimestamp(1);
          maxTime = new SpacetimeEvent(timestamp.getTime(), SpacetimeEvent.GMT_TIME_ZONE);
        } else {
          maxTime = new SpacetimeEvent(System.currentTimeMillis(), SpacetimeEvent.GMT_TIME_ZONE);
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        if (set != null) {
          try {
            set.close();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return maxTime;
  }

  protected ProjectDataIterator getIterator(FormulaeSeries[] series, FormulaeBase base, FormulaePeriod period, SpacetimeEvent fromTime, SpacetimeEvent toTime) {
    // TODO: use fromTime and toTime within the SQL query
    // TODO: check keys

    StringBuffer keyselect = new StringBuffer();
    String grouping = base.getText();

    for (int i = 0; i < keys.length; i++) {
      if (grouping != null) {
        keyselect.append(keys[i].getName());
      } else {
        keyselect.append(keys[i].getName());
      }
      keyselect.append(", ");
    }
    for (int i = 0; i < series.length; i++) {
      keyselect.append(series[i].getText());
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
      String datePointDiff = "datediff(time, '" + fromSQLTime + "')";
      String dateSpanDiff = "datediff('" + toSQLTime + "', '" + fromSQLTime + "')";
      groupby = " GROUP BY floor(" + datePointDiff + " / " + dateSpanDiff + " * " + slots + ")";
    }

    return new ProjectDataIterator(this, Database.executeQuery("SELECT " + keyselect + " FROM " + Project.TABLE_PREFIX + getName() + timing + groupby + " LIMIT " + slots));
  }

  public void refresh() {
    String[] keyStrings = Database.queryList("SHOW FIELDS FROM " + TABLE_PREFIX + getName());
    keys = new ProjectDataKey[keyStrings.length];
    for (int i = 0; i < keys.length; i++) {
      keys[i] = new ProjectDataKey(keyStrings[i], null);
    }
  }

}