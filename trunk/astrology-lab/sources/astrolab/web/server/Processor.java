package astrolab.web.server;

import java.util.HashMap;

import astrolab.db.Action;
import astrolab.web.Display;
import astrolab.web.Modify;
import astrolab.web.perspective.Perspective;
import astrolab.web.server.content.StaticPage;

public class Processor {

  private Connection connection;

	private static HashMap<String,StaticPage> staticRequests = new HashMap<String,StaticPage>();

	Processor(Connection connection) {
    this.connection = connection;
  }

	Request process(RequestParameters parameters) {
    Request request;
    String requestAddress = parameters.get("GET");

    if (staticRequests.containsKey(requestAddress)) {
      request = getStaticPage(requestAddress);
    } else {
      request = new Request(connection, parameters);

      if (Perspective.isPerspectiveRequest()) {
        request.setDisplay(new Perspective());
      } else {
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
      }
    }

    connection.getOutput().respond(request);
    return request;
	}

  private void process(Request request, int arg_action) {
    int from_view = request.getReferrerDisplay();
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

  static {
    registerStaticPage("favicon.ico", "favicon.ico", "image/x-icon");
    //registerStaticPage("/main.js", "main.js", "text/javascript");
    //registerStaticPage("/events.js", "events.js", "text/javascript");
    //registerStaticPage("/control.js", "control.js", "text/javascript");
    //registerStaticPage("/window.js", "window.js", "text/javascript");
    registerStaticPage("/classmates.html", "classmates.html", "text/html");
    registerStaticPage("/hands_left.jpg", "hands_left.jpg", "image/jpeg");
    registerStaticPage("/hands_top.jpg", "hands_top.jpg", "image/jpeg");
    registerStaticPage("/hands_right.jpg", "hands_right.jpg", "image/jpeg");
    registerStaticPage("/hands_bottom.jpg", "hands_bottom.jpg", "image/jpeg");
    for (int i = 1; i <= 22; i++) {
      registerStaticPage("/classmate" + i + ".jpg", "classmate" + i + ".jpg", "image/jpeg");
    }
  }

  private static void registerStaticPage(String key, String filename, String type) {
    staticRequests.put(key, new StaticPage(filename, type));
  }

  public static StaticPage getStaticPage(String key) {
    return (StaticPage) staticRequests.get(key);
  }
}