package astrolab.criteria;

import java.util.Calendar;

import astrolab.astronom.ActivePoint;

public abstract class CriterionPosition extends Criterion {

  private int[] mark = new int[360];

  protected CriterionPosition() {
    super();
  }

  protected CriterionPosition(int id, int type, int activePoint, String color) {
    super(id, type, activePoint, color);
  }

  protected int getMark(int position) {
    return mark[position];
  }

  public int getMark(Calendar periodStart, Calendar periodEnd) {
    return mark[(int) ActivePoint.getActivePoint(getActivePoint(), periodStart).getPosition()];
  }

  public void setMark(int position, int mark) {
    this.mark[position] = mark;
  }

}
