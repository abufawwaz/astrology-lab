package astrolab.project;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;

import astrolab.astronom.SpacetimeEvent;
import astrolab.db.Database;
import astrolab.formula.FormulaeBase;
import astrolab.formula.FormulaePeriod;
import astrolab.formula.FormulaeSeries;
import astrolab.project.geography.Location;

public class SubjectDataProject extends Project {

  private HashSet<String> keySet = new HashSet<String>();
  private ProjectDataKey[] keys = new ProjectDataKey[0];

  public SubjectDataProject(int id, String name) {
    super(id, name);

    String[] keyStrings = Database.queryList("SHOW FIELDS FROM " + TABLE_PREFIX + getName());
    keys = new ProjectDataKey[keyStrings.length];
    for (int i = 0; i < keys.length; i++) {
      keys[i] = new ProjectDataKey(keyStrings[i], null);
      keySet.add(keys[i].getName());
    }
  }

  public ProjectDataKey[] getKeys() {
    return keys;
  }

  public SpacetimeEvent getMinTime() {
    return readTime("ASC");
  }

  public SpacetimeEvent getMaxTime() {
    return readTime("DESC");
  }

  // project time is always GMT
  private SpacetimeEvent readTime(String order) {
    ResultSet set = null;
    try {
      String sql = "SELECT event_time, location FROM " + SubjectDataProject.TABLE_PREFIX + getName() +
         (!"archive".equalsIgnoreCase(getName()) ? ", project_archive" : "") +
         " WHERE " + SubjectDataProject.TABLE_PREFIX + getName() +
         ".subject_id = project_archive.subject_id ORDER BY event_time " + order + " LIMIT 1"; 
      set = Database.executeQuery(sql);

      if ((set != null) && set.next()) {
        Date timestamp = set.getTimestamp(1);
        int location = set.getInt(2);
        SpacetimeEvent event = new SpacetimeEvent(timestamp.getTime(), Location.getLocation(location));
        return new SpacetimeEvent(event.getTimeInMillis(), SpacetimeEvent.GMT_TIME_ZONE);
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

    return new SpacetimeEvent(System.currentTimeMillis(), SpacetimeEvent.GMT_TIME_ZONE);
  }

  protected ProjectDataIterator getIterator(FormulaeSeries[] series, FormulaeBase base, FormulaePeriod period, SpacetimeEvent fromTime, SpacetimeEvent toTime) {
    // TODO: use fromTime and toTime within the SQL query
    // TODO: check keys

    HashSet<String> keySelectSet = new HashSet<String>();
    String grouping = base.getText();

    if ("time".equals(grouping)) {
      grouping = "event_time";
    }

    for (ProjectDataKey key: keys) {
      keySelectSet.add(normalizeKey(key.getName()));
    }

    for (FormulaeSeries serie: series) {
      keySelectSet.add(normalizeKey(serie.getText()));
    }
    keySelectSet.add(normalizeKey(grouping));

    String timing = "";
    if (fromTime != null || toTime != null) {
      if (fromTime != null) {
        timing += " event_time >= '" + fromTime.toMySQLString() + "'";
      }

      if (toTime != null) {
        if (fromTime != null) {
          timing += " AND";
        }
        timing += " event_time <= '" + toTime.toMySQLString() + "'";
      }
    }

    int slots = 360; // allow change
    String groupby;
    if (grouping != null && !"event_time".equalsIgnoreCase(grouping)) {
      groupby = " GROUP BY floor(" + Project.TABLE_PREFIX + getName() + "." + grouping + ")";
    } else {
      String fromSQLTime = fromTime.toMySQLString();
      String toSQLTime = toTime.toMySQLString();
      String datePointDiff = "datediff(event_time, '" + fromSQLTime + "')";
      String dateSpanDiff = "datediff('" + toSQLTime + "', '" + fromSQLTime + "')";
      groupby = " GROUP BY floor(" + datePointDiff + " / " + dateSpanDiff + " * " + slots + ")";
    }

    // "SELECT " + keyselect + " FROM " + Project.TABLE_PREFIX + getName() + timing + groupby + " LIMIT " + slots

    StringBuffer select = new StringBuffer("SELECT ");
    String[] keySelects = keySelectSet.toArray(new String[0]);

    select.append(keySelects[0]);
    for (int i = 1; i < keySelects.length; i++) {
      select.append(", ");
      select.append(keySelects[i]);
    }

    select.append(" FROM ");
    select.append(Project.TABLE_PREFIX);
    select.append(getName());
    if (!"archive".equalsIgnoreCase(getName())) {
      select.append(", project_archive ");
    }
    select.append(" WHERE ");
    select.append(Project.TABLE_PREFIX);
    select.append(getName());
    select.append(".subject_id = project_archive.subject_id AND ");
    select.append(timing);
    select.append(groupby);
    select.append(" LIMIT ");
    select.append(slots);

    return new ProjectDataIterator(this, Database.executeQuery(select.toString()));
  }

  protected void checkKey(String key) {
  }

}