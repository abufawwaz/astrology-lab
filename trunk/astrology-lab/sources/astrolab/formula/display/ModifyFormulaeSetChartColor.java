package astrolab.formula.display;

import java.util.HashSet;

import astrolab.db.Database;
import astrolab.db.Personalize;
import astrolab.formula.FormulaIterator;
import astrolab.formula.FormulaeSeries;
import astrolab.project.Projects;
import astrolab.web.Modify;
import astrolab.web.server.Request;

public class ModifyFormulaeSetChartColor extends Modify {

  public final static String KEY = "formulae"; 
  public final static String IS_TO_SET = "set_as"; 

  private final static HashSet<String> COLOR_SET = new HashSet<String>();

  static {
    COLOR_SET.add("red");
    COLOR_SET.add("orange");
    COLOR_SET.add("yellow");
    COLOR_SET.add("green");
    COLOR_SET.add("blue");
    COLOR_SET.add("indigo");
    COLOR_SET.add("black");
  }

  public static HashSet<String> getColors() {
    return (HashSet<String>) COLOR_SET.clone();
  }

  public void operate(Request request) {
    try {
      String formulaeText = request.get(KEY);

      if (formulaeText != null) {
        int formulae_id = Integer.parseInt(formulaeText);
        int user = Personalize.getUser();
        int project = Projects.getProject().getId();
        String color = "NULL";

        if (Boolean.parseBoolean(request.get(IS_TO_SET))) {
          FormulaeSeries cf = null;
          FormulaeSeries[] series = FormulaIterator.getChartSeries(true);
          HashSet<String> colorSet = getColors();

          for (int i = 0; i < series.length; i++) {
            colorSet.remove(series[i].getColor());
            cf = series[i];
          }

          if (colorSet.isEmpty()) {
            Database.execute("UPDATE formula_chart SET chart_color = NULL WHERE user_id = " + user + " AND project_id = " + project + " AND formulae_id = " + cf.getId());
            color = cf.getColor();
          } else {
            color = (String) colorSet.toArray()[0];
          }
          color = "'" + color + "'";
        }

        Database.execute("UPDATE formula_chart SET chart_color = " + color + " WHERE user_id = " + user + " AND project_id = " + project + " AND formulae_id = " + formulae_id);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
	}

}