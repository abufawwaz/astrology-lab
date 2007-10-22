package astrolab.perspective.statistics;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import astrolab.astronom.SpacetimeEvent;
import astrolab.criteria.Criterion;
import astrolab.db.Database;
import astrolab.db.Text;
import astrolab.formula.Formulae;
import astrolab.formula.FormulaeBase;
import astrolab.formula.FormulaeSeries;
import astrolab.project.Project;
import astrolab.project.ProjectDataKey;
import astrolab.web.server.Request;

public class IteratorProjectTimeData extends IteratorProjectData {

  private static HashSet<String> UNNACCEPTABLE_KEYS = new HashSet<String>();
  static {
    UNNACCEPTABLE_KEYS.add("subject_id");
    UNNACCEPTABLE_KEYS.add("location");
    UNNACCEPTABLE_KEYS.add("time");
    UNNACCEPTABLE_KEYS.add("event_time");

    for (Integer planet: Criterion.TYPES.get(Criterion.TYPE_PLANET)) {
      UNNACCEPTABLE_KEYS.add(Text.getDescriptiveId(planet));
    }
  }

  protected IteratorProjectTimeData(Project project) {
    super(project);

    ensureBase();
    ensureSeries();
    readData();
  }

  public SpacetimeEvent getTime() {
    try {
      Object object = data.getObject(1);

      if (object instanceof java.sql.Timestamp) {
        return new SpacetimeEvent(((java.sql.Timestamp) object).getTime(), SpacetimeEvent.GMT_TIME_ZONE); // TODO: check that project records are stored with GMT
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return project.getMinTime();
  }

  public double[] getValue(Formulae series) {
    String key = series.getText();
    if (internalExistsValue("min_" + key)) {
      return new double[] {
          internalGetValue("min_" + key),
          internalGetValue("avg_" + key),
          internalGetValue("max_" + key)
      };
    } else {
      return null;
    }
  }

  private double internalGetValue(String key) {
    try {
      Object raw = data.getObject(key);
      if (raw instanceof Number) {
        return ((Number) raw).doubleValue();
      }
    } catch (SQLException e) {
    }

    return 0;
  }

  private boolean internalExistsValue(String key) {
    try {
      return (data.getObject(key) instanceof Number);
    } catch (SQLException e) {
    }
    return false;
  }

  private void ensureBase() {
    super.base = new FormulaeBase(0, project.getId(), Request.getCurrentRequest().getUser(), "time");
  }

  private void ensureSeries() {
    if ((series == null) || (series.length == 0)) {
      int user = Request.getCurrentRequest().getUser();
      ArrayList<Formulae> list = new ArrayList<Formulae>();

      for (ProjectDataKey key: project.getKeys()) {
        if (!UNNACCEPTABLE_KEYS.contains(key.getName())) {
          list.add(new FormulaeSeries(0, project.getId(), user, key.getName(), 0, "black"));
        }
      }
      list.add(StatisticsFormulae.getFormulae());
      series = list.toArray(new Formulae[0]);
    }
  }

  private void readData() {
    StringBuffer buffer = new StringBuffer("SELECT ");

    buffer.append(normalizeKey("time"));
    for (Formulae serie: series) {
      String seriesName = serie.getText();
      String seriesFormulae = normalizeKey(serie.getSQL());
      buffer.append(", avg(");
      buffer.append(seriesFormulae);
      buffer.append(") as avg_");
      buffer.append(seriesName);
      buffer.append(", min(");
      buffer.append(seriesFormulae);
      buffer.append(") as min_");
      buffer.append(seriesName);
      buffer.append(", max(");
      buffer.append(seriesFormulae);
      buffer.append(") as max_");
      buffer.append(seriesName);
    }

    fillTableSelect(buffer, 1);
//  fillTimeSelect(buffer);
    buffer.append(" TRUE"); // TODO: needed only while time selection is off

System.err.println(" FOREX PROJECT: " + project.getClass());
    String fromSQLTime = project.getMinTime().toMySQLString();
    String toSQLTime = project.getMaxTime().toMySQLString();
    String datePointDiff = "datediff(" + normalizeKey("time") + ", '" + fromSQLTime + "')";
    String dateSpanDiff = "datediff('" + toSQLTime + "', '" + fromSQLTime + "')";

    buffer.append(" GROUP BY floor(");
    buffer.append(datePointDiff);
    buffer.append(" / ");
    buffer.append(dateSpanDiff);
    buffer.append(" * 300)");

System.err.println(" READ: " + buffer.toString());
    this.data = Database.executeQuery(buffer.toString());
  }

}