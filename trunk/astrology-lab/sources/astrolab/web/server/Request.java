package astrolab.web.server;

import java.util.Properties;
import java.util.StringTokenizer;

import astrolab.web.Display;
import astrolab.web.Modify;
import astrolab.web.server.content.PanelResponse;

public class Request {

	public final static int KEY_ACTION = 1; // requested view
	public final static int KEY_SELECTION = 2; // user selection
  public final static int KEY_DISPLAY = 3; // requested display

  public final static String TEXT_ACTION = "_a";
  public final static String TEXT_DISPLAY = "_d";
  public final static String TEXT_SELECTION = "_s";
  public final static String TEXT_PATH = "_p";
  public final static String TEXT_NAME = "_name";
  public final static String TEXT_DATE = "_date";

  public final static String CHOICE_ACCURACY = "_accuracy";
  public final static String CHOICE_LATTITUDE = "_lattitude";
  public final static String CHOICE_LOCATION = "_location";
  public final static String CHOICE_LONGITUDE = "_longitude";
  public final static String CHOICE_SOURCE = "_source";
  public final static String CHOICE_TIME_ZONE = "_time_zone";

  private static ThreadLocal<Request> currentRequest = new ThreadLocal<Request>();

  private int user = -1;
  private int requestedDisplay = -1;
  private int action = -1;
  private int[] selection = new int[0];
  private int[] path = new int[0];

  private Connection connection;
  private Constraints constraints = new Constraints();
  private Response response = new PanelResponse(this);
  private Display display;
  private Modify modify;
  private Properties textParameters = new Properties();
  private Properties textHeaders;

  public Request(Connection connection, int user, Properties headers) {
    this.connection = connection;
    this.user = user;
    this.textHeaders = headers;

    currentRequest.set(this);
  }

  public static Request getCurrentRequest() {
    return currentRequest.get(); 
  }

  public String get(String parameter) {
    return textParameters.getProperty(parameter);
  }

  public String getHeader(String parameter) {
    return textHeaders.getProperty(parameter);
  }

  public Connection getConnection() {
    return connection;
  }

  public int getUser() {
    return user;
  }

  public int getAction() {
    return action;
  }

  public int getRequestedDisplay() {
    return requestedDisplay;
  }

  public int[] getSelection() {
    return selection;
  }

  public int[] getPath() {
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

	public Response getResponse() {
		return response;
	}

	public void set(String parameter, String value) {
		textParameters.setProperty(parameter, value);
	}

	/**
	 * @param selection
	 * @return true if the given selection is a subset of the selected objects
	 */
	public boolean isSelected(int[] selection) {
		if (selection == null || selection.length == 0) {
			return false;
		}
		for (int i = 0; i < selection.length; i++) {
			boolean found = false;
			for (int j = 0; j < this.selection.length; j++) {
				if (selection[i] == this.selection[j]) {
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

  public void extractParameters(String requestText) {
		StringTokenizer tokens = new StringTokenizer(requestText, "&");
		while (tokens.hasMoreTokens()) {
			String parameterText = tokens.nextToken();
			int equalsMark = parameterText.indexOf('=');
			String parameterKey = (equalsMark > 0) ? parameterText.substring(0, equalsMark) : parameterText;
			String parameterValue = (equalsMark > 0) ? parameterText.substring(equalsMark + 1) : null;
			
      if (TEXT_DISPLAY.equals(parameterKey)) {
        requestedDisplay = (parameterValue != null && parameterValue.length() > 0) ? Integer.parseInt(parameterValue) : 0;
      } else if (TEXT_ACTION.equals(parameterKey)) {
        action = (parameterValue != null && parameterValue.length() > 0) ? Integer.parseInt(parameterValue) : 0;
      } else if (TEXT_SELECTION.equals(parameterKey)) {
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
      } else if (TEXT_PATH.equals(parameterKey)) {
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
      } else {
				set(parameterKey, unescape(parameterValue));
			}
		}
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

  private final String unescape(String text) {
    byte[] bytes = text.getBytes();
    String result = "";

    for (int i = 0; i < bytes.length; i++) {
      if (bytes[i] == '+') {
        result += ' ';
      } else if (bytes[i] == '%') {
        int ch;
        String code = "" + (char) bytes[i + 1] + "" + (char) bytes[i + 2];
        if (code.startsWith("D0") && ((char) bytes[i + 3] == '%')) {
//          ch = Integer.parseInt(code + (char) bytes[i + 4] + "" + (char) bytes[i + 5], 16) - Integer.parseInt("D0B0", 16) + ((int) 'ï¿½'); 
//          ch = Integer.parseInt(code + (char) bytes[i + 4] + "" + (char) bytes[i + 5], 16) - Integer.parseInt("D0B0", 16) + ((int) 'Ð°'); // a
          ch = Integer.parseInt(code + (char) bytes[i + 4] + "" + (char) bytes[i + 5], 16) - Integer.parseInt("D0B0", 16) + ((int) 'à'); // a
System.err.println(" a: " + ((int) 'à'));
          i += 5;
        } else if (code.startsWith("D1") && ((char) bytes[i + 3] == '%')) {
//            ch = Integer.parseInt(code + (char) bytes[i + 4] + "" + (char) bytes[i + 5], 16) - Integer.parseInt("D180", 16) + ((int) 'ï¿½'); 
//          ch = Integer.parseInt(code + (char) bytes[i + 4] + "" + (char) bytes[i + 5], 16) - Integer.parseInt("D180", 16) + ((int) 'Ñ€'); // r
          ch = Integer.parseInt(code + (char) bytes[i + 4] + "" + (char) bytes[i + 5], 16) - Integer.parseInt("D180", 16) + ((int) 'ð'); // r
System.err.println(" a: " + ((int) 'ð'));
          i += 5;
        } else {
          ch = Integer.parseInt("" + (char) bytes[i + 1] + "" + (char) bytes[i + 2], 16);
          i += 2;
        }
        result += (char) ch;
      } else {
        result += (char) bytes[i];
      }
    }
    return result;
  }
}