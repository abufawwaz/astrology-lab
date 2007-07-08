package astrolab.criteria;

import java.sql.ResultSet;
import java.util.ArrayList;

import astrolab.db.Database;
import astrolab.db.Personalize;
import astrolab.perspective.election.FormElectionaryCriteriaTemplate;
import astrolab.web.server.Request;

public class Criteria {

  private final static Criterion[] EMPTY_LIST = new Criterion[0];

//    new CriterionZodiacSign(),
//    new Criterion(CriterionAlgorithm.ALGORITHM_DIRECTION),
//    new Criterion(CriterionAlgorithm.ALGORITHM_PHASE),
//    new CriterionPositionPlanetInHouse(),
//    new Criterion(CriterionAlgorithm.ALGORITHM_ASPECT),
//    new CriterionTimeOfWeek(),

  public final static Criterion[] getCriteria(int type, int actor, int action, int factor, int modifiers) {
    if (type != 0) {
      return new Criterion[] { new ReplacementCriterion(new Criterion(type), actor, action, factor, modifiers) };
    } else {
      ArrayList<Criterion> list = new ArrayList<Criterion>();
  
      // TODO: optimize: remove Criterion constructor
      for (CriterionAlgorithm algorithmType: CriterionAlgorithm.ALGORITHMS.values()) {
        for (Criterion c: new Criterion(algorithmType.getType()).getModifications(actor, action, factor, modifiers)) {
          list.add(c);
        }
      }
      return list.toArray(EMPTY_LIST);
    }
  }

  public static Criterion[] getCriteria() {
    int template = getSelectedTemplate();
    int user = Request.getCurrentRequest().getUser();
    String query = "SELECT * FROM perspective_elect_criteria WHERE criteria_template = " + template + " AND (criteria_owner = " + user + " OR criteria_owner = 0)";
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

  public static int[] getTemplates() {
    String sql = "select DISTINCT(criteria_template) from perspective_elect_criteria where criteria_owner = 0 or criteria_owner = " + Personalize.getUser();
    return Database.queryIds(sql);
  }

  public final static int getSelectedTemplate() {
    return Personalize.getFavourite(FormElectionaryCriteriaTemplate.ID, 0, 0);
  }

  public final static void selectTemplate(int template) {
    Personalize.addFavourite(FormElectionaryCriteriaTemplate.ID, template, 0);
  }

  public final static void copySelectedTemplate() {
    int user = Request.getCurrentRequest().getUser();
    Database.execute("DELETE FROM perspective_elect_criteria WHERE criteria_template = 0 AND criteria_owner = " + user);

    for (Criterion criterion: getCriteria()) {
      criterion.store();
    }
    Criteria.selectTemplate(0);
  }

}