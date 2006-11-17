package astrolab.web;

import java.util.Hashtable;

import astrolab.db.Action;
import astrolab.web.server.Request;

/**
 * The Modify instances receive the inout from the user and modify the database records.
 */
public abstract class Modify {

  private static Hashtable viewsIdToClass = new Hashtable();
	private static Hashtable viewsClassToId = new Hashtable();

	public abstract void operate(Request request);

	public final static int getId(Class classs) {
		String className = classs.getName();
		Integer id = (Integer) viewsClassToId.get(className);
		if (id == null) {
			id = new Integer(Action.getViewId(className));
			viewsIdToClass.put(id, className);
			viewsClassToId.put(className, id);
		}
		return id.intValue();
	}

	public final static Modify getView(int intid) {
		Integer id = new Integer(intid);
		String className = (String) viewsIdToClass.get(id);
		Modify result = null;
		if (className == null) {
			className = Action.getViewClass(intid);
      viewsIdToClass.put(id, className);
			viewsClassToId.put(className, id);
		}
		try {
			System.out.println("modify: " + className);
			Class classs = Class.forName(className);
			result = (Modify) classs.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}