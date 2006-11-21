package astrolab.tools;

import java.util.Date;

import astrolab.db.Personalize;

public class Log {

  public static void log(String message) {
    System.err.println(Thread.currentThread().getName() + " " + new Date().toString() + " " + Personalize.getUser() + ": " + message);
  }

  public static void log(String message, Throwable exception) {
    System.err.println(Thread.currentThread().getName() + " " + new Date().toString() + " " + Personalize.getUser() + ": Exception: " + message);
    exception.printStackTrace();
  }
}
