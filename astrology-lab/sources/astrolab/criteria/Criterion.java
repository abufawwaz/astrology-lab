package astrolab.criteria;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

import astrolab.astronom.Aspects;
import astrolab.astronom.SpacetimeEvent;
import astrolab.astronom.houses.HouseSystem;
import astrolab.astronom.planet.SolarSystem;
import astrolab.db.Database;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class Criterion {

  public final static String TYPE_PLANET = "Planet";
  public final static String TYPE_HOUSE = "House";
  public final static String TYPE_SIGN = "Sign";
  public final static String TYPE_ASPECT = "Aspect";
  public final static String TYPE_DIRECTION = "Direction";
  public final static String TYPE_PHASE = "Phase";
  public final static Hashtable<String, ArrayList<Integer>> TYPES = new Hashtable<String, ArrayList<Integer>>();

  private final static Criterion[] EMPTY_ARRAY = new Criterion[0];

  private int id;
  private int actor;
  private int action;
  private int factor;
  private int multiply = 1;
  private String color = "black";
  private Modifiers modifiers;
  private CriterionAlgorithm algorithm;

  static {
    ArrayList<Integer> list = new ArrayList<Integer>();
    for (int planet: SolarSystem.PLANETS_IDS) {
      list.add(planet);
    }
    TYPES.put(TYPE_PLANET, list);

    list = new ArrayList<Integer>();
    list.add(Aspects.CONJUNCT);
    list.add(Aspects.SEXTILE);
    list.add(Aspects.SQUARE);
    list.add(Aspects.TRINE);
    list.add(Aspects.OPPOSITION);
    TYPES.put(TYPE_ASPECT, list);

    list = new ArrayList<Integer>();
    list.add(CriterionDirection.ID_DIRECT);
    list.add(CriterionDirection.ID_STATIONARY);
    list.add(CriterionDirection.ID_RETROGRADE);
    TYPES.put(TYPE_DIRECTION, list);

    list = new ArrayList<Integer>();
    for (int phase: CriterionPhase.PHASES) {
      list.add(phase);
    }
    TYPES.put(TYPE_PHASE, list);

    list = new ArrayList<Integer>();
    for (int sign: CriterionSign.SIGNS) {
      list.add(sign);
    }
    TYPES.put(TYPE_SIGN, list);

    list = new ArrayList<Integer>();
    for (int sign: HouseSystem.HOUSES) {
      list.add(sign);
    }
    TYPES.put(TYPE_HOUSE, list);
  }

  protected Criterion(int id, int algorithm, int modifiers, int actor, int action, int factor, String color, int multiply) {
    this.id = id;
    this.algorithm = CriterionAlgorithm.getAlgorithm(algorithm);
    this.modifiers = new Modifiers(modifiers);
    this.actor = actor;
    this.action = action;
    this.factor = factor;
    this.color = color;
    this.multiply = multiply;
  }

  protected Criterion(int algorithm) {
    this.algorithm = CriterionAlgorithm.getAlgorithm(algorithm);
  }

  protected Criterion(int id, int algorithm, int actor) {
    this(algorithm);
    this.id = id;
    this.actor = actor;
  }

  protected Criterion(int id, int algorithm, int actor, int factor) {
    this(id, algorithm, actor);
    this.factor = factor;
  }

  protected Criterion(int id, int algorithm, int actor, int factor, int action) {
    this(id, algorithm, actor, factor);
    this.action = action;
  }

  public final Criterion[] getModifications(int actor, int action, int factor, int modifiers) {
    CriterionAlgorithm algorithm = getAlgorithm();
    HashSet<Integer> actorMods = new HashSet<Integer>();
    HashSet<Integer> actionMods = new HashSet<Integer>();
    HashSet<Integer> factorMods = new HashSet<Integer>();

    if (actor != 0) {
      if (algorithm.getActorTypes().length == 0) {
        return EMPTY_ARRAY;
      }

      for (String actorType: algorithm.getActorTypes()) {
        ArrayList<Integer> types = TYPES.get(actorType);
        if ((types != null) && types.contains(actor)) {
          for (int mod: Modifiers.ACTOR_MODIFIERS) {
            if ((modifiers & mod) == mod) {
              actorMods.add(mod);
            } else {
              actorMods.add(0);
            }
          }
        }
      }
    } else {
      if (algorithm.getActorTypes().length > 0) {
        for (int mod: Modifiers.ACTOR_MODIFIERS) {
          if ((modifiers & mod) == mod) {
            actorMods.add(mod);
          } else {
            actorMods.add(0);
          }
        }
      } else {
        actorMods.add(0);
      }
    }

    if (action != 0) {
      if (algorithm.getActionTypes().length == 0) {
        return EMPTY_ARRAY;
      }

      for (String actionType: algorithm.getActionTypes()) {
        ArrayList<Integer> types = TYPES.get(actionType);
        if ((types != null) && types.contains(action)) {
          for (int mod: Modifiers.ACTION_MODIFIERS) {
            if ((modifiers & mod) == mod) {
              actionMods.add(mod);
            } else {
              actionMods.add(0);
            }
          }
        }
      }
    } else if (algorithm.getActionTypes().length > 0) {
      for (int mod: Modifiers.ACTION_MODIFIERS) {
        if ((modifiers & mod) == mod) {
          actionMods.add(mod);
        } else {
          actionMods.add(0);
        }
      }
    } else {
      for (int mod: Modifiers.ACTION_MODIFIERS) {
        if ((modifiers & mod) == mod) {
          return EMPTY_ARRAY;
        }
      }
      actionMods.add(0);
    }

    if (factor != 0) {
      if (algorithm.getFactorTypes().length == 0) {
        return EMPTY_ARRAY;
      }

      for (String factorType: algorithm.getFactorTypes()) {
        ArrayList<Integer> types = TYPES.get(factorType);
        if ((types != null) && types.contains(factor)) {
          for (int mod: Modifiers.FACTOR_MODIFIERS) {
            if ((modifiers & mod) == mod) {
              factorMods.add(mod);
            } else {
              factorMods.add(0);
            }
          }
        }
      }
    } else if (algorithm.getFactorTypes().length > 0) {
      for (int mod: Modifiers.FACTOR_MODIFIERS) {
        if ((modifiers & mod) == mod) {
          factorMods.add(mod);
        } else {
          factorMods.add(0);
        }
      }
    } else {
      for (int mod: Modifiers.FACTOR_MODIFIERS) {
        if ((modifiers & mod) == mod) {
          return EMPTY_ARRAY;
        }
      }
      factorMods.add(0);
    }

    HashSet<Integer> mods = getAllCombinations(actorMods, actionMods, factorMods);
    if ((modifiers == Modifier.MODIFIER_ALL) || (modifiers == 0)) {
      mods.add(0);
    }

    ArrayList<Criterion> result = new ArrayList<Criterion>();
    for (int modifications: mods) {
      ReplacementCriterion modification = new ReplacementCriterion(this, actor, action, factor, modifications);
      if (getAlgorithm().accepts(modification)) {
        result.add(modification);
      }
    }

    return result.toArray(EMPTY_ARRAY);
  }

  private final HashSet<Integer> getAllCombinations(HashSet<Integer> actorMods, HashSet<Integer> actionMods, HashSet<Integer> factorMods) {
    HashSet<Integer> result = new HashSet<Integer>();

    for (int actorMod: getAllCombinations(actorMods)) {
      for (int actionMod: getAllCombinations(actionMods)) {
        for (int factorMod: getAllCombinations(factorMods)) {
          result.add(actorMod | actionMod | factorMod);
        }
      }
    }

    return result;
  }

  private final Integer[] getAllCombinations(HashSet<Integer> mods) {
    if (mods.isEmpty()) {
      return new Integer[0];
    } else if (mods.size() == 1) {
      return mods.toArray(new Integer[1]);
    } else {
      int m = mods.iterator().next();
      HashSet<Integer> result = new HashSet<Integer>();
      result.addAll(mods);
      result.remove(m);
      Integer[] combinations = getAllCombinations(result);
      result.clear();

      for (int c: combinations) {
        result.add(m | c);
      }
      return result.toArray(new Integer[0]);
    }
  }

  public int getId() {
    return id;
  }

  public CriterionAlgorithm getAlgorithm() {
    return algorithm;
  }

  public int getActor() {
    return actor;
  }

  public int getAction() {
    return action;
  }

  public int getFactor() {
    return factor;
  }

  public int getModifiers() {
    return modifiers.getModifiers();
  }

  public int getMultiplyBy() {
    return multiply;
  }

  public String getColor() {
    return color;
  }

  public final int getMark(SpacetimeEvent periodStart, SpacetimeEvent periodEnd) {
    return ((Modifiers) modifiers.clone()).getMark(this, periodStart, periodEnd);
  }

  protected static Criterion read(ResultSet query) throws SQLException {
    return new Criterion(query.getInt(1), query.getInt(4), query.getInt(10), query.getInt(5), query.getInt(6), query.getInt(7), query.getString(8), query.getInt(9));
  }

  public final void store() {
    int user = Request.getCurrentRequest().getUser();
    Database.execute("INSERT INTO perspective_elect_criteria (criteria_template, criteria_owner, criteria_type, criteria_actor, criteria_action, criteria_factor, criteria_color, criteria_multiply, criteria_modifiers) VALUES ('0', '" + user + "', '" + getAlgorithm().getType() + "', '" + getActor() + "', '" + getAction() + "', '" + getFactor() + "', '" + getColor() + "', '" + getMultiplyBy() + "', '" + getModifiers() + "')");
  }

  public final void store(String[] values) {
    new ReplacementCriterion(this, Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]), 0);
  }

  public final void delete() {
    int user = Request.getCurrentRequest().getUser();
    Database.execute("DELETE FROM perspective_elect_criteria where criteria_id = " + getId() + " AND criteria_owner = " + user);
  }

  public void changeColor() {
    final String[] COLORS = { "red", "orange", "yellow", "green", "blue", "indigo", "black" };
    int user = Request.getCurrentRequest().getUser();
    int c = 0;
    for (; c < COLORS.length; c++) {
      if (COLORS[c].equals(getColor())) {
        break;
      }
    }
    if (c < COLORS.length - 1) {
      c++;
    } else {
      c = 0;
    }
    Database.execute("UPDATE perspective_elect_criteria SET criteria_color = '" + COLORS[c] + "' WHERE criteria_id = " + getId() + " AND criteria_owner = " + user);
  }

  public void changeMultiply(boolean increase) {
    int user = Request.getCurrentRequest().getUser();
    int newMultiply = getMultiplyBy() + (increase ? 1 : -1);

    if (newMultiply == 0) {
      newMultiply += (increase ? 1 : -1);
    }
    Database.execute("UPDATE perspective_elect_criteria SET criteria_multiply = '" + newMultiply + "' WHERE criteria_id = " + getId() + " AND criteria_owner = " + user);
  }

  public final void toString(LocalizedStringBuffer output) {
    new Modifiers(getModifiers()).toString(output, this);
  }

}