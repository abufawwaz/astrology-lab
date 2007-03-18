package astrolab.web.server;

import astrolab.db.Personalize;
import astrolab.db.TextAccessControl;
import astrolab.project.archive.NatalRecord;
import astrolab.tools.Log;

public class SessionManager {

  private final static String COOKIE_SESSION = "session";

  protected static int determineUser(Request request) {
    RequestParameters parameters = request.getParameters();
    int user = Integer.parseInt(parameters.getCookie("session", "0"));

    if (user > 0) {
      String language = parameters.get("Accept-Language");

      Personalize.set(user, (language != null) ? language : Personalize.LANGUAGE_EN);
    }

    return user;
  }

  // called only by Perspective
  public static void establishSession(Request request) {
    RequestParameters parameters = request.getParameters();

    int user = Integer.parseInt(parameters.getCookie("session", "0"));

    if (user <= 0) {
      String url = parameters.get("GET");
      String referer = parameters.get("Referer");
      String session = parameters.get(COOKIE_SESSION);
  
      if (url.length() == 0) {
        if ((referer != null) && (referer.indexOf("mail") >= 0) && (session != null)) {
          user = Integer.parseInt(session);
          Log.log("Referer '" + referer + "' trusted to authenticate user " + user + ".");
          request.getConnection().getOutput().setCookie(COOKIE_SESSION, session);
        }
      }
    }

    if (user <= 0) {
      long time = System.currentTimeMillis();
      user = NatalRecord.store(-1, "User" + time, time, 0, NatalRecord.TYPE_MALE, NatalRecord.ACCURACY_SECOND, NatalRecord.SOURCE_ACCURATE, TextAccessControl.ACCESSIBLE_BY_OWNER);
      Request.getCurrentRequest().getConnection().getOutput().setCookie("session", String.valueOf(user));
    }

    if (user > 0) {
      String language = parameters.get("Accept-Language");

      Personalize.set(user, (language != null) ? language : Personalize.LANGUAGE_EN);
    }
  }

}