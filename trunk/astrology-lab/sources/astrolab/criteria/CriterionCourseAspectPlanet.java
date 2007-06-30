package astrolab.criteria;

import astrolab.astronom.Aspects;
import astrolab.astronom.SpacetimeEvent;
import astrolab.web.server.content.LocalizedStringBuffer;

public class CriterionCourseAspectPlanet extends Criterion {

  private int[] factors;
  private int[] aspects;

  public CriterionCourseAspectPlanet() {
    super();
  }

  public CriterionCourseAspectPlanet(int id, int activePoint, int passivePlanet, int aspect) {
    super(id, TYPE_COURSE_PLANET_ASPECT, activePoint, passivePlanet, aspect);
    factors = new int[] { getFactor() };
    aspects = new int[] { Aspects.getDegrees(aspect) };
  }

  public int getMark(SpacetimeEvent periodStart, SpacetimeEvent periodEnd) {
    Course course = new Course(getActivePoint(), factors, periodStart, periodEnd);
    return course.occurAspect(aspects) ? 1 : 0;
  }

  public String getName() {
    return "CoursePlanetAspect";
  }

  public String[] getActorTypes() {
    return new String[] { "Planet", "'course'", "Aspect", "Planet" };
  }

  protected void store(String[] inputValues) {
    int active = Integer.parseInt(inputValues[0]);
    int aspect = Integer.parseInt(inputValues[2]);
    int passive = Integer.parseInt(inputValues[3]);
    new CriterionCourseAspectPlanet(getId(), active, passive, aspect).store();
  }

  public void toString(LocalizedStringBuffer output) {
    output.localize(getActor());
    output.append(" ");
    output.localize(getAction());
    output.append(" ");
    output.localize(getFactor());
    output.append(" ");
    output.localize("of course");
  }

}
