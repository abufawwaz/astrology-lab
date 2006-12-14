package astrolab.db;

import java.util.StringTokenizer;

import astrolab.web.project.archive.natal.NatalRecord;
import astrolab.web.server.Request;

public final class Personalize extends AttributedObject {

  public final static int VALUE_NULL = -1;
	public final static int KEY_ATTRIBUTE = 2;

  public final static String ACCEPTED_LANGUAGES = "en,bg";
  public final static String LANGUAGE_BG = "bg";
  public final static String LANGUAGE_EN = "en";

  private static ThreadLocal<Personalize> currentPersonalization = new ThreadLocal<Personalize>();

  private final int user;
  private String language = LANGUAGE_EN;

  private Personalize(int user) {
    super(user);
    this.user = user;
  }

  public String toString() {
    return user + " " + language;
  }

  public static void clear() {
    currentPersonalization.remove();
  }

  public static void set(int user, String language) {
    Personalize person = new Personalize(user);
    String lang = null;
    boolean language_specified = false;
    if (language != null) {
      StringTokenizer tokens = new StringTokenizer(language, ",;");
      while (tokens.hasMoreTokens()) {
        lang = tokens.nextToken();
        if (ACCEPTED_LANGUAGES.indexOf(lang) >= 0) {
          language_specified = true;
          break;
        }
      }
    }

    if (!language_specified) {
      lang = Text.getText((int) person.getAttribute(AttributedObject.ATTRIBUTE_LANGUAGE));
    }
    if (lang != null && ACCEPTED_LANGUAGES.indexOf(lang) >= 0) {
      person.language = lang;
    }
    currentPersonalization.set(person);
  }

  public static String getLanguage() {
    Personalize person = currentPersonalization.get();
    if (person != null && person.user > 0) {
      return person.language;
    } else {
      return LANGUAGE_EN;
    }
  }

  public static int getUser(boolean requireReal) {
    Personalize person = currentPersonalization.get();
    if (person != null && person.user > 0) {
      return person.user;
    } else if (requireReal) {
      long time = System.currentTimeMillis();
      int user = NatalRecord.store(-1, "User" + time, time, 0, NatalRecord.TYPE_MALE, NatalRecord.ACCURACY_SECOND, NatalRecord.SOURCE_ACCURATE, Text.ACCESSIBLE_BY_OWNER);
      Request.getCurrentRequest().getConnection().getOutput().setCookie("session", String.valueOf(user));
      return user;
    } else {
      return -1;
    }
  }

  public static int[] getFavourites(int int_view) {
    Personalize person = currentPersonalization.get();
    if (person != null && person.user > 0) {
      int user = person.user;
    	String view = (int_view >= 0) ? "= " + int_view : "IS NULL";
      String[] list = Database.queryList("SELECT object_id from favourites where user_id = " + user + " and view_id " + view + " ORDER BY order_at");
      int[] result = new int[list.length];
      for (int i = 0; i < list.length; i++) {
        result[i] = Integer.parseInt(list[i]);
      }
      return result;
    } else {
      return new int[0];
    }
  }

  public static int getFavourite(int int_view, int order, int default_value) {
    Personalize person = currentPersonalization.get();
    if (person != null && person.user > 0) {
      int user = person.user;
    	String view = (int_view >= 0) ? "= " + int_view : "IS NULL";
      String result = Database.query("SELECT object_id from favourites where user_id = " + user + " and view_id " + view + " and order_at = " + order);

      if (result != null) {
        int int_result = Integer.parseInt(result);
        return (int_result >= 0) ? int_result : default_value;
      }
    }
    return default_value;
  }

  public static void addFavourite(int int_view, int favour) {
    Personalize person = currentPersonalization.get();
    if (person == null || person.user < 0) {
      System.out.println("No personalization for anonymous user!");
      return;
    }
    int user = person.user;
    String view = (int_view >= 0) ? "= " + int_view : "IS NULL";

    if (user > 0) {
      String order = Database.query("SELECT * FROM favourites WHERE user_id = " + user + " and view_id " + view + " and object_id = " + favour);
      if (order == null) {
        order = String.valueOf(getFavourites(int_view).length + 1);
        if (favour != VALUE_NULL) {
          Database.execute("INSERT INTO favourites VALUES (" + user + ", " + ((int_view >= 0) ? String.valueOf(int_view) : "NULL") + ", " + favour + ", " + order + ", true)");
        }
      }
    }
  }

  public static void addFavourite(int int_view, int favour, int order) {
    Personalize person = currentPersonalization.get();
    if (person == null || person.user < 0) {
      System.out.println("No personalization for anonymous user!");
      return;
    }
    int user = person.user;
    String view = (int_view >= 0) ? "= " + int_view : "IS NULL";

    if (user > 0) {
      if (Database.query("SELECT * FROM favourites WHERE user_id = " + user + " and view_id " + view + " and order_at = " + order) != null) {
        if (favour != VALUE_NULL) {
          Database.execute("UPDATE favourites SET object_id = " + favour + " WHERE user_id = " + user + " and view_id " + view + " and order_at = " + order);
        } else {
          Database.execute("DELETE from favourites WHERE user_id = " + user + " and view_id " + view + " and order_at = " + order);
        }
      } else {
        if (favour != VALUE_NULL) {
          Database.execute("INSERT INTO favourites VALUES (" + user + ", " + ((int_view >= 0) ? String.valueOf(int_view) : "NULL") + ", " + favour + ", " + order + ", true)");
        }
      }
    }
  }

  public static String getEmail() {
    Personalize person = currentPersonalization.get();
    if ((person != null) && (person.user > 0)) {
      return User.getEmail(person.user);
    }
    return null;
  }

  public static void setEmail(String email) {
    Personalize person = currentPersonalization.get();
    if (person.user > 0) {
      User.setEmail(person.user, email);
    }
  }

}
