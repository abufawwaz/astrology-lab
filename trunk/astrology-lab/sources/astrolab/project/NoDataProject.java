package astrolab.project;

import astrolab.astronom.SpacetimeEvent;
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

  public ProjectData getData() {
    return new NoDataProjectData(this);
  }

  public ProjectData getData(FormulaeSeries[] series, FormulaeBase base, FormulaePeriod period, SpacetimeEvent fromTime, SpacetimeEvent toTime) {
    return new NoDataProjectData(this);
  }

}
