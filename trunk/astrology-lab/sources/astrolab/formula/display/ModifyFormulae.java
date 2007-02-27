package astrolab.formula.display;

import astrolab.db.Database;
import astrolab.db.Personalize;
import astrolab.project.Projects;
import astrolab.web.Modify;
import astrolab.web.component.ComponentSelectText;
import astrolab.web.server.Request;

public class ModifyFormulae extends Modify {

  public void operate(Request request) {
    try {
      String text = ComponentSelectText.retrieve(request, "formulae");
      if (text != null) {
        addSeries(storeFormulae(text));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
	}

  public static int storeFormulae(String text) {
    int owner = Personalize.getUser(true);

    //TODO: limit formula to 100 per user
    //TODO: parse formula for attacks or syntax errors
    String textid = Database.query("SELECT formulae_id FROM formula WHERE formulae = '" + text + "'");
    if (textid == null) {
      Database.execute("INSERT INTO formula (owner_id, formulae) VALUES (" + owner + ", '" + text + "')");
      textid = Database.query("SELECT formulae_id FROM formula WHERE formulae = '" + text + "'");
    }
    return Integer.parseInt(textid);
  }

  private static void addSeries(int series_id) {
    int user = Personalize.getUser(true);
    int project = Projects.getProject().getId();

    Database.execute("INSERT INTO formula_chart VALUES (" + user + ", " + project + ", " + series_id + ", NULL, NULL)");
  }

}