package astrolab.web.display;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import astrolab.web.Display;

public abstract class JPGDisplay extends Display {

  protected JPGDisplay() {}

  protected abstract InputStream getFileStream();

  public byte[] getContent() {
    InputStream stream = null;

    try {
      int read;
      byte[] data = new byte[500];
      stream = getFileStream();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      while (stream.available() > 0) {
        read = stream.read(data);
        baos.write(data, 0, read);
      }

      return baos.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
      return new byte[0];
    } finally {
      if (stream != null) {
        try {
          stream.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  public final static String getExtension() {
    return "jpg";
  }

  public String getType() {
    return "image/jpg";
  }

}