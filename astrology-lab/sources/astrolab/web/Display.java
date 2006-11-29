package astrolab.web;

import java.lang.reflect.Method;
import java.util.Hashtable;

import astrolab.db.Action;
import astrolab.db.Personalize;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

/**
 * The Display instances construct a page to display some data in the proper manner.
 */
public abstract class Display {

  private static Hashtable viewsIdToClass = new Hashtable();
  private static Hashtable viewsIdToExtension = new Hashtable();
	private static Hashtable viewsClassToId = new Hashtable();

  public abstract String getType();

  public abstract void fillContent(Request request, LocalizedStringBuffer buffer);

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

  public static int getCurrentView(Request request) {
		return Personalize.getFavourite(-1, Personalize.KEY_VIEW_1, 0);
  }
  public static void setCurrentView(int view, Request request) {
    Personalize.addFavourite(-1, view, Personalize.KEY_VIEW_1);
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