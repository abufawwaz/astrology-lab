package astrolab.formula;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import astrolab.astronom.SpacetimeEvent;
import astrolab.db.Database;
import astrolab.db.Personalize;
import astrolab.db.RecordIterator;
import astrolab.formula.display.ModifyFormulaeSetChartColor;
import astrolab.project.Project;
import astrolab.project.ProjectDataKey;
import astrolab.project.Projects;

public class FormulaIterator extends RecordIterator {

  protected FormulaIterator(ResultSet set) {
    super(set);
  }

  public static FormulaeBase getChartBase() {
    int userId = Personalize.getUser();
    int projectId = Projects.getProject().getId();
    String query = "SELECT formula.formulae_id, project_id, owner_id, formulae FROM formula, formula_chart_base" +
        " WHERE project_id = " + projectId +
        " AND formula.formulae_id = formula_chart_base.base_id" +
        " AND user_id = " + userId +
        " AND selected " +
        " LIMIT 1";
    String[][] result = Database.queryList(4, query);
    if (result == null || result.length == 0) {
      return new FormulaeBase(0, projectId, userId, "time");
    } else {
      return new FormulaeBase(Integer.parseInt(result[0][0]), Integer.parseInt(result[0][1]), Integer.parseInt(result[0][2]), result[0][3]);
    }
  }

  public static FormulaePeriod getChartPeriod() {
    int userId = Personalize.getUser();
    int projectId = Projects.getProject().getId();
    String query = "SELECT formula.formulae_id, project_id, owner_id, formulae FROM formula, formula_chart_base" +
        " WHERE project_id = " + projectId +
        " AND formula.formulae_id = formula_chart_base.period_id" +
        " AND user_id = " + userId +
        " AND selected " +
        " LIMIT 1";
    String[][] result = Database.queryList(4, query);
    if (result == null || result.length == 0) {
      FormulaeBase base = getChartBase();
      return new FormulaePeriod(base.getId(), base.getProject(), base.getOwner(), base.getText());
    } else {
      return new FormulaePeriod(Integer.parseInt(result[0][0]), Integer.parseInt(result[0][1]), Integer.parseInt(result[0][2]), result[0][3]);
    }
  }

  public static FormulaeSeries[] getChartSeries(boolean withColor) {
    int userId = Personalize.getUser();
    int projectId = Projects.getProject().getId();
    String query = "SELECT formula.formulae_id, project_id, owner_id, formulae, score, chart_color FROM formula, formula_chart" +
        " WHERE project_id = " + projectId +
        " AND formula.formulae_id = formula_chart.formulae_id" +
        " AND user_id = " + userId +
        ((withColor) ? " AND chart_color IS NOT NULL " : "") +
        " ORDER BY chart_color DESC, formula.formulae_id";

    FormulaIterator iterator = new FormulaIterator(Database.executeQuery(query));
    ArrayList<Formulae> list = new ArrayList<Formulae>();
    while (iterator.hasNext()) {
      list.add((Formulae) iterator.next());
    }
    iterator.close();

    // if there is no formula added use the keys
    if (list.size() == 0) {
      Object[] colors = ModifyFormulaeSetChartColor.getColors().toArray();
      ProjectDataKey[] keys = Projects.getProject().getKeys();
      String base = getChartBase().getText();
      for (int i = 0; i < keys.length; i++) {
        if (!base.equals(keys[i].getName())) {
          list.add(new FormulaeSeries(i, projectId, 0, keys[i].getName(), 0.0, (String) ((i < colors.length) ? colors[i] : "black")));
        }
      }
    }
    return list.toArray(new FormulaeSeries[0]);
  }

  public static SpacetimeEvent getChartFromTime() {
    Project project = Projects.getProject();
    SpacetimeEvent result = getChartTime(project, "from_time");
    return (result != null) ? result : project.getMinTime();
  }

  public static SpacetimeEvent getChartToTime() {
    Project project = Projects.getProject();
    SpacetimeEvent result = getChartTime(project, "to_time");
    return (result != null) ? result : project.getMaxTime();
  }

  private final static SpacetimeEvent getChartTime(Project project, String key) {
    int userId = Personalize.getUser();
    String query = "SELECT " + key + " FROM formula_chart_base" +
        " WHERE project_id = " + project.getId() +
        " AND user_id = " + userId +
        " AND selected " +
        " LIMIT 1";
    ResultSet set = Database.executeQuery(query);
    try {
      if (set.next()) {
        Date timestamp = set.getTimestamp(1);
        if (timestamp != null) {
          return new SpacetimeEvent(timestamp.getTime());
        }
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
    return null;
  }

  protected Formulae read() throws Exception {
    return new FormulaeSeries(set.getInt(1), set.getInt(2), set.getInt(3), set.getString(4), set.getDouble(5), set.getString(6));
  }

}
