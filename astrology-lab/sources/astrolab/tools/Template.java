package astrolab.tools;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Properties;

import astrolab.web.component.time.ComponentSelectTime;

public class Template {

  public static String template(String filename) {
    try {
      byte[] BYTES_CONTENTS;
      InputStream fis = ComponentSelectTime.class.getClassLoader().getResourceAsStream(filename);
      int read;
      byte[] data = new byte[500];
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      while (fis.available() > 0) {
        read = fis.read(data);
        baos.write(data, 0, read);
      }
      BYTES_CONTENTS = baos.toByteArray();
      fis.close();

      return new String(BYTES_CONTENTS);
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  public static String populate(String template, Properties parameters) {
    String result = template;
    int param = 0;
    for (; true; param++) {
      String paramtext = "{" + param + "}";
      String value = (String) parameters.get(paramtext);

      if (value != null) {
        while (true) {
          int index = result.indexOf(paramtext);
          if (index >= 0) {
            result = result.substring(0, index) + value + result.substring(index + paramtext.length());
          } else {
            break;
          }
        }
      } else {
        break;
      }
    }
    return result;
  }

}
