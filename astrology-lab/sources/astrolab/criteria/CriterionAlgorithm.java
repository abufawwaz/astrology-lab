package astrolab.criteria;

import java.util.Hashtable;

import astrolab.astronom.SpacetimeEvent;

public abstract class CriterionAlgorithm {

  public final static int ALGORITHM_SIGN = 2;
  public final static int ALGORITHM_DIRECTION = 3;
  public final static int ALGORITHM_PHASE = 4;
  public final static int ALGORITHM_DAY_OF_WEEK = 5;
  public final static int ALGORITHM_HOUSE = 6;
  public final static int ALGORITHM_ASPECT = 7;
  public final static int ALGORITHM_VOID = 8;

  final static Hashtable<Integer, CriterionAlgorithm> ALGORITHMS = new Hashtable<Integer, CriterionAlgorithm>();

  private final int type;

  protected CriterionAlgorithm(int type) {
    this.type = type;
  }

  public abstract boolean accepts(Criterion criterion);

  public boolean isMarkPositive() {
    return true;
  }

  public abstract int calculateMark(Criterion criterion, SpacetimeEvent periodStart, SpacetimeEvent periodEnd);

  public String[] getActorTypes() {
    return new String[0];
  }

  protected String toStringBeforeActor(Criterion criterion) {
    return "";
  }

  protected String toStringAfterActor(Criterion criterion) {
    return "";
  }

  public String[] getActionTypes() {
    return new String[0];
  }

  protected String toStringBeforeAction(Criterion criterion) {
    return "";
  }

  protected String toStringAfterAction(Criterion criterion) {
    return "";
  }

  public String[] getFactorTypes() {
    return new String[0];
  }

  protected String toStringBeforeFactor(Criterion criterion) {
    return "";
  }

  protected String toStringAfterFactor(Criterion criterion) {
    return "";
  }

  public final int getType() {
    return type;
  }

  static {
    ALGORITHMS.put(ALGORITHM_ASPECT, new CriterionAspect());
    ALGORITHMS.put(ALGORITHM_DAY_OF_WEEK, new CriterionDayOfWeek());
    ALGORITHMS.put(ALGORITHM_DIRECTION, new CriterionDirection());
    ALGORITHMS.put(ALGORITHM_HOUSE, new CriterionHouse());
    ALGORITHMS.put(ALGORITHM_PHASE, new CriterionPhase());
    ALGORITHMS.put(ALGORITHM_SIGN, new CriterionSign());
    ALGORITHMS.put(ALGORITHM_VOID, new CriterionVoid());
  }

  public final static CriterionAlgorithm getAlgorithm(int type) {
    CriterionAlgorithm result = ALGORITHMS.get(type);
    if (result == null) {
      throw new IllegalStateException("Algorithm of type " + type + " is not registered!");
    }
    return result;
  }

}