package astrolab.criteria;

import astrolab.astronom.Aspects;
import astrolab.astronom.SpacetimeEvent;
import astrolab.web.server.content.LocalizedStringBuffer;

public class CriterionCourseAspect extends Criterion {

  private int[] aspects;

  public CriterionCourseAspect() {
    super();
  }

  public CriterionCourseAspect(int id, int activePoint, int aspect) {
    super(id, TYPE_COURSE_ASPECT, activePoint, aspect);
    aspects = new int[] { Aspects.getDegrees(aspect) };
  }

  public int getMark(SpacetimeEvent periodStart, SpacetimeEvent periodEnd) {
    Course course = new Course(getActivePoint(), Course.MAJOR_POINTS, periodStart, periodEnd);
    return course.occurAspect(aspects) ? 1 : 0;
  }

  public String getName() {
    return "CoursePlanetAspect";
  }

  public String[] getActorTypes() {
    return new String[] { "Planet", "'course'", "Aspect" };
  }

  protected void store(String[] inputValues) {
    int active = Integer.parseInt(inputValues[0]);
    int aspect = Integer.parseInt(inputValues[2]);
    new CriterionCourseAspect(getId(), active, aspect).store();
  }

  public void toString(LocalizedStringBuffer output) {
    output.localize(getActor());
    output.append(" ");
    output.localize(getFactor());
    output.append(" ");
    output.localize("of course");
  }

}
