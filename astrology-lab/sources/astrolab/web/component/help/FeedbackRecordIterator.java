package astrolab.web.component.help;

import java.sql.ResultSet;

import astrolab.db.Database;
import astrolab.db.RecordIterator;
import astrolab.web.server.Request;

public class FeedbackRecordIterator extends RecordIterator {

  public final static int SORT_BY_TIME = 0;
  public final static int SORT_BY_VOTE = 1;

  protected FeedbackRecordIterator(ResultSet set) {
    super(set);
  }

  public static FeedbackRecordIterator iterate(Request request, int project) {
    return iterate(request, project, SORT_BY_TIME);
  }

  public static FeedbackRecordIterator iterate(Request request, int project, int sort) {
    final String QUERY = "SELECT DISTINCT(help_project.comment_id), TABLE3.app31, TABLE1.app1, TABLE2.app2, TABLE3.app3, TABLE11.app11, TABLE21.app21 FROM help_project " +

    "LEFT JOIN (SELECT comment_id as app3_id, commenter_id as app3, comment_text as app31 FROM help_project WHERE help_project.project_id = " + project + ") as TABLE3 " +
    "ON help_project.comment_id = TABLE3.app3_id " + 

    "LEFT JOIN (SELECT help_project.comment_id as app1_id, COUNT(user_id) as app1 from help_project, help_feedback WHERE approve = 'yes' AND help_project.comment_id = help_feedback.id GROUP BY app1_id) AS TABLE1 " +
    "ON help_project.comment_id = TABLE1.app1_id " +

    "LEFT JOIN (SELECT help_project.comment_id as app11_id, COUNT(user_id) as app11 from help_project, help_feedback WHERE approve = 'yes' AND user_id = " + request.getUser() + " AND help_project.comment_id = help_feedback.id GROUP BY app11_id) AS TABLE11 " +
    "ON help_project.comment_id = TABLE11.app11_id " +

    "LEFT JOIN (SELECT help_project.comment_id as app2_id, COUNT(user_id) as app2 from help_project, help_feedback WHERE approve = 'no' AND help_project.comment_id = help_feedback.id GROUP BY app2_id) AS TABLE2 " +
    "ON help_project.comment_id = TABLE2.app2_id " +

    "LEFT JOIN (SELECT help_project.comment_id as app21_id, COUNT(user_id) as app21 from help_project, help_feedback WHERE approve = 'no' AND user_id = " + request.getUser() + " AND help_project.comment_id = help_feedback.id GROUP BY app21_id) AS TABLE21 " +
    "ON help_project.comment_id = TABLE21.app21_id " +

    "WHERE (TABLE1.app1 IS NOT NULL OR TABLE2.app2 IS NOT NULL) AND TABLE3.app3_id IS NOT NULL";

    if (sort == SORT_BY_VOTE) {
      return new FeedbackRecordIterator(Database.executeQuery(QUERY + " ORDER BY TABLE1.app1 DESC, TABLE2.app2, help_project.comment_id"));
    } else {
      return new FeedbackRecordIterator(Database.executeQuery(QUERY + " ORDER BY help_project.comment_id"));
    }
  }

  public FeedbackRecord next() {
    return (FeedbackRecord) super.next();
  }

  protected Object read() throws Exception {
    return new FeedbackRecord(set.getInt(1), set.getString(2), set.getInt(3), set.getInt(4), set.getInt(5), set.getInt(6), set.getInt(7));
  }
}