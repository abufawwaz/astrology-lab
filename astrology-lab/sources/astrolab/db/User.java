package astrolab.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class User {

  public static String getEmail(int user) {
    if (user > 0) {
      return Database.query("SELECT email from users where user_id = " + user);
    }
    return null;
  }

  public static Date getInvitationTime(int user) {
    if (user > 0) {
      ResultSet set = null;

      try {
        set = Database.executeQuery("SELECT invitation from users where user_id = " + user);

        return (set.last()) ? set.getTimestamp(1) : null;
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        if (set != null) {
          try {
            set.close();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return null;
  }

  public static void setEmail(int user, String email) {
    String sqlEmail = email.replace('\'', '_');
    if (user > 0) {
      if (Database.query("SELECT * FROM users WHERE user_id = " + user) != null) {
        Database.execute("UPDATE users set email = '" + sqlEmail + "' where user_id = " + user);
      } else {
        Database.execute("INSERT INTO users VALUES (" + user + ", 'en', '" + sqlEmail + "')");
      }
    }
  }

  public static void updateSentInvitation(int user, String email) {
    String sqlEmail = email.replace('\'', '_');
    if (Database.query("SELECT * FROM users WHERE user_id = " + user) != null) {
      Database.execute("UPDATE users set invitation = now() WHERE user_id = " + user);
      Database.execute("UPDATE users set email = '" + sqlEmail + "' WHERE user_id = " + user);
    } else {
      Database.execute("INSERT INTO users VALUES (" + user + ", 'en', '" + sqlEmail + "', now())");
    }
  }

}
