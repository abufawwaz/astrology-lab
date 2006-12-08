package astrolab.db;

public class Action {

  public static int getAction(int from_view, int to_view, int injection) {
    String from_part = (from_view >= 0) ? "from_view = " + from_view : "TRUE";
    String to_part = (to_view >= 0) ? "to_view = " + to_view : "TRUE";
    String injection_part = (injection >= 0) ? "inject_sequence = " + injection : "TRUE";
  	String action = Database.query("SELECT id from actions where " + from_part + " and " + to_part + " and " + injection_part);
    return (action != null) ? Integer.parseInt(action) : -1;
  }

  public static int[][] getActions(int action_group, int view, int selections) {
    String view_part = (view >= 0) ? "(from_view = " + view + " OR from_view IS NULL)": "from_view IS NULL";
    String action_group_part = (action_group >= 0) ? "action_group = " + action_group : "action_group IS NULL";
    String selection_part = "(required_selection IS NULL OR (object_id > 0 AND view_id IS NULL AND user_id = " + Personalize.getUser() + " AND required_selection = order_at))";
    String[][] list = Database.queryList(2, "SELECT DISTINCT(id), to_view from actions, favourites where " + view_part + " and " + selection_part + " and " + action_group_part + " and " + getProjectPart());
    int[][] result = new int[list.length][2];
    for (int i = 0; i < list.length; i++) {
      result[i][0] = Integer.parseInt(list[i][0]);
      result[i][1] = Integer.parseInt(list[i][1]);
    }
    return result;
  }

  public static int[] getFolders(int selection) {
    String selection_part = (selection >= 0) ? "action_group = " + selection : "action_group IS NULL";
    String[] list = Database.queryList("SELECT id from action_group where " + selection_part);
    int[] result = new int[list.length];
    for (int i = 0; i < list.length; i++) {
      result[i] = Integer.parseInt(list[i]);
    }
    return result;
  }

  public static int getViewId(String className) {
    return new Integer(Database.query("SELECT view from views where request = '" + className + "'")).intValue();
  }

  public static String getViewClass(int view) {
    return Database.query("SELECT request from views where view = " + view);
  }

  public static String getViewType(int view) {
    return Database.query("SELECT type from views where view = " + view);
  }

  public static String getTarget(int action, int view) {
    return Database.query("SELECT to_view from actions where id = " + action + " and (from_view = " + view + " or from_view IS NULL) and " + getProjectPart() + " ORDER BY from_view DESC");
  }

  public static String getExecution(int action, int view) {
    return Database.query("SELECT inject_sequence from actions where id = " + action + " and (from_view = " + view + " or from_view IS NULL) and " + getProjectPart() + " ORDER BY from_view DESC");
  }

  private static String getProjectPart() {
    int project = Personalize.getFavourite(-1, Text.getId("user.session.project"), -1);
    return (project >= 0) ? "(project = " + project + " OR project IS NULL)": "project IS NULL";
  }

}