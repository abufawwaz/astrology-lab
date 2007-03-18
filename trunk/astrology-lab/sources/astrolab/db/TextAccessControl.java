package astrolab.db;

public class TextAccessControl {

  public final static int ACCESSIBLE_BY_ALL = 0;
  public final static int ACCESSIBLE_BY_OWNER = -1;

  public static int getAccessibleBy(int id) {
    String data = Database.query("SELECT accessible_by from text where id = " + id);
    return (data != null) ? Integer.parseInt(data) : ACCESSIBLE_BY_ALL;
  }

  public final static String constructAccessibleByCaller() {
    int user = Personalize.getUser();

    if (user == 2000001) {
      // system user sees everything
      return "";
    } else {
      return " AND (accessible_by IS NULL OR accessible_by = " + user + ")";
    }
  }

  public final static String constructAccessibleBy(String id) {
    int user = Personalize.getUser();

    if (user == 2000001) {
      // system user sees everything
      return "";
    } else if (user == -1) {
      try {
        return " AND (accessible_by IS NULL OR accessible_by = " + Integer.parseInt(id) + ")";
      } catch (NumberFormatException nfe) {
        // no user. show only available to everybody
        return " AND (accessible_by IS NULL)";
      }
    } else if (id.equals(String.valueOf(user))) {
      // every user sees him/herself
      return "";
    } else {
      return " AND (accessible_by IS NULL OR accessible_by = " + user + ")";
    }
  }

}