package astrolab;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public final class StaticPage {

  private static HashMap<String, byte[]> STATIC_PAGES = new HashMap<String, byte[]>();
  private static HashMap<String, String> CONTENT_TYPE = new HashMap<String, String>();

  static {
    CONTENT_TYPE.put("html", "text/html");
    CONTENT_TYPE.put("js", "text/javascript");
    CONTENT_TYPE.put("ico", "image/icon");
  }

  static byte[] getStaticPage(String page) {
    byte[] result = STATIC_PAGES.get(page);

    if (result == null) {
      result = read(page);
      STATIC_PAGES.put(page, result);
    }

    return result;
  }

  static String getContentType(String page) {
    String extension = page.substring(page.indexOf('.') + 1);
    String result = CONTENT_TYPE.get(extension);
    return (result != null) ? result : "text/html";
  }

  private static byte[] read(String filename) {
    FileInputStream fis = null;

    try {
      System.err.println("static page: " + new File(filename).getAbsolutePath());
      fis = new FileInputStream(new File(filename));
      int read;
      byte[] data = new byte[500];
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      while (fis.available() > 0) {
        read = fis.read(data);
        baos.write(data, 0, read);
      }

      return baos.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();

      return null;
    } finally {
      if (fis != null) {
        try {
          fis.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

}