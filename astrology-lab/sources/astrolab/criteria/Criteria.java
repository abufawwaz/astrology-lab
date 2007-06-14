package astrolab.criteria;

import java.sql.ResultSet;
import java.util.ArrayList;

import astrolab.db.Database;
import astrolab.web.server.Request;

public class Criteria {

  private final static Criterion[] EMPTY_LIST = new Criterion[0];

  public static Criterion[] getCriteria() {
    int user = Request.getCurrentRequest().getUser();
    String query = "SELECT * FROM perspective_elect_criteria WHERE criteria_owner = " + user;
    ResultSet set = Database.executeQuery(query);
    try {
      ArrayList<Criterion> list = new ArrayList<Criterion>();
      while (set.next()) {
        list.add(Criterion.read(set));
      }

      return list.toArray(EMPTY_LIST);
    } catch (Exception e) {
      e.printStackTrace();
      return EMPTY_LIST;
    }
  }

}