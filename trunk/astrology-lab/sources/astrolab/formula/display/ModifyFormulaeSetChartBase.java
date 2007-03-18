package astrolab.formula.display;

import astrolab.db.Database;
import astrolab.db.Personalize;
import astrolab.project.Projects;
import astrolab.web.Modify;
import astrolab.web.server.Request;

public class ModifyFormulaeSetChartBase extends Modify {

  public final static String KEY = "formulae"; 
  public final static String IS_TO_SET = "set_as"; 

  public void operate(Request request) {
    try {
      String formulaeText = request.get(KEY);

      if (formulaeText != null) {
        int formulae_id = Integer.parseInt(formulaeText);
        int user = Personalize.getUser();
        int project = Projects.getProject().getId();
        if (Boolean.parseBoolean(request.get(IS_TO_SET))) {
          Database.execute("UPDATE formula_chart SET chart_base = formulae_id = " + formulae_id + " WHERE user_id = " + user + " AND project_id = " + project);
        } else {
          Database.execute("UPDATE formula_chart SET chart_base = FALSE WHERE user_id = " + user + " AND project_id = " + project + " AND formulae_id = " + formulae_id);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
	}

}