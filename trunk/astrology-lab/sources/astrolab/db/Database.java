package astrolab.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class Database {

  private static Connection connection;

  static {
    try {
      Class.forName("com.mysql.jdbc.Driver");
    } catch (Exception e) {
      System.err.println(" Unable to load MySQL driver:");
      System.err.println(e.toString());
    }
  }

  private static Statement createStatement() throws SQLException {
  	if (connection == null || connection.isClosed()) {
      try {
        connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/astrolab?useUnicode=true&connectionCollation=cp1251_bulgarian_ci&characterEncoding=cp1251", "develop", "develop");
      } catch (Exception e) {
        connection = DriverManager.getConnection("jdbc:mysql://astrology-lab.net:3306/astrolab?useUnicode=true&connectionCollation=cp1251_bulgarian_ci&characterEncoding=cp1251", "develop", "develop");
      }
  	}

    return connection.createStatement();
  }

  public static void execute(String sql) {
    System.out.println("[SQL execute] " + sql);

    try {
      createStatement().execute(sql);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static ResultSet executeQuery(String query) {
    System.out.println("[SQL execute query] " + query);

    try {
      return createStatement().executeQuery(query);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String query(String query) {
    System.out.println("[SQL query value] " + query);

    ResultSet set = null;
    try {
      set = createStatement().executeQuery(query);
      set.beforeFirst();
      if (set.next()) {
        return decode(set.getBytes(1));
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      closeSet(set);
    }
    return null;
  }

  public static String[] queryList(String query) {
    System.out.println("[SQL query values] " + query);

    ResultSet set = null;
    Vector vector = new Vector();
    try {
      set = createStatement().executeQuery(query);
      set.beforeFirst();
      while (set.next()) {
        vector.add(decode(set.getBytes(1)));
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      closeSet(set);
    }
    String[] result = new String[vector.size()];
    for (int i = 0; i < result.length; i++) {
      result[i] = (String) vector.get(i);
    }
    return result;
  }

  public static String[][] queryList(int columns, String query) {
    System.out.println("[SQL query values] " + query);

    ResultSet set = null;
    Vector vector = new Vector();
    try {
      set = createStatement().executeQuery(query);
      set.beforeFirst();
      while (set.next()) {
        String[] row = new String[columns];
        for (int i = 0; i < columns; i++) {
          row[i] = decode(set.getBytes(i + 1));
        }
        vector.add(row);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      closeSet(set);
    }
    String[][] result = new String[vector.size()][];
    for (int i = 0; i < result.length; i++) {
      result[i] = (String[]) vector.get(i);
    }
    return result;
  }

  private final static String decode(byte[] encoded) {
    try {
      return (encoded != null) ? new String(encoded, "windows-1251") : null;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new String(encoded);
  }

  private final static void closeSet(ResultSet set) {
    if (set == null) {
      return;
    }
    try {
      set.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}