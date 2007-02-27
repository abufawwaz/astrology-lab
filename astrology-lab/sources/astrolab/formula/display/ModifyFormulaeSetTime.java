package astrolab.formula.display;

import astrolab.astronom.Time;
import astrolab.db.Database;
import astrolab.db.Personalize;
import astrolab.project.Projects;
import astrolab.web.Modify;
import astrolab.web.component.time.ComponentSelectTime;
import astrolab.web.server.Request;

public class ModifyFormulaeSetTime extends Modify {

  public final static String KEY_FROM_TIME = "_chart_from_time"; 
  public final static String KEY_TO_TIME = "_chart_to_time";

  public void operate(Request request) {
    try {
      int user = Personalize.getUser(true);
      int project = Projects.getProject().getId();
      Time fromTime = ComponentSelectTime.retrieve(request, KEY_FROM_TIME);
      Time toTime = ComponentSelectTime.retrieve(request, KEY_TO_TIME);

      if (fromTime != null) {
        Database.execute("UPDATE formula_chart_base SET from_time = '" + fromTime.toMySQLString() + "' WHERE user_id = " + user + " AND project_id = " + project + " AND selected");
      }
      if (toTime != null) {
        Database.execute("UPDATE formula_chart_base SET to_time = '" + toTime.toMySQLString() + "' WHERE user_id = " + user + " AND project_id = " + project + " AND selected");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
	}

}