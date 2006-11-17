package astrolab.web.component.help;

import astrolab.db.Action;
import astrolab.db.Personalize;
import astrolab.db.Text;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class FormGiveFeedback extends HTMLFormDisplay {

  public FormGiveFeedback() {
    super(Action.getAction(-1, -1, HTMLFormDisplay.getId(FormGiveFeedback.class)));
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    operate(request);

    buffer.append("<input type='hidden' name='approve' value='-1' />");
    buffer.append("<input type='hidden' name='disapprove' value='-1' />");
    buffer.append("<table border='0'>");
    buffer.append("<tr>");
    buffer.append("<th width=30>");
    buffer.localize("Pro");
    buffer.append("</th><th width=30>");
    buffer.append(Text.getText("Con"));
    buffer.append("</th><th>");
    buffer.localize("Message");
    buffer.append(" / ");
    buffer.localize("Opinion");
    buffer.append("</th>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td colspan=3><center>");
    buffer.append("<textarea name='feedback' cols='80%' rows='10'></textarea>");
    buffer.append("<br>");
    buffer.append("<input type='submit' value='");
    buffer.localize("Save");
    buffer.append("' />");
    buffer.append("</center><hr /></td>");
    buffer.append("</tr>");

    FeedbackRecordIterator records = FeedbackRecordIterator.iterate(request, 0, FeedbackRecordIterator.SORT_BY_VOTE);
    while (records.hasNext()) {
      FeedbackRecord record = records.next();
      buffer.append("<tr>");
      buffer.append("<td>");
      if (!record.hasApproved()) {
        buffer.append("<a href='javascript:document.forms[0].approve.value=" + record.getId() + ";document.forms[0].submit();'>");
      }
      buffer.append(record.getApproves());
      if (!record.hasApproved()) {
        buffer.append("</a>");
      }
      buffer.append("</td>");
      buffer.append("<td>");
      if (!record.hasDisapproved()) {
        buffer.append("<a href='javascript:document.forms[0].disapprove.value=" + record.getId() + ";document.forms[0].submit();'>");
      }
      buffer.append(record.getDisapproves());
      if (!record.hasDisapproved()) {
        buffer.append("</a>");
      }
      buffer.append("</td>");
      buffer.append("<td>");
      buffer.localize(record.getId());
      buffer.signature(Personalize.getUser());
      buffer.append("</td>");
      buffer.append("</tr>");
    }

    buffer.append("</table>");
  }

  public void operate(Request request) {
    String feedback = request.get("feedback");
    if (feedback != null && feedback.trim().length() > 0) {
      FeedbackRecord.store(request.getUser(), 0, feedback.trim());
      return;
    }
    String approve = request.get("approve");
    if (approve != null) {
      int record = Integer.parseInt(approve);
      if (record > 0) {
        FeedbackRecord.approve(request.getUser(), 0, record);
        return;
      }
    }
    String disapprove = request.get("disapprove");
    if (disapprove != null) {
      int record = Integer.parseInt(disapprove);
      if (record > 0) {
        FeedbackRecord.disapprove(request.getUser(), 0, record);
        return;
      }
    }
  }

}