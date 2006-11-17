package astrolab.web.component.help;

import astrolab.db.Database;
import astrolab.db.Text;

class FeedbackRecord {

  private int id;
  private int approves;
  private int disapproves;
  private int has_approved;
  private int has_disapproved;

  FeedbackRecord(int id, int approves, int disapproves, int has_approved, int has_disapproved) {
    this.id = id;
    this.approves = approves;
    this.disapproves = disapproves;
    this.has_approved = has_approved;
    this.has_disapproved = has_disapproved;
  }

  public int getId() {
    return id;
  }

  public int getApproves() {
    return approves;
  }

  public int getDisapproves() {
    return disapproves;
  }

  public boolean hasApproved() {
    return has_approved > 0;
  }

  public boolean hasDisapproved() {
    return has_disapproved > 0;
  }

  public static int store(int person, int project, String message) {
    int id = Text.reserve(message, Text.TYPE_HELP_FEEDBACK);

    Database.execute("DELETE FROM help_project WHERE comment_id = " + id);
    Database.execute("INSERT INTO help_project VALUES (" + id + ", " + person + ", " + project + ")");

    approve(person, project, id);
    return id;
  }

  public static void approve(int person, int project, int message) {
    Database.execute("DELETE FROM help_feedback WHERE user_id = " + person + " AND id = " + message);
    Database.execute("INSERT INTO help_feedback VALUES (" + message + ", " + person + ", 'yes')");
  }

  public static void disapprove(int person, int project, int message) {
    Database.execute("DELETE FROM help_feedback WHERE user_id = " + person + " AND id = " + message);
    Database.execute("INSERT INTO help_feedback VALUES (" + message + ", " + person + ", 'no')");
  }

}