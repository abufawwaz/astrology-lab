package astrolab.criteria;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import astrolab.astronom.ActivePoint;
import astrolab.db.Database;
import astrolab.db.Text;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public abstract class Criterion {

  public final static int TYPE_START_TIME = 0;
  public final static int TYPE_LOCATION = 1;
  public final static int TYPE_ZODIAC_SIGN = 2;

  private int type;
  private int action;
  private int factor;
  private ActivePoint activePoint;
  private String color;

  protected Criterion(int type, ActivePoint activePoint, String color) {
    this.type = type;
    this.activePoint = activePoint;
    this.color = color;
  }

  public ActivePoint getActivePoint() {
    return activePoint;
  }

  public int getType() {
    return type;
  }

  public int getActor() {
    return Text.getId(activePoint.getName());
  }

  public int getAction() {
    return action;
  }

  public int getFactor() {
    return factor;
  }

  public String getColor() {
    return color;
  }

  public abstract int getMark(Calendar calendar);

  public String toString() {
    LocalizedStringBuffer buffer = new LocalizedStringBuffer();
    buffer.append("<table><tr><td>");
    buffer.localize(getColor());
    buffer.append("</td><td>");
    buffer.localize(getActor());
    buffer.append("</td><td>");
    buffer.localize(getAction());
    buffer.append("</td><td>");
    buffer.localize(getFactor());
    buffer.append("</td></tr></table>");
    return buffer.toString();
  }

  public int getMark(Calendar periodStart, Calendar periodEnd) {
    return Math.max(getMark(periodStart), getMark(periodEnd));
  }

  protected static Criterion read(ResultSet query) throws SQLException {
    int type = query.getInt(2);
    switch (type) {
      case TYPE_START_TIME: {
        return new CriterionStartTime(query.getInt(3), query.getInt(4)); 
      }
      case TYPE_ZODIAC_SIGN: {
        ActivePoint activePoint = ActivePoint.getActivePoint(query.getInt(3));
        String color = query.getString(6);
        return new CriterionZodiacSign(activePoint, query.getInt(5), false, color); 
      }
    }
    return null;
  }

  public void store() {
    int user = Request.getCurrentRequest().getUser();
    Database.execute("INSERT INTO perspective_elect_criteria VALUES ('" + user + "', '" + getType() + "', '" + getActor() + "', '" + getAction() + "', '" + getFactor() + "', '" + getColor() + "')");
  }

}