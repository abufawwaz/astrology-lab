package astrolab.web.server.content;

import astrolab.db.Action;
import astrolab.db.Text;
import astrolab.web.Display;
import astrolab.web.HTMLDisplay;
import astrolab.web.perspective.Perspective;
import astrolab.web.server.Request;

public class MenuPage extends HTMLDisplay {

  public MenuPage() {
    super("Perspectives");
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    int view = request.getReferrerDisplay();
    int[] actions = Action.getFolders(-1);

    listPerspectives(buffer);

    // TODO: remove this
//    buffer.append("<hr />");
//    buffer.append("<hr />");
//    buffer.append("<hr />");
//    buffer.append("<hr />");
//    buffer.append("<hr />");
//
//    for (int i = 0; i < actions.length; i++) {
//      buffer.append("\r\n<br />");
//      buffer.append("<b>");
//      buffer.append(Text.getText(actions[i]));
//      buffer.append("</b>");
//
//      listActions(buffer, Action.getActions(actions[i], view));
//    }
  }

  private void listPerspectives(LocalizedStringBuffer buffer) {
    int[] ids = Perspective.listPerspectives();

    for (int i = 0; i < ids.length; i++) {
      buffer.append("<a href='' onclick=\"javascript:top.switchPerspective('");
      buffer.append(ids[i]);
      buffer.append("')\">");
      buffer.localize(ids[i]);
      buffer.append("</a>");
      buffer.append("\r\n<br />");
    }
//    buffer.append("<hr />");
  }

//  private void listActions(LocalizedStringBuffer buffer, int[][] actions) {
//    for (int i = 0; i < actions.length; i++) {
//      buffer.append("\r\n<br />");
//      buffer.append("<a href='#' onclick=\"javascript:top.addPane('', 'root.");
//      buffer.append(Display.getExtension(actions[i][1]));
//      buffer.append("?_a=");
//      buffer.append(actions[i][0]);
//      buffer.append("', true)\">");
//      buffer.append(Text.getText(actions[i][0]));
//      buffer.append("</a>");
//    }
//  }

}
