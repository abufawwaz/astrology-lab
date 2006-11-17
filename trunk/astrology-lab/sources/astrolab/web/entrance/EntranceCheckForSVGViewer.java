package astrolab.web.entrance;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import astrolab.db.Text;
import astrolab.web.HTMLDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

class EntranceCheckForSVGViewer extends HTMLDisplay implements Entrance {

  private static String FILENAME = "astrolab/web/entrance/entrance_svg_viewer_check.html";
  private static String CONTENTS = "";

  static {
    try {
      byte[] BYTES_CONTENTS;
      InputStream fis = EntranceCheckForSVGViewer.class.getClassLoader().getResourceAsStream(FILENAME);
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

  public boolean isApplicable(Request request) {
    if (request.getHeader("User-Agent").contains("MSIE") && "no".equalsIgnoreCase(request.getConnection().getInput().getCookie("svg-viewer", "no"))) {
      return true;
    }
    
    return false;
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append(CONTENTS);
  }

  public void modify(Request request) {
    String user_name = request.get("user_name");

    if ((user_name != null) && (user_name.length() > 0)) {
      Text.changeText(request.getUser(), user_name.trim());
    }
  }

}
