package astrolab.web.component.help;

import astrolab.db.Action;
import astrolab.db.Personalize;
import astrolab.db.Text;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayProjectDescription extends HTMLFormDisplay {

  public DisplayProjectDescription() {
    super(Action.getAction(-1, -1, HTMLFormDisplay.getId(DisplayProjectDescription.class)));
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    operate(request);

    int project = Personalize.getFavourite(-1, Text.getId("user.session.project"), -1);
    if (project > 0) {
      buffer.append("<center><h2>");
      buffer.localize(project);
      buffer.append("</h2></center>");

      buffer.append("<input type='hidden' name='approve' value='-1' />");
      buffer.append("<input type='hidden' name='disapprove' value='-1' />");
      buffer.append("<table width='100%' border='0'>");
      buffer.append("<tr><th>");
      buffer.localize("Message");
      buffer.append(" / ");
      buffer.localize("Opinion");
      buffer.append("</th>");
      buffer.append("<th width='30'>");
      buffer.localize("Pro");
      buffer.append("</th><th width='30'>");
      buffer.append(Text.getText("Con"));
      buffer.append("</th>");
      buffer.append("</tr>");

      FeedbackRecordIterator records = FeedbackRecordIterator.iterate(request, project);

      if (!records.hasNext()) {
        buffer.append("<tr><td colspan='3'>");
        buffer.localize("No opinions at the moment.");
        buffer.append("</td></tr>");
      }

      while (records.hasNext()) {
        FeedbackRecord record = records.next();
        boolean isAuthor = (record.getAuthor() == Personalize.getUser(false));

        buffer.append("<tr>");
        buffer.append("<td>");
        buffer.localize(record.getId());
        buffer.signature(record.getAuthor());
        buffer.append("</td>");

        buffer.append("<td>");
        if (!isAuthor && !record.hasApproved()) {
          buffer.append("<a href='javascript:document.forms[0].approve.value=" + record.getId() + ";document.forms[0].submit();'>");
        }
        buffer.append(record.getApproves());
        if (!isAuthor && !record.hasApproved()) {
          buffer.append("</a>");
        }
        buffer.append("</td>");
        buffer.append("<td>");
        if (!isAuthor && !record.hasDisapproved()) {
          buffer.append("<a href='javascript:document.forms[0].disapprove.value=" + record.getId() + ";document.forms[0].submit();'>");
        }
        buffer.append(record.getDisapproves());
        if (!isAuthor && !record.hasDisapproved()) {
          buffer.append("</a>");
        }
        buffer.append("</td>");
        buffer.append("</tr>");
      }

      buffer.append("<tr>");
      buffer.append("\r\n<td><center>");
      buffer.append("<textarea name='feedback' cols='65%' rows='10'></textarea>");
      buffer.append("</center></td>");
      buffer.append("\r\n<td colspan='2' valign='bottom'><center>");
      buffer.append("<input type='submit' value='");
      buffer.localize("Save");
      buffer.append("' />");
      buffer.append("</center></td>");
      buffer.append("\r\n</tr>");

      buffer.append("\r\n</table>");
    } else {
      buffer.localize("Please, select a project!");
    }
  }

  public void operate(Request request) {
    int project = Personalize.getFavourite(-1, Text.getId("user.session.project"), -1);
    if (project > 0) {
      String feedback = request.get("feedback");
      if (feedback != null && feedback.trim().length() > 0) {
        FeedbackRecord.store(request.getUser(), project, feedback.trim());
        return;
      }
      String approve = request.get("approve");
      if (approve != null) {
        int record = Integer.parseInt(approve);
        if (record > 0) {
          FeedbackRecord.approve(request.getUser(), project, record);
          return;
        }
      }
      String disapprove = request.get("disapprove");
      if (disapprove != null) {
        int record = Integer.parseInt(disapprove);
        if (record > 0) {
          FeedbackRecord.disapprove(request.getUser(), project, record);
          return;
        }
      }
    }
  }
}