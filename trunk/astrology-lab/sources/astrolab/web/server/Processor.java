package astrolab.web.server;

import java.util.HashMap;

import astrolab.db.Action;
import astrolab.db.Personalize;
import astrolab.tools.Log;
import astrolab.web.Display;
import astrolab.web.Modify;
import astrolab.web.server.content.MenuPage;
import astrolab.web.server.content.StaticPage;

public class Processor {

  private Connection connection;

	private static HashMap<String,StaticPage> staticRequests = new HashMap<String,StaticPage>();

	Processor(Connection connection) {
    this.connection = connection;
  }

	Request process(RequestParameters parameters, int raw_user) {
    Request request;
    String requestAddress = parameters.get("GET");

    int user = determineUser(parameters, raw_user);

    if (requestAddress.indexOf("menu") >= 0) {
      request = new MenuPage(connection, user, parameters);
    } else if (staticRequests.containsKey(requestAddress)) {
      request = getStaticPage(requestAddress);
    } else {
      request = new Request(connection, user, parameters);
    }

    if (request.getRequestedDisplay() >= 0) {
      if (request.getRequestedModify() >= 0) {
        int modify = request.getRequestedModify();
        request.setModify(Modify.getView(modify));
      }
      int display = request.getRequestedDisplay();
      request.setDisplay(Display.getView(display));
    } else {
      process(request, request.getAction());
    }

    Request.markDatabaseChange(false);
    connection.getOutput().respond(request);
    return request;
	}

  private void process(Request request, int arg_action) {
    int from_view = request.getCurrentDisplay();
    int action = determineAction(arg_action, from_view);

    if (action > 0) {
      String to_view = Action.getTarget(action, from_view);
      String injection = Action.getExecution(action, from_view);

      if (injection != null) {
        request.setModify(Modify.getView(Integer.parseInt(injection)));
      }

      if (to_view != null) {
        request.setDisplay(Display.getView(Integer.parseInt(to_view)));
      } else {
        request.setDisplay(Display.getView(0));
      }
    }
  }

  private final int determineAction(int arg_action, int from_view) {
    int action = arg_action;
    if (action < 0) {
      action = Action.getAction(from_view, from_view, -1);
    }
    if (action < 0) {
      action = Action.getAction(-1, from_view, -1);
    }
    return action;
  }

  private final int determineUser(RequestParameters parameters, int raw_user) {
    int user = raw_user;
    String url = parameters.get("GET");
    String referer = parameters.get("Referer");
    String session = parameters.get("session");

    if (url.length() == 0) {
      if ((referer != null) && (referer.indexOf("mail") >= 0) && (session != null)) {
        user = Integer.parseInt(session);
        Log.log("Referer '" + referer + "' trusted to authenticate user " + user + ".");
        connection.getOutput().setCookie("session", session);
        Personalize.set(user, Personalize.LANGUAGE_EN);
      }
    }

    return raw_user;
  }

  static {
    registerStaticPage("favicon.ico", "favicon.ico", "image/x-icon");
    registerStaticPage("", "frames.html", null);
    registerStaticPage("/", "frames.html", null);
  }

  private static void registerStaticPage(String key, String filename, String type) {
    staticRequests.put(key, new StaticPage(filename, type));
  }

  public static StaticPage getStaticPage(String key) {
    return (StaticPage) staticRequests.get(key);
  }
}