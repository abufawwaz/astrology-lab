package astrolab.criteria;

import java.util.ArrayList;
import java.util.Hashtable;

import astrolab.astronom.SpacetimeEvent;
import astrolab.db.Text;
import astrolab.web.server.content.LocalizedStringBuffer;

public class Modifiers extends ArrayList<Modifier> {

  private static final long serialVersionUID = 1L;

  private int modifiers;

  public final static int[] ACTOR_MODIFIERS = {
    Modifier.MODIFIER_ACTOR_RULER,
    Modifier.MODIFIER_ACTOR_COURSE
  };

  public final static int[] ACTION_MODIFIERS = {
    Modifier.MODIFIER_ACTION_ASPECT_APPLYING,
    Modifier.MODIFIER_ACTION_ASPECT_SEPARATING
  };

  public final static int[] FACTOR_MODIFIERS = {
    Modifier.MODIFIER_FACTOR_RULER
  };

  private final static Hashtable<Integer, Modifier> MODIFIERS = new Hashtable<Integer, Modifier>();

  static {
    MODIFIERS.put(Modifier.MODIFIER_ACTOR_RULER, new ModifierActorRuler());
    MODIFIERS.put(Modifier.MODIFIER_ACTOR_COURSE, new ModifierActorCourse());
    MODIFIERS.put(Modifier.MODIFIER_ACTION_ASPECT_APPLYING, new ModifierActionAspectApplying());
    MODIFIERS.put(Modifier.MODIFIER_ACTION_ASPECT_SEPARATING, new ModifierActionAspectSeparating());
    MODIFIERS.put(Modifier.MODIFIER_FACTOR_RULER, new ModifierFactorRuler());
  }

  Modifiers(int modifiers) {
    this.modifiers = modifiers;

    if ((Modifier.MODIFIER_ACTOR_RULER & modifiers) != 0) {
      add(MODIFIERS.get(Modifier.MODIFIER_ACTOR_RULER));
    }
    if ((Modifier.MODIFIER_ACTOR_COURSE & modifiers) != 0) {
      add(MODIFIERS.get(Modifier.MODIFIER_ACTOR_COURSE));
    }
    if ((Modifier.MODIFIER_ACTION_ASPECT_APPLYING & modifiers) != 0) {
      add(MODIFIERS.get(Modifier.MODIFIER_ACTION_ASPECT_APPLYING));
    }
    if ((Modifier.MODIFIER_ACTION_ASPECT_SEPARATING & modifiers) != 0) {
      add(MODIFIERS.get(Modifier.MODIFIER_ACTION_ASPECT_SEPARATING));
    }
    if ((Modifier.MODIFIER_FACTOR_RULER & modifiers) != 0) {
      add(MODIFIERS.get(Modifier.MODIFIER_FACTOR_RULER));
    }
  }

  int getModifiers() {
    return modifiers;
  }

  int getMark(Criterion criterion, SpacetimeEvent periodStart, SpacetimeEvent periodEnd) {
    Criterion workCriterion = criterion;
    for (Modifier m: this) {
      workCriterion = m.getModifiedCriterion(workCriterion, periodStart, periodEnd);
    }

    for (Modifier m: this) {
      int mark = m.getModifiedMark(workCriterion, periodStart, periodEnd);

      if (mark != Modifier.MARK_NO_EFFECT) {
        return mark;
      }
    }
    return workCriterion.getAlgorithm().calculateMark(workCriterion, periodStart, periodEnd);
  }

  public static int switchModifier(int modifiers, int modifier) {
    return (modifiers ^ modifier); 
  }

  public static String[] toString(int modifiers, int modifier) {
    Modifier m = MODIFIERS.get(modifier);
    if ((modifiers & modifier) != 0) {
      return new String[] {m.getSelectedString(), m.getUnselectedString() };
    } else {
      return new String[] {m.getUnselectedString(), m.getSelectedString() };
    }
  }

  void toString(LocalizedStringBuffer output, Criterion criterion) {
    CriterionAlgorithm algorithm = criterion.getAlgorithm();

    if (algorithm.getActorTypes().length > 0) {
      output.append(algorithm.toStringBeforeActor(criterion));
      output.append(" ");

      int actorId = criterion.getActor();
      String actor = (actorId > 0) ? localize(actorId) : Text.getText(algorithm.getActorTypes()[0]);
      for (Modifier m: this) {
        actor = m.actorToString(criterion, actor);
      }
      output.append(actor);

      output.append(" ");
      output.append(algorithm.toStringAfterActor(criterion));
    }
    if (algorithm.getActionTypes().length > 0) {
      output.append(algorithm.toStringBeforeAction(criterion));
      output.append(" ");

      int actionId = criterion.getAction();
      String action = (actionId > 0) ? localize(actionId) : Text.getText(algorithm.getActionTypes()[0]);
      for (Modifier m: this) {
        action = m.actionToString(criterion, action);
      }
      output.append(action);

      output.append(" ");
      output.append(algorithm.toStringAfterAction(criterion));
    }
    if (algorithm.getFactorTypes().length > 0) {
      output.append(algorithm.toStringBeforeFactor(criterion));
      output.append(" ");

      int factorId = criterion.getFactor();
      String factor = (factorId > 0) ? localize(factorId) : Text.getText(algorithm.getFactorTypes()[0]);
      for (Modifier m: this) {
        factor = m.factorToString(criterion, factor);
      }
      output.append(factor);

      output.append(" ");
      output.append(algorithm.toStringAfterFactor(criterion));
    }
  }

  private final String localize(int id) {
    return Text.getSVGText(id);
  }

}
