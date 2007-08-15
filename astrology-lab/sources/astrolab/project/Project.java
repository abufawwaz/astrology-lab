package astrolab.project;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.SpacetimeEvent;
import astrolab.db.Database;
import astrolab.formula.FormulaeBase;
import astrolab.formula.FormulaePeriod;
import astrolab.formula.FormulaeSeries;

public class Project {

  final static String TABLE_PREFIX = "project_";

  private int id;
  private String name;
  private ProjectDataKey[] keys;
  private SpacetimeEvent minTime = null;
  private SpacetimeEvent maxTime = null;

  public Project(int id, String name) {
    this.id = id;
    this.name = name;

    if (name != null) {
      listKeys();
    }
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public ProjectDataKey[] getKeys() {
    return keys;
  }

  public SpacetimeEvent getMinTime() {
    if (minTime == null) {
      ResultSet set = null;
      try {
        set = Database.executeQuery("SELECT time FROM " + Project.TABLE_PREFIX + getName() + " ORDER BY time ASC LIMIT 1");

        if ((set != null) && set.next()) {
          Date timestamp = set.getTimestamp(1);
          minTime = new SpacetimeEvent(timestamp.getTime());
        } else {
          minTime = new SpacetimeEvent(System.currentTimeMillis());
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

  public SpacetimeEvent getMaxTime() {
    if (maxTime == null) {
      ResultSet set = null;
      try {
        set = Database.executeQuery("SELECT time FROM " + Project.TABLE_PREFIX + getName() + " ORDER BY time DESC LIMIT 1");

        if ((set != null) && set.next()) {
          Date timestamp = set.getTimestamp(1);
          maxTime = new SpacetimeEvent(timestamp.getTime());
        } else {
          maxTime = new SpacetimeEvent(System.currentTimeMillis());
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

  public ProjectData getData() {
    return new ProjectData(this);
  }

  public ProjectData getData(FormulaeSeries[] series, FormulaeBase base, FormulaePeriod period, SpacetimeEvent fromTime, SpacetimeEvent toTime) {
    checkKey(base.getText());
    for (int f = 0; f < series.length; f++) {
      checkKey(series[f].getText());
    }
    String[] keys = new String[series.length];
    for (int i = 0; i < keys.length; i++) {
      keys[i] = series[i].getText();
    }
    return new ProjectData(this, keys, base.getText(), fromTime, toTime);
  }

  private void listKeys() {
    String[] keyStrings = Database.queryList("SHOW FIELDS FROM " + TABLE_PREFIX + name);
    keys = new ProjectDataKey[keyStrings.length];
    for (int i = 0; i < keys.length; i++) {
      keys[i] = new ProjectDataKey(keyStrings[i], null);
    }
  }

  private void checkKey(String key) {
    if (key.indexOf(' ') >= 0 || key.indexOf('(') >= 0) {
      // TODO: find atoms more precisely!
      return;
    }

    boolean found = false;
    for (int k = 0; k < keys.length; k++) {
      if (key.equalsIgnoreCase(keys[k].getName())) {
        found = true;
        break;
      }
    }

    if (!found) {
      try {
        ActivePoint.getActivePoint(key, new SpacetimeEvent(System.currentTimeMillis()));
        ProjectDataFiller.startFiller(key, this);
  
        addKey(key);
      } catch (IllegalStateException ise) {
        // ignore the key;
      }
    }
  }

  private void addKey(String key) {
    Database.execute("ALTER TABLE " + TABLE_PREFIX + name + " ADD COLUMN " + key + " DOUBLE");
    Database.execute("ALTER TABLE " + TABLE_PREFIX + name + " ADD INDEX USING BTREE (" + key + ")");
    listKeys();
  }

}