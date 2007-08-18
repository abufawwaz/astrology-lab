package astrolab.web.server;

import java.util.Properties;
import java.util.StringTokenizer;

import astrolab.db.Personalize;
import astrolab.db.Text;

public class RequestParameters {

  public final static String PROJECT_ID = "user.session.project";
  public final static String TEXT_NAME = "_name";

  private Properties textParameters = new Properties();

  public RequestParameters(String requestText) {
    StringTokenizer tokens = new StringTokenizer(requestText, " ");
    tokens.nextToken(); // skip HTTP Method
    String requestURI = tokens.nextToken();
    int paramIndex = requestURI.indexOf('?');

    set("URL", requestURI);
    set("GET", (paramIndex < 0) ? requestURI : requestURI.substring(0, paramIndex));

    extractParameters((paramIndex < 0) ? "" : requestURI.substring(paramIndex + 1));
  }

  public String get(String parameter) {
    return textParameters.getProperty(parameter);
  }

  public String get(String parameter, String defaultValue) {
    return textParameters.getProperty(parameter, defaultValue);
  }

  public int getInt(String parameter) {
    try {
      return (int) Double.parseDouble(textParameters.getProperty(parameter));
    } catch (NullPointerException e) {
      return 0;
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  public double getDouble(String parameter) {
    try {
      return Double.parseDouble(textParameters.getProperty(parameter));
    } catch (NullPointerException e) {
      return 0;
    } catch (NumberFormatException e) {
      return 0;
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  public String getCookie(String name, String defaultValue) {
    String cookies = textParameters.getProperty("Cookie");
    if (cookies == null) {
      return defaultValue;
    }
    int index1 = cookies.indexOf(name);
    if (index1 < 0) {
      return defaultValue;
    }
    int index2 = cookies.indexOf('=', index1);
    int index3 = cookies.indexOf(';', index2);
    return (index3 > 0) ? cookies.substring(index2 + 1, index3).trim() : cookies.substring(index2 + 1).trim();
  }

  public void set(String parameter, String value) {
    textParameters.setProperty(parameter, value);

    //TODO: all favorites may be added here
    if ("user.session.project".equals(parameter)) {
      Personalize.addFavourite(-1, Integer.parseInt(value), Text.getId("user.session.project"));
    }
  }

  public void extractParameters(String text) {
    StringTokenizer tokens = new StringTokenizer(text, "&");
    while (tokens.hasMoreTokens()) {
      String parameterText = tokens.nextToken();
      int equalsMark = parameterText.indexOf('=');
      String parameterKey = (equalsMark > 0) ? parameterText.substring(0, equalsMark) : parameterText;
      String parameterValue = (equalsMark > 0) ? parameterText.substring(equalsMark + 1) : null;

      set(parameterKey, unescape(parameterValue));
    }
  }

  public String toString() {
    return textParameters.toString();
  }

  private final String unescape(String text) {
    byte[] bytes = (text != null) ? text.getBytes() : new byte[0];
    String result = "";

    for (int i = 0; i < bytes.length; i++) {
      if (bytes[i] == '+') {
        result += ' ';
      } else if (bytes[i] == '%') {
        int ch;
        String code = "" + (char) bytes[i + 1] + "" + (char) bytes[i + 2];
        if (code.startsWith("D0") && ((char) bytes[i + 3] == '%')) {
//          ch = Integer.parseInt(code + (char) bytes[i + 4] + "" + (char) bytes[i + 5], 16) - Integer.parseInt("D0B0", 16) + ((int) '�');
          ch = Integer.parseInt(code + (char) bytes[i + 4] + "" + (char) bytes[i + 5], 16) - Integer.parseInt("D0B0", 16) + 1072; // 'а' (a) home Linux
//          ch = Integer.parseInt(code + (char) bytes[i + 4] + "" + (char) bytes[i + 5], 16) - Integer.parseInt("D0B0", 16) + ((int) '�'); // a
          i += 5;
        } else if (code.startsWith("D1") && ((char) bytes[i + 3] == '%')) {
//            ch = Integer.parseInt(code + (char) bytes[i + 4] + "" + (char) bytes[i + 5], 16) - Integer.parseInt("D180", 16) + ((int) '�'); 
          ch = Integer.parseInt(code + (char) bytes[i + 4] + "" + (char) bytes[i + 5], 16) - Integer.parseInt("D180", 16) + 1088; // 'р' (r) home Linux
//          ch = Integer.parseInt(code + (char) bytes[i + 4] + "" + (char) bytes[i + 5], 16) - Integer.parseInt("D180", 16) + ((int) '�'); // r
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