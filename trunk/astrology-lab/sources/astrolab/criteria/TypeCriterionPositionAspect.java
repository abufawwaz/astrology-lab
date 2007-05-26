package astrolab.criteria;

public class TypeCriterionPositionAspect extends TypeCriterion {

  public String[] getActorTypes() {
    return new String[] { "Planet", "Aspect", "Planet" };
  }

  public String getName() {
    return "Position";
  }

}
