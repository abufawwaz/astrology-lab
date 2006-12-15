package astrolab.web.component.help;

import java.util.Properties;

import astrolab.db.Personalize;
import astrolab.db.Text;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.component.ComponentLink;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayProjectDescription extends HTMLFormDisplay {

  public DisplayProjectDescription() {
    super(HTMLFormDisplay.getId(DisplayProjectDescription.class), true);
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    operate(request);

    int project = Personalize.getFavourite(-1, Text.getId("user.session.project"), -1);
    if (project > 0) {
      buffer.append("<center><h2>");
      buffer.localize(project);
      buffer.append("</h2></center>");

      buffer.append("<style type='text/css'>textarea { width:95%; }</style>");
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
          Properties parameters = new Properties();
          parameters.put("approve", String.valueOf(record.getId()));
          ComponentLink.fillLink(buffer, DisplayProjectDescription.class, parameters, String.valueOf(record.getApproves()));
        } else {
          buffer.append(record.getApproves());
        }
        buffer.append("</td>");
        buffer.append("<td>");
        if (!isAuthor && !record.hasDisapproved()) {
          Properties parameters = new Properties();
          parameters.put("disapprove", String.valueOf(record.getId()));
          ComponentLink.fillLink(buffer, DisplayProjectDescription.class, parameters, String.valueOf(record.getDisapproves()));
        } else {
          buffer.append(record.getDisapproves());
        }
        buffer.append("</td>");
        buffer.append("</tr>");
      }

      buffer.append("<tr>");
      buffer.append("\r\n<td><center>");
      buffer.append("<textarea name='feedback' cols='80%' rows='10'></textarea>");
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