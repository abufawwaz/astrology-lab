package astrolab.criteria;

import java.util.Hashtable;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.SpacetimeEvent;
import astrolab.astronom.planet.SolarSystem;
import astrolab.db.Text;

public class ModifierFactorRuler implements Modifier {

  private final static Hashtable<Integer, Integer> RULERS = new Hashtable<Integer, Integer>();

  static {
    RULERS.put(0, Text.getId(SolarSystem.MARS));
    RULERS.put(1, Text.getId(SolarSystem.VENUS));
    RULERS.put(2, Text.getId(SolarSystem.MERCURY));
    RULERS.put(3, Text.getId(SolarSystem.MOON));
    RULERS.put(4, Text.getId(SolarSystem.SUN));
    RULERS.put(5, Text.getId(SolarSystem.MERCURY));
    RULERS.put(6, Text.getId(SolarSystem.VENUS));
    RULERS.put(7, Text.getId(SolarSystem.MARS));
    RULERS.put(8, Text.getId(SolarSystem.JUPITER));
    RULERS.put(9, Text.getId(SolarSystem.SATURN));
    RULERS.put(10, Text.getId(SolarSystem.SATURN));
    RULERS.put(11, Text.getId(SolarSystem.JUPITER));
  }

  public Criterion getModifiedCriterion(Criterion criterion, SpacetimeEvent periodStart, SpacetimeEvent periodEnd) {
    int sign = (int) (ActivePoint.getActivePoint(criterion.getFactor(), periodStart).getPosition() / 30);
    return new ReplacementCriterion(criterion, RULERS.get(sign), 0, 0, 0);
  }

  public int getModifiedMark(Criterion criterion, SpacetimeEvent periodStart, SpacetimeEvent periodEnd) {
    return MARK_NO_EFFECT;
  }

  public String actorToString(Criterion criterion, String actor) {
    return actor;
  }

  public String actionToString(Criterion criterion, String action) {
    return action;
  }

  public String factorToString(Criterion criterion, String factor) {
    return "(" + Text.getText("ruler") + " " + Text.getText("of") + " " + factor + ")";
  }

  public String getSelectedString() {
    return "ruler";
  }

  public String getUnselectedString() {
    return "self";
  }

}