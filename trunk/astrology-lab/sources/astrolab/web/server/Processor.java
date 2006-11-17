package astrolab.web.server;

import java.util.HashMap;
import java.util.Properties;

import astrolab.db.Action;
import astrolab.web.Display;
import astrolab.web.Modify;
import astrolab.web.entrance.DisplayEntrance;
import astrolab.web.server.content.MenuPage;
import astrolab.web.server.content.StaticPage;

public class Processor {

  private Connection connection;

	private static HashMap staticRequests = new HashMap();

	Processor(Connection connection) {
    this.connection = connection;
  }

	Request process(String rawRequestText, Properties headers, int raw_user) {
    int user = raw_user;
    int paramIndex = rawRequestText.indexOf('?');
    Request request;

    headers.setProperty("GET", (paramIndex < 0) ? rawRequestText : rawRequestText.substring(0, paramIndex));

    if (rawRequestText.indexOf("menu") >= 0) {
      request = new MenuPage(connection, user, headers);
    } else if (staticRequests.containsKey((paramIndex < 0) ? rawRequestText : rawRequestText.substring(0, paramIndex))) {
      request = (Request) staticRequests.get((paramIndex < 0) ? rawRequestText : rawRequestText.substring(0, paramIndex));
      request = new StaticPage((StaticPage) request, connection, user, headers);
    } else {
      request = new Request(connection, user, headers);
    }

    if (paramIndex >= 0) {
      request.extractParameters(rawRequestText.substring(paramIndex + 1));
    }

    if (request.getRequestedDisplay() >= 0) {
      request.setDisplay(Display.getView(request.getRequestedDisplay()));
    } else {
      process(request, request.getAction(), request.getViewFrame());
    }

    connection.getOutput().respond(request);
    return request;
	}

  private void process(Request request, int arg_action, int viewFrame) {
    int from_view = Display.getCurrentView(request);
    int action = determineAction(arg_action, from_view);

    DisplayEntrance entrance = new DisplayEntrance();

    if ((arg_action < 0) && (entrance.isApplicable(request))) {
      request.setDisplay(entrance);
    } else if (action > 0) {
      String to_view = Action.getTarget(action, from_view);
      String injection = Action.getExecution(action, from_view);

      if (injection != null) {
        request.setModify(Modify.getView(Integer.parseInt(injection)));
      }

      if (to_view != null) {
        request.setDisplay(Display.getView(Integer.parseInt(to_view)));
        Display.setCurrentView(Integer.parseInt(to_view), request);
      } else {
        request.setDisplay(Display.getView(0));
        Display.setCurrentView(0, request);
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

  static {
    registerStaticPage("favicon.ico", "favicon.ico", "image/x-icon");
    registerStaticPage("", "frames.html", null);
    registerStaticPage("frame_control.html", "frame_control.html", null);
    registerStaticPage("play_on.gif", "play_on.gif", "image/gif");
    registerStaticPage("play_off.gif", "play_off.gif", "image/gif");
    registerStaticPage("pause.gif", "pause.gif", "image/gif");
  }

  private static void registerStaticPage(String key, String filename, String type) {
    staticRequests.put(key, new StaticPage(filename, type));
  }

  public static StaticPage getStaticPage(String key) {
    return (StaticPage) staticRequests.get(key);
  }
}