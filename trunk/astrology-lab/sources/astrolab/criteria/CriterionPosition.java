package astrolab.criteria;

import java.util.Calendar;

import astrolab.astronom.ActivePoint;

public class CriterionPosition extends Criterion {

  private int[] mark = new int[360];

  protected CriterionPosition(int type, ActivePoint activePoint, String color) {
    super(type, activePoint, color);
  }

  protected int getMark(int position) {
    return mark[position];
  }

  public int getMark(Calendar calendar) {
    return mark[(int) getActivePoint().getPosition(calendar)];
  }

  public void setMark(int position, int mark) {
    this.mark[position] = mark;
  }

}
