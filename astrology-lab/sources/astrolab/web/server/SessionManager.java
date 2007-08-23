package astrolab.web.server;

import astrolab.db.Personalize;
import astrolab.db.Text;
import astrolab.db.TextAccessControl;
import astrolab.project.archive.NatalRecord;
import astrolab.tools.Log;

public class SessionManager {

  private final static String COOKIE_SESSION = "session";

  protected static int determineUser(Request request) {
    RequestParameters parameters = request.getParameters();
    int user = Integer.parseInt(parameters.getCookie("session", "0"));

    // web crawlers do not accept cookies so you have to identify them
    if (user <= 0) {
      String botName = getCrawlerName(parameters);

      if (botName != null) {
        user = Text.getId(botName);
      }
    }

    if (user > 0) {
      String language = parameters.get("Accept-Language");

      Personalize.set(user, (language != null) ? language : Personalize.LANGUAGE_EN);
    }

    return user;
  }

  // called only by Perspective
  public static void establishSession(Request request) {
    RequestParameters parameters = request.getParameters();

    // test for a regular user
    int user = Integer.parseInt(parameters.getCookie("session", "0"));

    if (!NatalRecord.exists(user)) {
      user = -1;
    }

    // test for access from invitation mail
    if (user <= 0) {
      String url = parameters.get("GET");
      String referer = parameters.get("Referer");
      String session = parameters.get(COOKIE_SESSION);
  
      if (url.length() == 0) {
        if ((referer != null) && (referer.indexOf("mail") >= 0) && (session != null)) {
          user = Integer.parseInt(session);
          Log.log("Referer '" + referer + "' trusted to authenticate user " + user + ".");
          request.getConnection().getOutput().setCookie(COOKIE_SESSION, session, true);
        }
      }
    }

    // test for robot web crawler
    if (user <= 0) {
      String botName = getCrawlerName(parameters);

      if (botName != null) {
        user = Text.getId(botName);

        if (!NatalRecord.exists(user)) {
          user = NatalRecord.store(-1, botName, System.currentTimeMillis(), 0, NatalRecord.TYPE_MALE, NatalRecord.ACCURACY_SECOND, NatalRecord.SOURCE_ACCURATE, TextAccessControl.ACCESSIBLE_BY_OWNER);
        }

        Log.log("A visit from crawler '" + botName + "'.");
      }
    }

    // unknown user. Create account and set cookie
    if (user <= 0) {
      long time = System.currentTimeMillis();
      user = NatalRecord.store(-1, "User" + time, time, 0, NatalRecord.TYPE_MALE, NatalRecord.ACCURACY_SECOND, NatalRecord.SOURCE_ACCURATE, TextAccessControl.ACCESSIBLE_BY_OWNER);
      Request.getCurrentRequest().getConnection().getOutput().setCookie("session", String.valueOf(user), true);
    }

    if (user > 0) {
      String language = parameters.get("Accept-Language");

      Personalize.set(user, (language != null) ? language : Personalize.LANGUAGE_EN);
    }
  }

  private final static String getCrawlerName(RequestParameters parameters) {
    String botName = null;

    try {
      String agent = parameters.get("User-Agent", "");
      int index = agent.indexOf("http://");
  
      if (index > 0) {
        index += "http://".length();
  
        int nameEnd = agent.indexOf('/', index);
        botName = (nameEnd > 0) ? agent.substring(index, nameEnd) : agent.substring(index);
  
        index = botName.lastIndexOf('.');
        if (index > 0) {
          index = botName.lastIndexOf('.', index - 1);
        }
        if (index > 0) {
          botName = botName.substring(index + 1);
        }
      }
    } catch (Exception e) {
      Log.log("Unable to extract the name of the web crawler", e);
    }

    return botName;
  }
}