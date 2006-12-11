package astrolab.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import astrolab.tools.Log;
import astrolab.web.server.Request;

public class Database {

  private static Connection connection;

  static {
    try {
      Class.forName("com.mysql.jdbc.Driver");
    } catch (Exception e) {
      Log.log(" Unable to load MySQL driver:", e);
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
    Log.log("[SQL execute] " + sql);

    try {
      createStatement().execute(sql);
      Request.markDatabaseChange(true);
    } catch (Exception e) {
      Log.log(sql, e);
    }
  }

  public static ResultSet executeQuery(String query) {
    Log.log("[SQL execute query] " + query);

    try {
      return createStatement().executeQuery(query);
    } catch (Exception e) {
      Log.log(query, e);
    }
    return null;
  }

  public static String query(String query) {
    Log.log("[SQL query value] " + query);

    ResultSet set = null;
    try {
      set = createStatement().executeQuery(query);
      set.beforeFirst();
      if (set.next()) {
        return decode(set.getBytes(1));
      }
    } catch (Exception e) {
      Log.log(query, e);
    } finally {
      closeSet(set);
    }
    return null;
  }

  public static int[] queryIds(String query) {
    Log.log("[SQL query ids] " + query);

    ResultSet set = null;
    Vector<Integer> vector = new Vector<Integer>();
    try {
      set = createStatement().executeQuery(query);
      set.beforeFirst();
      while (set.next()) {
        vector.add(new Integer(set.getInt(1)));
      }
    } catch (Exception e) {
      Log.log(query, e);
    } finally {
      closeSet(set);
    }
    int[] result = new int[vector.size()];
    for (int i = 0; i < result.length; i++) {
      result[i] = vector.get(i).intValue();
    }
    return result;
  }

  public static String[] queryList(String query) {
    Log.log("[SQL query values] " + query);

    ResultSet set = null;
    Vector<String> vector = new Vector<String>();
    try {
      set = createStatement().executeQuery(query);
      set.beforeFirst();
      while (set.next()) {
        vector.add(decode(set.getBytes(1)));
      }
    } catch (Exception e) {
      Log.log(query, e);
    } finally {
      closeSet(set);
    }
    String[] result = new String[vector.size()];
    for (int i = 0; i < result.length; i++) {
      result[i] = vector.get(i);
    }
    return result;
  }

  public static String[][] queryList(int columns, String query) {
    Log.log("[SQL query values] " + query);

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
      Log.log(query, e);
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
      Log.log(new String(encoded), e);
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
      Log.log(null, e);
    }
  }

}