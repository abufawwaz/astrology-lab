package astrolab.criteria;

public class TypeCriterionZodiacSign extends TypeCriterion {

  public String[] getActorTypes() {
    return new String[] { "Planet", "Sign" };
  }

  public String getName() {
    return "Position";
  }

}
