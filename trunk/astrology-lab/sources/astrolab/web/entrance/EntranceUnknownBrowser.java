package astrolab.web.entrance;

import astrolab.db.Text;
import astrolab.web.HTMLDisplay;
import astrolab.web.project.archive.natal.NatalRecord;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

class EntranceUnknownBrowser extends HTMLDisplay implements Entrance {

  public boolean isApplicable(Request request) {
    if (request.getUser() > 0) {
      return false;
    }

    String url = request.getHeader("GET");
    String referer = request.getHeader("Referer");
    String session = request.get("session");

    if ("".equals(url) || "frames.html".equals(url)) {
      if ((referer != null) && (referer.indexOf("mail") >= 0) && (session != null)) {
        request.getConnection().getOutput().setCookie("session", session);
        request.setUser(Integer.parseInt(session));
        return false;
      }
    }

    return true;
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("You are accessing www.astrology-lab.net from an unknown browser."); //TODO: localize
    buffer.append("<hr />");
    buffer.append("If you are registered, please, go to your mail inbox an use the entrance link.");
    buffer.append("<hr />");
    buffer.append("If you don't have an entrance link you either are not registered or you have not provided a valid e-mail.");
    buffer.append("<br />");
    buffer.append("Register by selecting here:");
  }

  public void modify(Request request) {
    long time = System.currentTimeMillis();
    int user = NatalRecord.store("User" + time, time, 0, NatalRecord.TYPE_MALE, NatalRecord.ACCURACY_SECOND, NatalRecord.SOURCE_ACCURATE, Text.ACCESSIBLE_BY_OWNER);
    request.getConnection().getOutput().setCookie("session", String.valueOf(user));
    request.setUser(user);
  }

}
