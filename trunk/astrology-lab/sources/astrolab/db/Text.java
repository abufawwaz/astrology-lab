package astrolab.db;

public class Text {

  public final static int TYPE_TIME_ZONE = 1000001;
  public final static int TYPE_REGION = 1001001;
  public final static int TYPE_EVENT = 2000001;
  public final static int TYPE_LIMIT = 5000000;

  private final static int getLimit(int type) {
    switch (type) {
      case TYPE_TIME_ZONE: {
        return TYPE_REGION -1;
      }
      case TYPE_REGION: {
        return TYPE_EVENT -1;
      }
      case TYPE_EVENT: {
        return TYPE_LIMIT;
      }
    }
    throw new IllegalStateException(" Unknown text type: " + type);
  }

  public static String escape(String text) {
    if (text == null) {
      return "NULL";
    } else {
      return "'" + text.trim().replace('\'', '^').replace('\"', '^').replace('<', '(').replace('>', ')') + "'";
    }
  }

  public synchronized static int reserve(String raw_text, int type) {
    return reserve(raw_text, null, type, 0);
  }

  public synchronized static int reserve(String raw_text, String descrid, int type) {
    return reserve(raw_text, descrid, type, 0);
  }

  public synchronized static int reserve(String raw_text, String raw_descrid, int type, int user) {
    String descrid = escape(raw_descrid);
    String text = escape(raw_text);
    String language = Personalize.getLanguage();
    String existing = Database.query("SELECT id FROM text WHERE " + language + " = " + text);
    if (existing != null) {
      int id = Integer.parseInt(existing);
      String accessible = Database.query("SELECT accessible_by FROM text WHERE id = " + id);
      String expected = String.valueOf(user);
      if (user == TextAccessControl.ACCESSIBLE_BY_OWNER) {
        expected = String.valueOf(Personalize.getUser());
      } else if (user == TextAccessControl.ACCESSIBLE_BY_ALL) {
        expected = "NULL";
      }
      if (!String.valueOf(user).equalsIgnoreCase("" + accessible)) {
        Database.execute("UPDATE text SET accessible_by = " + expected + " WHERE id = " + id);
      }
      return Integer.parseInt(existing);
    } else {
      int limit = getLimit(type);
      int id = Integer.parseInt(Database.query("SELECT max(id) FROM text WHERE id < " + limit)) + 1;
      if (id < type) {
        id = type;
      }

      String accessible_by;
      if (user == TextAccessControl.ACCESSIBLE_BY_ALL) {
        accessible_by = "NULL";
      } else if (user == TextAccessControl.ACCESSIBLE_BY_OWNER) {
        int current_user = Personalize.getUser();
        accessible_by = String.valueOf((current_user > 0) ? current_user : id);
      } else {
        accessible_by = String.valueOf(user);
      }

      if (Personalize.LANGUAGE_BG.equals(language)) {
        Database.execute("INSERT INTO text VALUES (" + id + ", " + accessible_by + ", " + descrid + ", NULL, " + text + ")");
      } else {
        Database.execute("INSERT INTO text VALUES (" + id + ", " + accessible_by + ", " + descrid + ", " + text + ", NULL)");
      }
      return id;
    }
  }

  public static String getText(int id) {
    String language = Personalize.getLanguage();
    String accessible_by = TextAccessControl.constructAccessibleBy(String.valueOf(id));
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

  public static String getCenteredSVGText(int id) {
    String accessible_by = TextAccessControl.constructAccessibleBy(String.valueOf(id));
    String value = Database.query("SELECT svg.svg from svg, text where text.id=svg.id AND svg.id = " + id + accessible_by);
    return (value != null) ? value : "<svg:text font-size='100'>" + getText(id) + "</svg:text>";
  }

  public static String getSVGText(int id) {
    String accessible_by = TextAccessControl.constructAccessibleBy(String.valueOf(id));
    String value = Database.query("SELECT svg.svg from svg, text where text.id=svg.id AND svg.id = " + id + accessible_by);
    return (value != null) ? "<svg:svg width='20' height='20' viewBox='-120 -120 240 240'>" + value + "</svg:svg>" : getText(id);
  }

  public static String getSVGText(String id, int fontSize) {
    String accessible_by = TextAccessControl.constructAccessibleBy(id);
    String value = Database.query("SELECT svg.svg from svg, text where text.id=svg.id AND (text.descrid = '" + id + "' or text.en = '" + id + "')" + accessible_by);
    return (value != null) ? "<svg:svg width='" + fontSize + "' height='" + fontSize + "' viewBox='-120 -120 240 240'>" + value + "</svg:svg>" : getText(id);
  }

  public static String getText(String id) {
    String language = Personalize.getLanguage();
    String accessible_by = TextAccessControl.constructAccessibleBy(id);
    String value = Database.query("SELECT " + language + " from text where (descrid = '" + id + "' or en = '" + id + "')" + accessible_by);
    if (value == null && !"en".equals(language)) {
      value = Database.query("SELECT en from text where descrid = '" + id + "'" + accessible_by);
    }
    return (value != null) ? value : id;
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

  public static void changeText(int id, String text, int user) {
    String language = Personalize.getLanguage();
    Database.execute("UPDATE text set " + language + " = '" + text.replace('\'', '_') + "' where id = " + id);

    String expected = String.valueOf(user);
    if (user == TextAccessControl.ACCESSIBLE_BY_OWNER) {
      expected = String.valueOf(Personalize.getUser());
    } else if (user == TextAccessControl.ACCESSIBLE_BY_ALL) {
      expected = "NULL";
    }
    Database.execute("UPDATE text SET accessible_by = " + expected + " WHERE id = " + id);
  }

}