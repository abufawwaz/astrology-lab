package astrolab.criteria;

import java.util.ArrayList;

public abstract class TypeCriterion {

  private final static Criterion[] TYPES = new Criterion[] {
    new CriterionZodiacSign(),
    new CriterionPositionDirection(),
    new CriterionPositionPhase(),
    new CriterionPositionPlanetInHouse(),
    new CriterionCourseDirection(),
    new CriterionCourseVoid(),
    new CriterionTimeOfWeek(),
  };

  public final static boolean isCriteriaDetermined(String type, String[] inputValues) {
    String[] types = convertTypes(type, inputValues);

    for (int i = 0; i < types.length; i++) {
      if (types[i] == null) {
        return false;
      }
    }
    return true;
  }

  public final static String[][] getCriteria(String type, String[] inputValues) {
    String[] types = convertTypes(type, inputValues);
    ArrayList<String[]> list = new ArrayList<String[]>();

    for (int i = 0; i < TYPES.length; i++) {
      String[] typeTypes = TYPES[i].getActorTypes();
      if (isMatching(types, typeTypes)) {
        ArrayList<String> criteria = new ArrayList<String>();
        criteria.add(TYPES[i].getName());
        for (int t = 0; t < typeTypes.length; t++) {
          criteria.add(typeTypes[t]);
        }
        list.add(criteria.toArray((String[]) new String[0]));
      }
    }
    return list.toArray((String[][]) new String[0][]);
  }

  public final static void store(String type, String[] inputValues) {
    if (Criteria.getSelectedTemplate() > 0) {
      Criteria.copySelectedTemplate();
    }

    for (int i = 0; i < TYPES.length; i++) {
      if (TYPES[i].getName().equals(type)) {
        TYPES[i].store(inputValues);
        return;
      }
    }
  }

  public abstract String getName();

  public abstract String[] getActorTypes();

  protected abstract void store(String[] inputValues);

  private final static boolean isMatching(String[] t1, String[] t2) {
    for (int i = 0; i < t1.length; i++) {
      if (t1[i] == null) {
        continue;
      }
      if (i >= t2.length) {
        return false;
      }
      if (!t1[i].equals(t2[i])) {
        return false;
      }
    }
    return true;
  }

  private final static String[] convertTypes(String type, String[] values) {
    String[] criteriaTypes = null;
    for (int i = 0; i < TYPES.length; i++) {
      if (TYPES[i].getName().equals(type)) {
        criteriaTypes = TYPES[i].getActorTypes();
        break;
      }
    }
    if (criteriaTypes == null) {
      criteriaTypes = new String[0];
    } else {
      for (int i = 0; i < criteriaTypes.length; i++) {
        if ((values[i] == null) && !criteriaTypes[i].startsWith("'")) {
          criteriaTypes[i] = null;
        }
      }
    }
    return criteriaTypes;
  }
}