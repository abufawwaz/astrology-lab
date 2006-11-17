package astrolab.web.component.help;

import java.util.Date;

import astrolab.db.Event;
import astrolab.db.User;
import astrolab.tools.SendMail;
import astrolab.web.Modify;
import astrolab.web.server.Request;

public class ModifySendInvitation extends Modify {

	public void operate(Request request) {
    int receiverId = Event.getSelectedEvent().getSubjectId();
    Date lastInvitation = User.getInvitationTime(receiverId);

    if ((lastInvitation != null) && (lastInvitation.getTime() + FormSendInvitation.WEEK_SPAN > System.currentTimeMillis())) {
      // invitation already sent
      return;
    } else {
      String to = User.getEmail(receiverId);
      if (to == null) {
        to = request.get("_email_to");
      }

      String subject = request.get("_email_subject");
      if (subject == null) {
        // no subject
        return;
      }
      String content = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head><body><pre>";
      content += request.get("_email_text");
      content += "</pre><hr><center>Keep this e-mail and use it to enter <b><i>www.</i></b><b><i>astrology-lab</i></b><b><i>.net</i></b> with your profile!</center><br><br><center><form method='post' action='http://www.astrology-lab.net'><input type='hidden' name='session' value='" + receiverId + "'><input type='submit' value='Enter www.astrology-lab.net'></form></center><br><hr><center>Do not forward or send this e-mail to anyone!</center></body></html>";

      SendMail.send(receiverId, to, subject, content);
    }
	}

}