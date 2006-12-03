package astrolab.formula;

import java.sql.ResultSet;

import astrolab.db.Database;
import astrolab.db.Personalize;
import astrolab.db.RecordIterator;
import astrolab.db.Text;

public class FormulaIterator extends RecordIterator {

  protected FormulaIterator(ResultSet set) {
    super(set);
  }

  public static FormulaIterator iterateBest() {
    int project_id = Personalize.getFavourite(-1, Text.getId("user.session.project"), -1);
    String query = "SELECT formulae_id, project_id, owner_id, score FROM formula_description" +
        " WHERE project_id = " + project_id +
            " ORDER BY score DESC" +
            " LIMIT 30";
    return new FormulaIterator(Database.executeQuery(query));
  }

  public static FormulaIterator iterateOwn() {
    int project_id = Personalize.getFavourite(-1, Text.getId("user.session.project"), -1);
    String query = "SELECT formulae_id, project_id, owner_id, score FROM formula_description" +
        " WHERE project_id = " + project_id +
            " AND owner_id = " + Personalize.getUser() +
            " LIMIT 10";
    return new FormulaIterator(Database.executeQuery(query));
  }

  protected Formulae read() throws Exception {
    return new Formulae(set.getInt(1), set.getInt(2), set.getInt(3), set.getDouble(4));
  }

}
