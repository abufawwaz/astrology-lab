package astrolab.project;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.TimeZone;

import astrolab.astronom.Time;
import astrolab.db.Database;
import astrolab.formula.FormulaeBase;
import astrolab.formula.FormulaePeriod;
import astrolab.formula.FormulaeSeries;

public class Project {

  final static String TABLE_PREFIX = "project_";

  private int id;
  private String name;
  private ProjectDataKey[] keys;
  private Time minTime = null;
  private Time maxTime = null;

  public Project(int id, String name) {
    this.id = id;
    this.name = name;
    listKeys();
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

  public Time getMinTime() {
    if (minTime == null) {
      ResultSet set = Database.executeQuery("SELECT time FROM " + Project.TABLE_PREFIX + getName() + " ORDER BY time ASC LIMIT 1");
      try {
        if (set.next()) {
          Date timestamp = set.getTimestamp(1);
          minTime = new Time(timestamp.getTime(), TimeZone.getDefault()); // TODO: fix the time zone
        } else {
          minTime = new Time();
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        try {
          set.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    return minTime;
  }

  public Time getMaxTime() {
    if (maxTime == null) {
      ResultSet set = Database.executeQuery("SELECT time FROM " + Project.TABLE_PREFIX + getName() + " ORDER BY time DESC LIMIT 1");
      try {
        if (set.next()) {
          Date timestamp = set.getTimestamp(1);
          maxTime = new Time(timestamp.getTime(), TimeZone.getDefault()); // TODO: fix the time zone
        } else {
          maxTime = new Time();
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        try {
          set.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    return maxTime;
  }

  public ProjectData getData() {
    return new ProjectData(this);
  }

  public ProjectData getData(FormulaeSeries[] series, FormulaeBase base, FormulaePeriod period, Time fromTime, Time toTime) {
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
      addKey(key);
    }

    ProjectDataFiller.startFiller(key, this);
  }

  private void addKey(String key) {
    Database.execute("ALTER TABLE " + TABLE_PREFIX + name + " ADD COLUMN " + key + " DOUBLE, INDEX USING BTREE (" + key + ")");
    listKeys();
  }

}