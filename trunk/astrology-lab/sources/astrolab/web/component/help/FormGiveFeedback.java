package astrolab.web.component.help;

import java.util.Properties;

import astrolab.db.Personalize;
import astrolab.db.Text;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.component.ComponentLink;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class FormGiveFeedback extends HTMLFormDisplay {

  public FormGiveFeedback() {
    super(HTMLFormDisplay.getId(FormGiveFeedback.class), true);
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    operate(request);

    buffer.append("<style type='text/css'>");
    buffer.append("\r\ntextarea { width:95%; }");
    buffer.append("\r\n</style>");

    buffer.append("<table border='0'>");
    buffer.append("<tr>");
    buffer.append("<th width='30'>");
    buffer.localize("Pro");
    buffer.append("</th><th width='30'>");
    buffer.append(Text.getText("Con"));
    buffer.append("</th><th>");
    buffer.localize("Message");
    buffer.append(" / ");
    buffer.localize("Opinion");
    buffer.append("</th>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td colspan='3' class='class_input'><center>");
    buffer.append("<textarea name='feedback' cols='80%' rows='5'></textarea>");
    buffer.append("<br />");
    buffer.append("<input type='submit' value='");
    buffer.localize("Save");
    buffer.append("' />");
    buffer.append("</center><hr /></td>");
    buffer.append("</tr>");

    FeedbackRecordIterator records = FeedbackRecordIterator.iterate(request, 0, FeedbackRecordIterator.SORT_BY_VOTE);
    while (records.hasNext()) {
      FeedbackRecord record = records.next();
      boolean isAuthor = (record.getAuthor() == Personalize.getUser());
      buffer.append("<tr>");
      buffer.append("<td>");
      if (!isAuthor && !record.hasApproved()) {
        Properties parameters = new Properties();
        parameters.put("approve", String.valueOf(record.getId()));
        ComponentLink.fillLink(buffer, FormGiveFeedback.class, parameters, String.valueOf(record.getApproves()));
      } else {
        buffer.append(record.getApproves());
      }
      buffer.append("</td>");
      buffer.append("<td>");
      if (!isAuthor && !record.hasDisapproved()) {
        Properties parameters = new Properties();
        parameters.put("disapprove", String.valueOf(record.getId()));
        ComponentLink.fillLink(buffer, FormGiveFeedback.class, parameters, String.valueOf(record.getDisapproves()));
      } else {
        buffer.append(record.getDisapproves());
      }
      buffer.append("</td>");
      buffer.append("<td>");
      buffer.append(record.getText());
      buffer.signature(record.getAuthor());
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