package astrolab.tools;

import java.util.Date;

import astrolab.db.Personalize;

public class Log {

  private static ThreadLocal<Boolean> mode = new ThreadLocal<Boolean>();

  public static void beSilent(boolean flag) {
    mode.set(flag);
  }

  public static void log(String message) {
    if ((mode.get() == null) || !mode.get()) {
      System.err.println(Thread.currentThread().getName() + " " + new Date().toString() + " " + Personalize.getUser(false) + ": " + message);
    }
  }

  public static void log(String message, Throwable exception) {
    if ((mode.get() == null) || !mode.get()) {
      System.err.println(Thread.currentThread().getName() + " " + new Date().toString() + " " + Personalize.getUser(false) + ": Exception: " + message);
      exception.printStackTrace();
    }
  }
}
