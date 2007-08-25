package astrolab.project;

import astrolab.astronom.SpacetimeEvent;
import astrolab.formula.FormulaeBase;
import astrolab.formula.FormulaePeriod;
import astrolab.formula.FormulaeSeries;

public abstract class Project {

  protected final static String TABLE_PREFIX = "project_";

  private int id;
  private String name;

  public Project(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public abstract ProjectDataKey[] getKeys();

  // project time is always GMT
  public abstract SpacetimeEvent getMinTime();

  // project time is always GMT
  public abstract SpacetimeEvent getMaxTime();

  protected abstract ProjectDataIterator getIterator(FormulaeSeries[] series, FormulaeBase base, FormulaePeriod period, SpacetimeEvent fromTime, SpacetimeEvent toTime);

  protected String normalizeKey(String key) {
    final String projectTablePrefix = Project.TABLE_PREFIX + getName() + ".";
    final String archiveTablePrefix = Project.TABLE_PREFIX + "archive.";

    for (ProjectDataKey dataKey: getKeys()) {
      if (dataKey.getName().equals(key)) {
        return projectTablePrefix + key;
      }
    }

    int braceStart = key.indexOf('(');
    int braceEnd = key.lastIndexOf(')');

    if ((braceStart >= 0) && (braceEnd > 0)) {
      return key.substring(0, braceStart + 1) + normalizeKey(key.substring(braceStart + 1, braceEnd)) + key.substring(braceEnd);
    }

    return archiveTablePrefix + key;
  }
}