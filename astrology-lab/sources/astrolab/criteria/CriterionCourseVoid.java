package astrolab.criteria;

import java.util.Calendar;

public class CriterionCourseVoid extends Criterion {

  public CriterionCourseVoid() {
    super();
  }

  public CriterionCourseVoid(int id, int activePoint, String color) {
    super(id, TYPE_COURSE_VOID, activePoint, color);
  }

  public int getMark(Calendar periodStart, Calendar periodEnd) {
//long time = System.currentTimeMillis();
try {
    Course course = new Course(getActivePoint(), Course.MAJOR_POINTS, periodStart, periodEnd);
    return course.occurAspect(Course.MAJOR_ASPECTS) ? 0 : 1;
} finally {
//  System.err.println(" time: " + (System.currentTimeMillis() - time));
}
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

}
