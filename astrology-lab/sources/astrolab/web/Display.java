package astrolab.web;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;

import astrolab.db.Action;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

/**
 * The Display instances construct a page to display some data in the proper manner.
 */
public abstract class Display {

  private static Hashtable viewsIdToClass = new Hashtable();
  private static Hashtable viewsIdToExtension = new Hashtable();
	private static Hashtable viewsClassToId = new Hashtable();

  private Hashtable<String, String> actions = new Hashtable<String, String>();
  private Hashtable<String, String[]> actionsAvoid = new Hashtable<String, String[]>();

  public void addAction(String eventType, String requestParameter) {
    actions.put(eventType, requestParameter);
  }

  public void addAction(String eventType, String requestParameter, String[] avoidParameters) {
    actions.put(eventType, requestParameter);
    actionsAvoid.put(eventType, avoidParameters);
  }

  public abstract String getType();

  public abstract byte[] getContent();

  public final static String getExtension(int id) {
    String extension = (String) viewsIdToExtension.get(new Integer(id));
    if (extension == null) {
      getView(id);
      extension = (String) viewsIdToExtension.get(new Integer(id));
    }
    return extension;
  }

  public final static int getId(Class classs) {
    String className = classs.getName();
    Integer id = (Integer) viewsClassToId.get(className);
    if (id == null) {
      id = new Integer(Action.getViewId(className));
      remember(id.intValue(), classs);
    }
    return id.intValue();
  }

  protected final void fillActionScript(Request request, LocalizedStringBuffer buffer, boolean inOwnScript) {
    String key;
    String parameter;
    Enumeration<String> keys = actions.keys();

    String get = request.get("URL");
    String url = (get.indexOf('?') >= 0) ? get + "&" : get + "?";
    url = url.replaceAll("&amp;", "&");

    if (keys.hasMoreElements()) {
      if (!inOwnScript) {
        buffer.newline();
        buffer.append("<script language='javascript'>"); buffer.newline();
        buffer.append("//<![CDATA["); buffer.newline();
      }

      buffer.newline();
      buffer.append("var listenerObject = null;");
      buffer.newline();
      buffer.append("var frameIndex = 0;");
      buffer.newline();
      buffer.append("for (; frameIndex < parent.frames.length; frameIndex++) {");
      buffer.append("  if (parent.frames[frameIndex].window == this.window) break;");
      buffer.append("}");
      buffer.newline();

      buffer.append("\r\nwindow.onunload = function() {");
      while (keys.hasMoreElements()) {
        key = keys.nextElement();
        buffer.append("top.unregisterListener('" + key + "', listenerObject);");
      }
      buffer.append(" }");

      keys = actions.keys();
      while (keys.hasMoreElements()) {
        key = keys.nextElement();
        parameter = actions.get(key);

        if (!parameter.startsWith("javascript:")) {
          String[] avoidParameters = actionsAvoid.get(key);
          String parameterURL = (avoidParameters != null) ? removeParameters(url, avoidParameters) : url;
          int parameterIndex = parameterURL.indexOf(parameter + "=");
          int parameterEnd = parameterURL.indexOf("&", parameterIndex);
          if (parameterIndex >= 0) {
            if (parameterEnd >= 0) {
              parameterURL = parameterURL.substring(0, parameterIndex) + url.substring(parameterEnd + 1);
            } else {
              parameterURL = parameterURL.substring(0, parameterIndex);
            }
          }
  
          buffer.append("\r\nlistenerObject = top.registerListener(this.window, '" + key + "', function(message, findex) {");
  
          if (this instanceof SVGDisplay) {
            buffer.append("top.frames[findex].location='" + parameterURL + parameter + "=' + message");
          } else {
            buffer.append("window.location='" + parameterURL + parameter + "=' + message"); // normal frame
          }
          buffer.append(" })");
        } else {
          buffer.append("\r\nlistenerObject = top.registerListener(this.window, '" + key + "', function(message) { " + parameter.substring("javascript:".length()) + " })");
        }
      }

      if (!inOwnScript) {
        buffer.append("\r\n//]]>");
        buffer.append("\r\n</script>");
      }
    }
  }

  private final static String removeParameters(String url, String[] parameters) {
    String result = url;
    for (String parameter: parameters) {
      int parameterIndex = result.indexOf(parameter + "=");
      int parameterEnd = result.indexOf("&", parameterIndex);
      if (parameterIndex >= 0) {
        if (parameterEnd >= 0) {
          result = result.substring(0, parameterIndex) + url.substring(parameterEnd + 1);
        } else {
          result = result.substring(0, parameterIndex);
        }
      }
    }
    return result;
  }

  public final static Display getView(int intid) {
		Integer id = new Integer(intid);
		String className = (String) viewsIdToClass.get(id);
		Display result = null;
    boolean toRemember = false;

    if (className == null) {
			className = Action.getViewClass(intid);
      toRemember = true;
		}

    try {
			Class classs = Class.forName(className);
			result = (Display) classs.newInstance();

      if (toRemember) {
        remember(intid, classs);
      }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

  private final static void remember(int id, Class classs) {
    String className = classs.getName();
    viewsIdToClass.put(id, className);
    viewsClassToId.put(className, id);

    String extension = "html";
    try {
      Method method_extension = classs.getMethod("getExtension", null);
      extension = (String) method_extension.invoke(null, (Object[]) null);
    } catch (Exception e) {
      e.printStackTrace();
    }
    viewsIdToExtension.put(id, extension);
  }
}