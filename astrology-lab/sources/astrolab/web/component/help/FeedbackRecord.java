package astrolab.web.component.help;

import astrolab.db.Database;

class FeedbackRecord {

  private int id;
  private int approves;
  private int disapproves;
  private int author;
  private int has_approved;
  private int has_disapproved;
  private String text;

  FeedbackRecord(int id, String text, int approves, int disapproves, int author, int has_approved, int has_disapproved) {
    this.id = id;
    this.text = text;
    this.approves = approves;
    this.disapproves = disapproves;
    this.author = author;
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

  public int getAuthor() {
    return author;
  }

  public String getText() {
    return text;
  }

  public boolean hasApproved() {
    return has_approved > 0;
  }

  public boolean hasDisapproved() {
    return has_disapproved > 0;
  }

  public static int store(int person, int project, String message) {
    Database.execute("INSERT INTO help_project (commenter_id, project_id, comment_text) VALUES (" + person + ", " + project + ", '" + message + "')");

    int id = Integer.parseInt(Database.query("SELECT comment_id FROM help_project WHERE commenter_id = " + person + " AND project_id = " + project + " AND comment_text = '" + message + "'"));
    approve(person, project, id);

    return id;
  }

  public static void approve(int person, int project, int commentId) {
    Database.execute("DELETE FROM help_feedback WHERE id=" + commentId + " and user_id=" + person);
    Database.execute("INSERT INTO help_feedback VALUES (" + commentId + ", " + person + ", 'yes')");
  }

  public static void disapprove(int person, int project, int commentId) {
    Database.execute("DELETE FROM help_feedback WHERE id=" + commentId + " and user_id=" + person);
    Database.execute("INSERT INTO help_feedback VALUES (" + commentId + ", " + person + ", 'no')");
  }

}