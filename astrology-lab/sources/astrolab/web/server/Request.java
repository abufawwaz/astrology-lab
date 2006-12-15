package astrolab.web.server;

import java.util.StringTokenizer;

import astrolab.web.Display;
import astrolab.web.Modify;
import astrolab.web.server.content.PanelResponse;

public class Request {

  private static ThreadLocal<Request> currentRequest = new ThreadLocal<Request>();

  private boolean hasDatabaseChange = false;

  private int user = -1;

  private Connection connection;
  private Constraints constraints = new Constraints();
  private Response response = new PanelResponse(this);
  private Display display;
  private Modify modify;
  private RequestParameters parameters;

  public Request(Connection connection, int user, RequestParameters parameters) {
    this.connection = connection;
    this.user = user;
    this.parameters = parameters;

    currentRequest.set(this);
  }

  public static Request getCurrentRequest() {
    return currentRequest.get(); 
  }

  public static boolean hasDatabaseChange() {
    Request request = getCurrentRequest();
    return (request != null) ? request.hasDatabaseChange : false;
  }

  public static void markDatabaseChange(boolean flag) {
    Request request = getCurrentRequest();
    if (request != null) {
      request.hasDatabaseChange = flag;
    }
  }

  /*
   * @deprecated use getParameters().get(...) instead
   */
  public String get(String parameter) {
    return parameters.get(parameter);
  }

  public Connection getConnection() {
    return connection;
  }

  public int getUser() {
    return user;
  }

  public int getAction() {
    return Integer.parseInt(parameters.get("_a", "-1"));
  }

  public int getReferrerDisplay() {
    return Integer.parseInt(parameters.get("_rd", "-1"));
  }

  public int getRequestedDisplay() {
    return Integer.parseInt(parameters.get("_d", "-1"));
  }

  public int getRequestedModify() {
    return Integer.parseInt(parameters.get("_m", "-1"));
  }

  public int[] getSelection() {
    int[] selection;
    String parameterValue = parameters.get("_s");

    if (parameterValue != null && parameterValue.length() > 0) {
      int i = 0;
      int[] newsel = new int[countChars(parameterValue, ':') + 1];
      StringTokenizer seltoken = new StringTokenizer(parameterValue, ":");
      while (seltoken.hasMoreTokens()) {
        newsel[i++] = Integer.parseInt(seltoken.nextToken());
      }
      selection = newsel;
    } else {
      selection = new int[0];
    }

    return selection;
  }

  public int[] getPath() {
    int[] path;
    String parameterValue = parameters.get("_p");

    if (parameterValue != null && parameterValue.length() > 0) {
      int i = 0;
      int[] newsel = new int[countChars(parameterValue, ':') + 1];
      StringTokenizer seltoken = new StringTokenizer(parameterValue, ":");
      while (seltoken.hasMoreTokens()) {
        newsel[i++] = Integer.parseInt(seltoken.nextToken());
      }
      path = newsel;
    } else {
      path = new int[0];
    }

    return path;
  }

  public Display getDisplay() {
    return display;
  }

  public void setDisplay(Display display) {
    this.display = display;
  }

  public Modify getModify() {
    return modify;
  }

  public void setModify(Modify modify) {
    this.modify = modify;
  }

	public Constraints getConstraints() {
		return constraints;
	}

  public RequestParameters getParameters() {
    return parameters;
  }

  public Response getResponse() {
    return response;
  }

	/**
	 * @param selection
	 * @return true if the given selection is a subset of the selected objects
	 */
	public boolean isSelected(int[] selection) {
		if (selection == null || selection.length == 0) {
			return false;
		}
    int[] thisSelection = getSelection();
		for (int i = 0; i < selection.length; i++) {
			boolean found = false;
			for (int j = 0; j < thisSelection.length; j++) {
				if (selection[i] == thisSelection[j]) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}

  private final int countChars(String text, char c) {
    byte[] bytes = text.getBytes();
    byte ch = (byte) c;
    int result = 0;
    for (int i = 0; i < bytes.length; i++) {
      if (bytes[i] == ch) {
        result++;
      }
    }
    return result;
  }

}