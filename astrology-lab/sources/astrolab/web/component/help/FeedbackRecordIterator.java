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
    final String QUERY = "SELECT DISTINCT(text.id), TABLE1.app1, TABLE2.app2, TABLE3.app3, TABLE11.app11, TABLE21.app21 FROM text " +

    "LEFT JOIN (SELECT text.id as app3_id, help_project.commenter_id as app3 FROM text, help_project WHERE text.id = help_project.comment_id AND help_project.project_id = " + project + ") as TABLE3 " +
    "ON text.id = TABLE3.app3_id " + 

    "LEFT JOIN (SELECT text.id as app1_id, COUNT(user_id) as app1 from text, help_feedback WHERE approve = 'yes' AND text.id = help_feedback.id GROUP BY app1_id) AS TABLE1 " +
    "ON text.id = TABLE1.app1_id " +

    "LEFT JOIN (SELECT text.id as app11_id, COUNT(user_id) as app11 from text, help_feedback WHERE approve = 'yes' AND user_id = " + request.getUser() + " AND text.id = help_feedback.id GROUP BY app11_id) AS TABLE11 " +
    "ON text.id = TABLE11.app11_id " +

    "LEFT JOIN (SELECT text.id as app2_id, COUNT(user_id) as app2 from text, help_feedback WHERE approve = 'no' AND text.id = help_feedback.id GROUP BY app2_id) AS TABLE2 " +
    "ON text.id = TABLE2.app2_id " +

    "LEFT JOIN (SELECT text.id as app21_id, COUNT(user_id) as app21 from text, help_feedback WHERE approve = 'no' AND user_id = " + request.getUser() + " AND text.id = help_feedback.id GROUP BY app21_id) AS TABLE21 " +
    "ON text.id = TABLE21.app21_id " +

    "WHERE (TABLE1.app1 IS NOT NULL OR TABLE2.app2 IS NOT NULL) AND TABLE3.app3_id IS NOT NULL";

    if (sort == SORT_BY_VOTE) {
      return new FeedbackRecordIterator(Database.executeQuery(QUERY + " ORDER BY TABLE1.app1 DESC, TABLE2.app2, text.id"));
    } else {
      return new FeedbackRecordIterator(Database.executeQuery(QUERY + " ORDER BY text.id"));
    }
  }

  public FeedbackRecord next() {
    return (FeedbackRecord) super.next();
  }

  protected Object read() throws Exception {
    return new FeedbackRecord(set.getInt(1), set.getInt(2), set.getInt(3), set.getInt(4), set.getInt(5), set.getInt(6));
  }
}