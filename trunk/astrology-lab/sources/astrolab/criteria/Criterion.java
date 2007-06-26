package astrolab.criteria;

import java.sql.ResultSet;
import java.sql.SQLException;

import astrolab.astronom.SpacetimeEvent;
import astrolab.db.Database;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public abstract class Criterion {

  public final static int TYPE_LOCATION = 1;
  public final static int TYPE_ZODIAC_SIGN = 2;
  public final static int TYPE_POSITION_DIRECTION = 3;
  public final static int TYPE_COURSE_DIRECTION = 4;
  public final static int TYPE_COURSE_VOID = 5;
  public final static int TYPE_POSITION_PHASE = 6;
  public final static int TYPE_TIME_OF_WEEK = 7;
  public final static int TYPE_POSITION_PLANET_HOUSE = 8;
  public final static int TYPE_IS_HOUSE_RULER = 9;

  private int id;
  private int type;
  private int action;
  private int factor;
  private int activePoint;
  private int multiply = 1;
  private String color = "black";

  protected Criterion() {
  }

  protected Criterion(int id, int type, int activePoint) {
    this.id = id;
    this.type = type;
    this.activePoint = activePoint;
  }

  protected Criterion(int id, int type, int activePoint, int factor) {
    this(id, type, activePoint);
    this.factor = factor;
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

  public int getMultiplyBy() {
    return multiply;
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
    int type = query.getInt(4);
    Criterion criterion = null;

    switch (type) {
      case TYPE_TIME_OF_WEEK: {
        criterion = new CriterionTimeOfWeek(query.getInt(1), query.getInt(5));
        break;
      }
      case TYPE_ZODIAC_SIGN: {
        criterion = new CriterionZodiacSign(query.getInt(1), query.getInt(5), query.getInt(7), false);
        break;
      }
      case TYPE_POSITION_DIRECTION: {
        criterion = new CriterionPositionDirection(query.getInt(1), query.getInt(5), query.getInt(7));
        break;
      }
      case TYPE_COURSE_DIRECTION: {
        criterion = new CriterionCourseDirection(query.getInt(1), query.getInt(5), query.getInt(7));
        break;
      }
      case TYPE_COURSE_VOID: {
        criterion = new CriterionCourseVoid(query.getInt(1), query.getInt(5));
        break;
      }
      case TYPE_POSITION_PHASE: {
        criterion = new CriterionPositionPhase(query.getInt(1), query.getInt(5), query.getInt(7));
        break;
      }
      case TYPE_POSITION_PLANET_HOUSE: {
        criterion = new CriterionPositionPlanetInHouse(query.getInt(1), query.getInt(5), query.getInt(7));
        break;
      }
      case TYPE_IS_HOUSE_RULER: {
        criterion = new CriterionRulerOfHouse(query.getInt(1), query.getInt(5), query.getInt(7));
        break;
      }
      default: {
//        throw new IllegalStateException(" Type " + type + " is not a valid criteria type.");
      }
    }

    criterion.type = type;
    criterion.color = query.getString(8);
    criterion.multiply = query.getInt(9);
    return criterion;
  }

  protected final void store() {
    int user = Request.getCurrentRequest().getUser();
    Database.execute("INSERT INTO perspective_elect_criteria (criteria_template, criteria_owner, criteria_type, criteria_actor, criteria_action, criteria_factor, criteria_color, criteria_multiply) VALUES ('0', '" + user + "', '" + getType() + "', '" + getActor() + "', '" + getAction() + "', '" + getFactor() + "', '" + getColor() + "', '" + getMultiplyBy() + "')");
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

  public void changeMultiply(boolean increase) {
    int user = Request.getCurrentRequest().getUser();
    int newMultiply = getMultiplyBy() + (increase ? 1 : -1);

    if (newMultiply == 0) {
      newMultiply += (increase ? 1 : -1);
    }
    Database.execute("UPDATE perspective_elect_criteria SET criteria_multiply = '" + newMultiply + "' WHERE criteria_id = " + getId() + " AND criteria_owner = " + user);
  }

  protected abstract void store(String[] inputValues);

  public abstract void toString(LocalizedStringBuffer output);

}