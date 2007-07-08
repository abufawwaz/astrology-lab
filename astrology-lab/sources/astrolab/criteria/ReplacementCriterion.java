package astrolab.criteria;

import astrolab.astronom.SpacetimeEvent;

class ReplacementCriterion extends Criterion {

  private int actor;
  private int action;
  private int factor;
  private int modifiers;
  private Criterion criterion;

  ReplacementCriterion(Criterion criterion, int actor, int action, int factor, int modifiers) {
    super(criterion.getAlgorithm().getType());
    this.criterion = criterion;
    this.actor = actor;
    this.action = action;
    this.factor = factor;
    this.modifiers = modifiers;
  }

  public int getActor() {
    return (actor > 0) ? actor : criterion.getActor();
  }

  public int getAction() {
    return (action > 0) ? action : criterion.getAction();
  }

  public int getFactor() {
    return (factor > 0) ? factor : criterion.getFactor();
  }

  public int getModifiers() {
    return modifiers;
  }

  public int calculateMark(SpacetimeEvent periodStart, SpacetimeEvent periodEnd) {
    throw new IllegalStateException();
  }

}
