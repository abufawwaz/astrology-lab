package astrolab.db;

public class Text {

  public final static int ACCESSIBLE_BY_ALL = 0;
  public final static int ACCESSIBLE_BY_OWNER = -1;

  private final static int TYPE_OFFSET = 1000000;
	private final static int TYPE_SUB_OFFSET = 1000;

	// large groups
	public final static int TYPE_VIEW = 1;
	public final static int TYPE_EVENT = TYPE_OFFSET * 2 + 1;
	public final static int TYPE_LABORATORY = TYPE_OFFSET * 3 + 1;
	public final static int TYPE_ACTION = TYPE_OFFSET * 4 + 1;
  public final static int TYPE_REGION = TYPE_OFFSET * 5 + 1;
  public final static int TYPE_LABELS = TYPE_OFFSET * 6 + 1;
  public final static int TYPE_HELP_FEEDBACK = TYPE_OFFSET * 7 + 1;
  public final static int TYPE_PRODUCTS = TYPE_OFFSET * 8 + 1;
  public final static int TYPE_RESERVED = TYPE_OFFSET * 9 + 1;

	// small groups
	public final static int TYPE_VARIOUS = TYPE_OFFSET * 10 + 1;
	public final static int TYPE_EVENT_TYPE = TYPE_SUB_OFFSET * 1 + TYPE_VARIOUS;
  public final static int TYPE_EVENT_ATTRIBUTE_TYPE = TYPE_SUB_OFFSET * 2 + TYPE_VARIOUS;
  public final static int TYPE_TIME_ZONE = TYPE_SUB_OFFSET * 3 + TYPE_VARIOUS;

  public static String escape(String text) {
    return text.replace('\'', '^').replace('\"', '^').replace('<', '(').replace('>', ')');
  }

  public synchronized static int reserve(String raw_text, int type) {
    return reserve(raw_text, "NULL", type, 0);
  }

  public synchronized static int reserve(String raw_text, String descrid, int type) {
    return reserve(raw_text, descrid, type, 0);
  }

  public synchronized static int reserve(String raw_text, String descrid, int type, int user) {
    String text = escape(raw_text);
    String language = Personalize.getLanguage();
    String existing = Database.query("SELECT id FROM text WHERE " + language + " = '" + text + "'");
    if (existing != null) {
      int id = Integer.parseInt(existing);
      String accessible = Database.query("SELECT accessible_by FROM text WHERE id = " + id);
      String expected = String.valueOf(user);
      if (user == ACCESSIBLE_BY_OWNER) {
        expected = String.valueOf(Personalize.getUser());
      } else if (user == ACCESSIBLE_BY_ALL) {
        expected = "NULL";
      }
      if (!String.valueOf(user).equalsIgnoreCase("" + accessible)) {
        Database.execute("UPDATE text SET accessible_by = " + expected + " WHERE id = " + id);
      }
      return Integer.parseInt(existing);
    } else {
      int limit = (type < TYPE_VARIOUS) ? type + TYPE_OFFSET : type + TYPE_SUB_OFFSET;
      int id = Integer.parseInt(Database.query("SELECT max(id) FROM text WHERE id < " + limit)) + 1;
      if (id < type) {
        id = type;
      }

      String accessible_by;
      if (user == ACCESSIBLE_BY_ALL) {
        accessible_by = "NULL";
      } else if (user == ACCESSIBLE_BY_OWNER) {
        int current_user = Personalize.getUser();
        accessible_by = String.valueOf((current_user > 0) ? current_user : id);
      } else {
        accessible_by = String.valueOf(user);
      }

      if (Personalize.LANGUAGE_EN.equals(language)) {
        Database.execute("INSERT INTO text VALUES (" + id + ", " + accessible_by + ", '" + descrid + "', '" + text + "', NULL)");
      } else if (Personalize.LANGUAGE_BG.equals(language)) {
        Database.execute("INSERT INTO text VALUES (" + id + ", " + accessible_by + ", '" + descrid + "', NULL, '" + text + "')");
      } else {
        Database.execute("INSERT INTO text VALUES (" + id + ", " + accessible_by + ", '" + text + "', NULL, NULL)");
      }
      return id;
    }
  }

  public static int getAccessibleBy(int id) {
    String data = Database.query("SELECT accessible_by from text where id = " + id);
    return (data != null) ? Integer.parseInt(data) : ACCESSIBLE_BY_ALL;
  }

  public static String getText(int id) {
    String language = Personalize.getLanguage();
    String accessible_by = constructAccessibleBy(String.valueOf(id));
    String value = Database.query("SELECT " + language + " from text where id = " + id + accessible_by);
    if (value == null && !"en".equals(language)) {
      value = Database.query("SELECT en from text where id = " + id + accessible_by);
    }
    if (value == null && !"bg".equals(language)) {
      value = Database.query("SELECT bg from text where id = " + id + accessible_by);
    }
    if (value == null) {
      value = Database.query("SELECT descrid from text where id = " + id + accessible_by);
    }
    return value;
  }

  public static String getText(String id) {
    String language = Personalize.getLanguage();
    String accessible_by = constructAccessibleBy(id);
    String value = Database.query("SELECT " + language + " from text where (descrid = '" + id + "' or en = '" + id + "')" + accessible_by);
    if (value == null && !"en".equals(language)) {
      value = Database.query("SELECT en from text where descrid = '" + id + "'" + accessible_by);
    }
    return (value != null) ? value : id;
  }

  private final static String constructAccessibleBy(String id) {
    int user = Personalize.getUser();

    if (user == 2000001) {
      // system user sees everything
      return "";
    } else if (id.equals(String.valueOf(user))) {
      // every user sees him/herself
      return "";
    } else {
      if (user > 0) {
        return " AND (accessible_by IS NULL OR accessible_by = " + user + ")";
      } else {
        try {
          return " AND (accessible_by IS NULL OR accessible_by = " + Integer.parseInt(id) + ")";
        } catch (NumberFormatException nfe) {
          // no user. show only available to everybody
          return " AND (accessible_by IS NULL)";
        }
      }
    }
  }

  public static int getId(String id) {
    String language = Personalize.getLanguage();
    String int_id = Database.query("SELECT id from text where descrid = '" + id + "' or " + language + " = '" + id + "'");
    return (int_id != null) ? Integer.parseInt(int_id) : -1;
  }

  public static String getDescriptiveId(int id) {
    return Database.query("SELECT descrid from text where id = " + id);
  }

  public static String getDescriptiveId(String text) {
    String language = Personalize.getLanguage();
    String value = Database.query("SELECT descrid from text where " + language + " = '" + text + "'");
    if (value == null && !"en".equals(language)) {
      value = Database.query("SELECT descrid from text where en = '" + text + "'");
    }
    if (value == null && !"bg".equals(language)) {
      value = Database.query("SELECT descrid from text where bg = '" + text + "'");
    }
    return (value != null) ? value : text;
  }

  public static void changeText(int id, String text) {
    String language = Personalize.getLanguage();
    Database.execute("UPDATE text set " + language + " = '" + text.replace('\'', '_') + "' where id = " + id);
  }

}