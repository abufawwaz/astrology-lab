package astrolab.criteria;

import astrolab.astronom.SpacetimeEvent;
import astrolab.web.server.content.LocalizedStringBuffer;

public class CriterionCourseVoid extends Criterion {

  public CriterionCourseVoid() {
    super();
  }

  public CriterionCourseVoid(int id, int activePoint, String color) {
    super(id, TYPE_COURSE_VOID, activePoint, color);
  }

  public int getMark(SpacetimeEvent periodStart, SpacetimeEvent periodEnd) {
    Course course = new Course(getActivePoint(), Course.MAJOR_POINTS, periodStart, periodEnd);
    return course.occurAspect(Course.MAJOR_ASPECTS) ? 0 : 1;
  }

  public String getName() {
    return "CourseVoid";
  }

  public String[] getActorTypes() {
    return new String[] { "Planet", "'course'", "'void'" };
  }

  protected void store(String[] inputValues) {
    new CriterionCourseVoid(getId(), Integer.parseInt(inputValues[0]), "black").store();
  }

  public void toString(LocalizedStringBuffer output) {
    output.localize(getActor());
    output.append(" ");
    output.localize("is");
    output.append(" ");
    output.localize("void of course");
  }

}
