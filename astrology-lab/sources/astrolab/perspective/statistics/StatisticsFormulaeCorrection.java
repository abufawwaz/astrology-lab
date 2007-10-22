package astrolab.perspective.statistics;

import astrolab.db.Database;
import astrolab.formula.Formulae;
import astrolab.project.Projects;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class StatisticsFormulaeCorrection extends Formulae {

  private String[] correction;
  private Formulae base;

  // correction[0] = level
  // correction[1] = offset
  // correction[2] = altitude
  // correction[3] = length
  // correction[4] = formulae
  public StatisticsFormulaeCorrection(String[] correction) {
    super("correction formulae");
    this.correction = correction;
    this.base = new Formulae(Integer.parseInt(correction[4]), 0, 0, null, 0);
  }

  public void fill(LocalizedStringBuffer buffer) {
    buffer.append(correction[0]);
    buffer.append(" + {");
    if (Integer.parseInt(correction[1]) != 1) {
      buffer.append("(");
    }

    base.fill(buffer);
    if (Integer.parseInt(correction[1]) != 1) {
      buffer.append(" - ");
      buffer.append(correction[1]);
      buffer.append(")");
    }

    if (Integer.parseInt(correction[3]) != 1) {
      buffer.append("*");
      buffer.append(correction[3]);
    }
    buffer.append("} *");
    buffer.append(correction[2]);
  }

  // <level> + SIN((<formulae> - <offset>) * <length>) * <altitude>
  public String getSQL() {
    return "(" + correction[0] + " + SIN(RADIANS((" + base.getSQL() + " - " + correction[1] + ") * " + correction[3] + ")) * " + correction[2] + ")";
  }

  public void store(int series) {
    int user = Request.getCurrentRequest().getUser();
    Database.execute("INSERT INTO formula (owner_id, formulae) VALUES (" + user + ", 'f" + Math.random() + "')");
    String newId = Database.query("SELECT MAX(formulae_id) FROM formula");
    String[][] elements = Database.queryList(2, "SELECT element_coefficient, element_id FROM formula_elements WHERE formulae_id = " + correction[4]);

    for (String[] element: elements) {
      Database.execute("INSERT INTO formula_elements VALUES (" + newId + ", " + element[0] + ", " + element[1] + ")");
    }

    Database.execute("INSERT INTO perspective_statistics_correction VALUES ("
        + user + ", "
        + Projects.getProject().getId() + ", "
        + series + ", "
        + correction[1] + ", "
        + correction[0] + ", "
        + correction[2] + ", "
        + correction[3] + ", "
        + newId + ")");
  }

  public static StatisticsFormulaeCorrection[] readCorrections(int series) {
    String[][] corrections = Database.queryList(5, "SELECT element_level, element_offset, element_altitude, element_length, formulae_id FROM perspective_statistics_correction WHERE subject_id = " + Request.getCurrentRequest().getUser() + " AND project_id = " + Projects.getProject().getId() + " AND series_id = " + series);
    StatisticsFormulaeCorrection[] result = new StatisticsFormulaeCorrection[corrections.length];

    for (int i = 0; i < corrections.length; i++) {
      result[i] = new StatisticsFormulaeCorrection(corrections[i]);
    }
    return result;
  }
}