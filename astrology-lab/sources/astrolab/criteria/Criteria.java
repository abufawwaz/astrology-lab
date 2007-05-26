package astrolab.criteria;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;

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

      ensureTime(list);
      ensureLocation(list);

      return list.toArray(EMPTY_LIST);
    } catch (Exception e) {
      e.printStackTrace();
      return EMPTY_LIST;
    }
  }

  private final static void ensureLocation(ArrayList<Criterion> list) {
    
  }

  private final static void ensureTime(ArrayList<Criterion> list) {
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i) instanceof CriterionStartTime) {
        list.add(0, list.remove(i));
        return;
      }
    }

    list.add(0, new CriterionStartTime(Calendar.getInstance()));
  }

}