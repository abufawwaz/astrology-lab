package astrolab.perspective.statistics;

import astrolab.db.Database;
import astrolab.formula.Formulae;
import astrolab.project.Projects;
import astrolab.web.server.Request;

public class StatisticsFormulae {

  public static Formulae getFormulae() {
    int subject = Request.getCurrentRequest().getUser();
    int project = Projects.getProject().getId();
    int formulae;
    String formulaeName = String.valueOf(subject) + "_" + project;
    String formulaeText = Database.query("SELECT formulae_id FROM perspective_statistics WHERE subject_id = '" + subject + "' AND project_id = '" + project + "'");

    if (formulaeText == null) {
      String textId = Database.query("SELECT formulae_id FROM formula WHERE formulae = '" + formulaeName + "'");

      if (textId == null) {
        Database.execute("INSERT INTO formula (owner_id, formulae) VALUES (" + subject + ", '" + formulaeName + "')");
        textId = Database.query("SELECT formulae_id FROM formula WHERE formulae = '" + formulaeName + "'");
      }

      formulae = Integer.parseInt(textId);

      Database.execute("INSERT INTO perspective_statistics VALUES (" + subject + ", " + project + ", " + formulae + ")");
    } else {
      formulae = Integer.parseInt(formulaeText);
    }

    return new Formulae(formulae, project, subject, formulaeName, 0);
  }

  public static void changeFactor(int factor, int step) {
    double coefficient = 0.0;
    Formulae formulae = getFormulae();
    String coefficientText = Database.query("SELECT element_coefficient FROM formula_elements WHERE formulae_id = " + formulae.getId() + " AND element_id = " + factor);

    if (coefficientText != null) {
      coefficient = Double.parseDouble(coefficientText) + step;
      Database.execute("UPDATE formula_elements SET element_coefficient = " + coefficient + " WHERE formulae_id = " + formulae.getId() + " AND element_id = " + factor);
    } else {
      Database.execute("INSERT INTO formula_elements VALUES (" + formulae.getId() + ", " + step + ", " + factor + ")");
    }
  }

}