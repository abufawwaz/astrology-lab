package astrolab.perspective.statistics;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.StringTokenizer;

import astrolab.astronom.SpacetimeEvent;
import astrolab.formula.FormulaIterator;
import astrolab.formula.Formulae;
import astrolab.project.Project;
import astrolab.project.ProjectDataKey;

public abstract class IteratorProjectData {

  protected Project project;
  protected ResultSet data;

  protected boolean isSubjectBased = false;

  protected Formulae base;
  protected Formulae[] series;

  protected IteratorProjectData(Project project) {
    this.project = project;
    this.base = FormulaIterator.getChartBase();
    this.series = FormulaIterator.getChartSeries(true);

    if ("archive".equals(project.getName())) {
      isSubjectBased = false;
    } else {
      for (ProjectDataKey key: project.getKeys()) {
        if (key.getName().equals("subject_id")) {
          isSubjectBased = true;
        }
      }
    }
  }

  public Formulae getBase() {
    return base;
  }

  public Formulae[] getSeries() {
    return series;
  }

  protected Object get(String key) {
    Object object = null;

    try {
      object = data.getObject(normalizeKey(key));
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

  protected double getNumeric(String key) {
    Object raw = get(key);

    if (raw == null) {
      return 0;
    } else if (raw instanceof Number) {
      return ((Number) raw).doubleValue();
    } else if (raw instanceof SpacetimeEvent) {
      return ((SpacetimeEvent) raw).getTimeInMillis();
    } else {
      return 0;
    }
  }

  protected String getString(String key) {
    Object raw = get(key);
    return (raw != null) ? String.valueOf(raw) : null;
  }

  protected SpacetimeEvent getTime() {
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

  protected final void fillTableSelect(StringBuffer buffer, int count) {
    buffer.append(" FROM project_");
    buffer.append(project.getName());
    buffer.append(" as p1");
    for (int i = 1; i < count; i++) {
      buffer.append(", project_");
      buffer.append(project.getName());
      buffer.append(" as p");
      buffer.append(String.valueOf(i + 1));
    }

    if (isSubjectBased) {
      buffer.append(", project_archive as p0 WHERE p0.event_id = p1.subject_id AND");
    } else {
      buffer.append(" WHERE");
    }
  }

  protected final void fillTimeSelect(StringBuffer buffer) {
    SpacetimeEvent fromTime = FormulaIterator.getChartFromTime();
    SpacetimeEvent toTime = FormulaIterator.getChartToTime();

    if (fromTime != null) {
      buffer.append(" ");
      buffer.append(normalizeKey("time"));
      buffer.append(" >= '");
      buffer.append(fromTime.toMySQLString());
      buffer.append("'");
    }

    if ((fromTime != null) && (toTime != null)) {
      buffer.append(" AND");
    }

    if (toTime != null) {
      buffer.append(" ");
      buffer.append(normalizeKey("time"));
      buffer.append(" <= '");
      buffer.append(toTime.toMySQLString());
      buffer.append("'");
    }
  }

  static HashSet<String> sqlReserved = new HashSet<String>();
  static {
    sqlReserved.add(" ");
    sqlReserved.add(".");
    sqlReserved.add("+");
    sqlReserved.add("-");
    sqlReserved.add("*");
    sqlReserved.add("/");
    sqlReserved.add("(");
    sqlReserved.add(")");
    sqlReserved.add("abs");
    sqlReserved.add("floor");
    sqlReserved.add("min");
    sqlReserved.add("max");
    sqlReserved.add("radians");
    sqlReserved.add("sin");
  }

  protected final String normalizeKey(String key) {
    StringTokenizer tokens = new StringTokenizer(key, " .+-*/()", true);
    StringBuffer result = new StringBuffer();

    while (tokens.hasMoreTokens()) {
      boolean isNumber = true;
      String token = tokens.nextToken();
      for (char ch: token.toCharArray()) {
        if (!Character.isDigit(ch)) {
          isNumber = false;
          break;
        }
      }

      if (isNumber || sqlReserved.contains(token.toLowerCase())) {
        result.append(token);
      } else {
        result.append(normalizeAtom(token));
      }
    }

    return result.toString();
  }

  protected final String normalizeAtom(String key) {
    if ((key.indexOf('*') >= 0) || (key.indexOf('+') >= 0) || (key.indexOf('-') >= 0) || (key.indexOf('/') >= 0)) {
      return key;
    }

    final String projectTablePrefix = "p1.";
    final String archiveTablePrefix = "p0.";

    for (ProjectDataKey dataKey: project.getKeys()) {
      if (dataKey.getName().equals(key)) {
        return projectTablePrefix + key;
      }
    }

    int braceStart = key.indexOf('(');
    int braceEnd = key.lastIndexOf(')');

    if ((braceStart >= 0) && (braceEnd > 0)) {
      return key.substring(0, braceStart + 1) + normalizeKey(key.substring(braceStart + 1, braceEnd)) + key.substring(braceEnd);
    }

    if ("time".equalsIgnoreCase(key)) {
      key = "event_time";
    }

    return archiveTablePrefix + key;
  }

}