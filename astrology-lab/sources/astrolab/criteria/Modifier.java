package astrolab.criteria;

import astrolab.astronom.SpacetimeEvent;

public interface Modifier {

  public final static int MARK_NO_EFFECT = -1;

  public final static int MODIFIER_ALL = 0x11111111;
  public final static int MODIFIER_ACTOR_SELF = 0x00000000;
  public final static int MODIFIER_ACTOR_COURSE = 0x00000001;
  public final static int MODIFIER_ACTOR_RULER = 0x00000010;
  public final static int MODIFIER_FACTOR_RULER = 0x00000020;
  public final static int MODIFIER_ACTION_ASPECT_APPLYING = 0x00000100;
  public final static int MODIFIER_ACTION_ASPECT_SEPARATING = 0x00000200;

  Criterion getModifiedCriterion(Criterion criterion, SpacetimeEvent periodStart, SpacetimeEvent periodEnd);

  int getModifiedMark(Criterion criterion, SpacetimeEvent periodStart, SpacetimeEvent periodEnd);

  String actorToString(Criterion criterion, String actor);

  String actionToString(Criterion criterion, String action);

  String factorToString(Criterion criterion, String factor);

  String getSelectedString();

  String getUnselectedString();

}