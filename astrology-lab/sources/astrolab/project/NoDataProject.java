package astrolab.project;

import java.sql.ResultSet;

import astrolab.astronom.SpacetimeEvent;
import astrolab.db.Database;
import astrolab.formula.FormulaeBase;
import astrolab.formula.FormulaePeriod;
import astrolab.formula.FormulaeSeries;

public class NoDataProject extends Project {

  public NoDataProject(int id) {
    super(id, null);
  }

  public ProjectDataKey[] getKeys() {
    return new ProjectDataKey[0];
  }

  public SpacetimeEvent getMinTime() {
    return new SpacetimeEvent(System.currentTimeMillis());
  }

  public SpacetimeEvent getMaxTime() {
    return new SpacetimeEvent(System.currentTimeMillis());
  }

  protected ProjectDataIterator getIterator(FormulaeSeries[] series, FormulaeBase base, FormulaePeriod period, SpacetimeEvent fromTime, SpacetimeEvent toTime) {
    ResultSet emptySet = Database.executeQuery("SELECT * FROM project WHERE FALSE");
    return new ProjectDataIterator(this, emptySet);
  }

}
