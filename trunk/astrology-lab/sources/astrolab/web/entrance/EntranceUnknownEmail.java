package astrolab.web.entrance;

import astrolab.db.Personalize;
import astrolab.db.Text;
import astrolab.tools.SendMail;
import astrolab.web.HTMLDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

class EntranceUnknownEmail extends HTMLDisplay implements Entrance {

  public boolean isApplicable(Request request) {
    if ((request.getUser() <= 0) || (Personalize.getEmail() != null)) {
      return false;
    }
    
    return true;
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("You have not provided valid e-mail address! Until you do so you will not be able to use your profile from another browser or after you clear your browser's cookies."); //TODO: localize
    buffer.append("<br />");
    buffer.append("Enter your e-mail address here: <input type='text' name='email' />");
  }

  public void modify(Request request) {
    String email = request.get("email");
    if ((email != null) && (email.length() > 0) && sendEmail(request.getUser(), email)) {
      Personalize.setEmail(email);
    }
  }

  private final boolean sendEmail(int user, String to) {
    String content = "<html><body><center>Keep this e-mail and use it to enter <b><i>www.</i></b><b><i>astrology-lab</i></b><b><i>.net</i></b> with your profile!</center><br><br><center><form method='post' action='http://www.astrology-lab.net'><input type='hidden' name='session' value='" + user + "'><input type='submit' value='Enter www.astrology-lab.net'></form></center><br><hr><center>Do not forward or send this e-mail to anyone!</center></body></html>";
    String subject = "" + Text.getText(user) + ", Your registration at www.astrology-lab.net has been successful!";
    return SendMail.send(user, to, subject, content);
  }

}
