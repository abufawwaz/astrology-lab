package astrolab.db;

import java.util.Hashtable;

public class AttributedObject {

//	public final static int ATTRIBUTE_SUBJECT_ID = 10002001;  // TODO: create and get it from database
//	public final static int ATTRIBUTE_LOCATION = 10002002;  // TODO: create and get it from database
//	public final static int ATTRIBUTE_TIME = 10002003;  // TODO: create and get it from database
//	public final static int ATTRIBUTE_ANSWER = 10002004;  // TODO: create and get it from database
//	public final static int ATTRIBUTE_COLOR = 10002005;  // TODO: create and get it from database
//	public final static int ATTRIBUTE_X = 10002006;  // TODO: create and get it from database
//	public final static int ATTRIBUTE_Y = 10002007;  // TODO: create and get it from database
//  public final static int ATTRIBUTE_SEQUENCE = 10002008;  // TODO: create and get it from database
  public final static int ATTRIBUTE_LANGUAGE = 10002010;  // TODO: create and get it from database

	protected int id;
	private Hashtable cache = new Hashtable();

	protected AttributedObject(int id) {
		this.id = id;
	}

	public final int getId() {
		return id;
	}

	public final int getIntAttribute(int id) {
		return (int) getAttribute(id);
	}

	public final double getAttribute(int id) {
		Object cached = cache.get(new Integer(id));
		if (cached != null) {
			return ((Double) cached).doubleValue();
		}

		String data = Database.query("SELECT attribute_value from attribute WHERE object_id = " + this.id + " AND attribute_id = " + id);
		double value = (data != null) ? Double.parseDouble(data) : 0;
		cache.put(new Integer(id), new Double(value));
		return value;
	}

	public final void setAttribute(int id, double value) {
		if (getIntAttribute(id) != 0) {
  		Database.execute("UPDATE attribute SET attribute_value = " + value + " WHERE object_id = " + this.id + " AND attribute_id = " + id);
		} else {
  		Database.execute("INSERT INTO attribute VALUES (" + this.id + ", " + id + ", " + value + ")");
		}
		cache.put(new Integer(id), new Double(value));
	}

  public final void setAttribute(int id, String value) {
    this.setAttribute(id, Integer.parseInt(value));
  }

  public final void setData(int project, int[] data) {
    String table = "project_" + Text.getDescriptiveId(project);
    String sql = "INSERT INTO " + table + " VALUES (" + id;
    for (int i = 0; i < data.length; i++) {
      sql += ", " + data[i];
    }
    sql += ")";
    Database.execute(sql);
  }
}
