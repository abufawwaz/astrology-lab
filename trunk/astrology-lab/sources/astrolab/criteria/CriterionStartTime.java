package astrolab.criteria;

import java.util.TimeZone;

import astrolab.astronom.SpacetimeEvent;
import astrolab.db.Database;
import astrolab.db.Text;
import astrolab.web.component.time.ComponentSelectTime;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class CriterionStartTime extends Criterion {

  public final static String REQUEST_PARAMETER = "criterion_start_time";
  private SpacetimeEvent time;

  CriterionStartTime(int id, int time1, int time2) {
    super(id, Criterion.TYPE_START_TIME, 0, "black");

    long timestamp = time2;
    timestamp <<= 31;
    timestamp |= time1;

    // TODO: check the time zone
    this.time = new SpacetimeEvent(timestamp);
  }

  public CriterionStartTime(int id, SpacetimeEvent time) {
    super(id, Criterion.TYPE_START_TIME, 0, "black");
    this.time = time;
  }

  public int getActor() {
    return Text.getId("Time");
  }

  public SpacetimeEvent getStartTime() {
    return time;
  }

  public int getMark(SpacetimeEvent periodStart, SpacetimeEvent periodEnd) {
    return (periodStart.getTimeInMillis() >= time.getTimeInMillis()) ? 1 : 0;
  }

  public String toString() {
    LocalizedStringBuffer buffer = new LocalizedStringBuffer();
    buffer.append("<table><tr><td>");
    buffer.localize("Time");
    buffer.append("</td><td>");
    ComponentSelectTime.fill(buffer, time, REQUEST_PARAMETER, true);
    buffer.append("</td></tr></table>");
    return buffer.toString();
  }

  public void store() {
    int user = Request.getCurrentRequest().getUser();
    long timestamp = time.getTimeInMillis();
    int time1 = (int) (timestamp & 0x7FFFFFFF);
    int time2 = (int) (timestamp >> 31);

    Database.execute("DELETE FROM perspective_elect_criteria WHERE criteria_owner = " + user + " AND criteria_type = " + getType());
    Database.execute("INSERT INTO perspective_elect_criteria (criteria_owner, criteria_type, criteria_actor, criteria_action, criteria_factor, criteria_color) VALUES ('" + user + "', '" + getType() + "', '" + time1 + "', '" + time2 + "', '0', 'black')");
  }

  public String getName() {
    return "StartTime";
  }

  public String[] getActorTypes() {
    return new String[0];
  }

  protected void store(String[] inputValues) {
  }

  public void toString(LocalizedStringBuffer output) {
    output.localize("Start at");
    output.append(time.toString());
  }

}
