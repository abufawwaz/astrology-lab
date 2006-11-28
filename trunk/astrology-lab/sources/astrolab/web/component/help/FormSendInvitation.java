package astrolab.web.component.help;

import java.util.Date;

import astrolab.db.Action;
import astrolab.db.Event;
import astrolab.db.User;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class FormSendInvitation extends HTMLFormDisplay {

  final static long WEEK_SPAN = 7 * 24 * 60 * 60 * 1000;

  public FormSendInvitation() {
    super(Action.getAction(-1, -1, HTMLFormDisplay.getId(FormSendInvitation.class)));
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    Event receiver = Event.getSelectedEvent();
    int receiverId = receiver.getSubjectId();
    Date lastInvitation = User.getInvitationTime(receiverId);

    if ((lastInvitation != null) && (lastInvitation.getTime() + WEEK_SPAN > System.currentTimeMillis())) {
      buffer.localize(receiverId);
      buffer.append(" has received an invitation. You cannot re-send invitation before ");
      buffer.append(new Date(lastInvitation.getTime() + WEEK_SPAN).toString());
      buffer.append("!");
    } else {
      buffer.append("<table>");

      buffer.append("<tr>");
      buffer.append("<td>");
      buffer.append("To: ");
      buffer.append("</td>");
      buffer.append("<td>");
      buffer.localize(receiverId);
      buffer.append("</td>");
      buffer.append("</tr>");

      if (User.getEmail(receiverId) == null) {
        buffer.append("<tr>");
        buffer.append("<td>");
        buffer.append("Email: ");
        buffer.append("</td>");
        buffer.append("<td>");
        buffer.append("<input type='text' size='60%' name='_email_to' value='");
        buffer.localize(receiverId);
        buffer.append("@gmail.com' />");
        buffer.append("</td>");
        buffer.append("</tr>");
      }

      buffer.append("<tr>");
      buffer.append("<td>");
      buffer.append("Subject: ");
      buffer.append("</td>");
      buffer.append("<td>");
      buffer.append("<input type='text' size='60%' name='_email_subject' value='Join www.astrology-lab.net!' />");
      buffer.append("</td>");
      buffer.append("</tr>");

      buffer.append("<tr>");
      buffer.append("<td colspan='2'>");
      buffer.append("<textarea name='_email_text' cols='70%' rows='10' value=''>");
      buffer.append("</textarea>");
      buffer.append("</td>");
      buffer.append("</tr>");

      buffer.append("<tr>");
      buffer.append("<td colspan='2'>");
      buffer.append("<hr /><center>Keep this e-mail and use it to enter <b><i>www.</i></b><b><i>astrology-lab</i></b><b><i>.net</i></b> with your profile!</center>");
      buffer.append("<br /><br /><center><input type='button' value='Enter www.astrology-lab.net' /></center>");
      buffer.append("<br /><hr /><center>Do not forward or send this e-mail to anyone!</center>");
      buffer.append("</td>");
      buffer.append("</tr>");

      buffer.append("</table>");

      buffer.append("<br />");
      buffer.append("<input type='submit' value='");
      buffer.localize("Send Invitation");
      buffer.append("' />");
    }
  }

}