package astrolab.web.entrance;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import astrolab.web.HTMLDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

class EntranceCheckSVGViewer extends HTMLDisplay {

  private static String FILENAME = "astrolab/web/entrance/entrance_svg_viewer_check.html";
  private static String CONTENTS = "";

  static {
    try {
      byte[] BYTES_CONTENTS;
      InputStream fis = EntranceCheckSVGViewer.class.getClassLoader().getResourceAsStream(FILENAME);
      int read;
      byte[] data = new byte[500];
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      while (fis.available() > 0) {
        read = fis.read(data);
        baos.write(data, 0, read);
      }
      BYTES_CONTENTS = baos.toByteArray();
      fis.close();

      CONTENTS = new String(BYTES_CONTENTS);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    if (request.getParameters().get("User-Agent").contains("MSIE") && "no".equalsIgnoreCase(request.getParameters().getCookie("svg-viewer", "no"))) {
      buffer.append(CONTENTS);
    } else {
      buffer.append("yes");
    }
  }

}
