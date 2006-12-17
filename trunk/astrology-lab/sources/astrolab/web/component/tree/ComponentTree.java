package astrolab.web.component.tree;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Properties;

import astrolab.web.component.time.ComponentSelectTime;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentTree {

  private static String FILENAME = "astrolab/web/component/tree/component_tree.html";
  private static String RAW_CONTENTS = "";

  static {
    try {
      byte[] BYTES_CONTENTS;
      InputStream fis = ComponentSelectTime.class.getClassLoader().getResourceAsStream(FILENAME);
      int read;
      byte[] data = new byte[500];
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      while (fis.available() > 0) {
        read = fis.read(data);
        baos.write(data, 0, read);
      }
      BYTES_CONTENTS = baos.toByteArray();
      fis.close();

      RAW_CONTENTS = new String(BYTES_CONTENTS);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void fill(LocalizedStringBuffer buffer, DisplayTree tree) {
    ComponentTree.fill(buffer, tree, tree.getRoot());
  }

  public static void fill(LocalizedStringBuffer buffer, DisplayTree tree, TreeObject selection) {
    Properties parameters = new Properties();
    parameters.put("{1}", String.valueOf(selection.getId()));
    parameters.put("{2}", selection.getText(false));
    parameters.put("{3}", "/tree.html?_d=" + tree.getId());
    parameters.put("{4}", tree.getChoiceId());
    buffer.append(populate(parameters));
  }

  public static int retrieve(Request request, DisplayTree tree) {
    return Integer.parseInt(request.get(tree.getChoiceId()));
  }

  private final static String populate(Properties parameters) {
    String result = RAW_CONTENTS;
    int param = 1;
    for (; true; param++) {
      String paramtext = "{" + param + "}";
      String value = (String) parameters.get(paramtext);

      if (value != null) {
        int index = result.indexOf(paramtext);
        if (index >= 0) {
          result = result.substring(0, index) + value + result.substring(index + paramtext.length());
        }
      } else {
        break;
      }
    }
    return result;
  }

}
