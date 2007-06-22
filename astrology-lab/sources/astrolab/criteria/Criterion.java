package astrolab.criteria;

import java.sql.ResultSet;
import java.sql.SQLException;

import astrolab.astronom.SpacetimeEvent;
import astrolab.db.Database;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public abstract class Criterion {

  public final static int TYPE_START_TIME = 0;
  public final static int TYPE_LOCATION = 1;
  public final static int TYPE_ZODIAC_SIGN = 2;
  public final static int TYPE_POSITION_DIRECTION = 3;
  public final static int TYPE_COURSE_DIRECTION = 4;
  public final static int TYPE_COURSE_VOID = 5;
  public final static int TYPE_POSITION_PHASE = 6;

  private int id;
  private int type;
  private int action;
  private int factor;
  private int activePoint;
  private String color;

  protected Criterion() {
  }

  protected Criterion(int id, int type, int activePoint, String color) {
    this.id = id;
    this.type = type;
    this.activePoint = activePoint;
    this.color = color;
  }

  public abstract String getName();

  public abstract String[] getActorTypes();

  public int getActivePoint() {
    return activePoint;
  }

  public int getId() {
    return id;
  }

  public int getType() {
    return type;
  }

  public int getActor() {
    return activePoint;
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

  public abstract int getMark(SpacetimeEvent periodStart, SpacetimeEvent periodEnd);

  public String toString() {
    LocalizedStringBuffer buffer = new LocalizedStringBuffer();
    buffer.append("<table><tr><td>");
    buffer.localize(getActor());
    buffer.append("</td><td>");
    buffer.localize(getAction());
    buffer.append("</td><td>");
    buffer.localize(getFactor());
    buffer.append("</td></tr></table>");
    return buffer.toString();
  }

  protected static Criterion read(ResultSet query) throws SQLException {
    int type = query.getInt(3);
    switch (type) {
      case TYPE_START_TIME: {
        return new CriterionStartTime(query.getInt(1), query.getInt(4), query.getInt(5)); 
      }
      case TYPE_ZODIAC_SIGN: {
        String color = query.getString(7);
        return new CriterionZodiacSign(query.getInt(1), query.getInt(4), query.getInt(6), false, color); 
      }
      case TYPE_POSITION_DIRECTION: {
        String color = query.getString(7);
        return new CriterionPositionDirection(query.getInt(1), query.getInt(4), query.getInt(6), color); 
      }
      case TYPE_COURSE_DIRECTION: {
        String color = query.getString(7);
        return new CriterionCourseDirection(query.getInt(1), query.getInt(4), query.getInt(6), color); 
      }
      case TYPE_COURSE_VOID: {
        String color = query.getString(7);
        return new CriterionCourseVoid(query.getInt(1), query.getInt(4), color); 
      }
      case TYPE_POSITION_PHASE: {
        String color = query.getString(7);
        return new CriterionPositionPhase(query.getInt(1), query.getInt(4), query.getInt(6), color); 
      }
    }
    return null;
  }

  public void store() {
    int user = Request.getCurrentRequest().getUser();
    Database.execute("INSERT INTO perspective_elect_criteria (criteria_owner, criteria_type, criteria_actor, criteria_action, criteria_factor, criteria_color) VALUES ('" + user + "', '" + getType() + "', '" + getActor() + "', '" + getAction() + "', '" + getFactor() + "', '" + getColor() + "')");
  }

  public void delete() {
    int user = Request.getCurrentRequest().getUser();
    Database.execute("DELETE FROM perspective_elect_criteria where criteria_id = " + getId() + " AND criteria_owner = " + user);
  }

  public void changeColor() {
    final String[] COLORS = { "red", "orange", "yellow", "green", "blue", "indigo", "black" };
    int user = Request.getCurrentRequest().getUser();
    int c = 0;
    for (; c < COLORS.length; c++) {
      if (COLORS[c].equals(getColor())) {
        break;
      }
    }
    if (c < COLORS.length - 1) {
      c++;
    } else {
      c = 0;
    }
    Database.execute("UPDATE perspective_elect_criteria SET criteria_color = '" + COLORS[c] + "' WHERE criteria_id = " + getId() + " AND criteria_owner = " + user);
  }

  protected abstract void store(String[] inputValues);

  public abstract void toString(LocalizedStringBuffer output);

}