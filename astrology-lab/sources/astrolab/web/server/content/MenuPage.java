package astrolab.web.server.content;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import astrolab.db.Action;
import astrolab.db.Personalize;
import astrolab.db.Text;
import astrolab.web.Display;
import astrolab.web.server.Connection;
import astrolab.web.server.Request;
import astrolab.web.server.Response;

public class MenuPage extends Request implements Response {

	public MenuPage(Connection connection, int user, Properties headers) {
    super(connection, user, headers);
  }

  public Response getResponse() {
    return this;
  }

	public byte[] getBytes() {
    LocalizedStringBuffer buffer = new LocalizedStringBuffer();
    buffer.append("<html>");
    buffer.append("\r\n<head>");
    buffer.append("\r\n\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
    buffer.append("\r\n</head>");
    buffer.append("\r\n<body>");
    if (getUser() < 0) {
      buffer.append("\r\n");
      buffer.append(Text.getText("menu.not-logged-in"));
      buffer.append("\r\n<hr />");
    } else {
      String name = Text.getText(getUser()).trim();
      int index = name.indexOf(' ');
      if (index > 0) {
        name = name.substring(0, index);
      }
      buffer.append("\r\n");
      buffer.localize("Hello");
      buffer.append(", ");
      buffer.append(name);
      buffer.append("!\r\n<hr />");
    }

//    listActions(buffer, Action.getActions(-1, view, getSelection().length), "");
//    buffer.append("\r\n<hr>");
//
//    buffer.append("view: " + view);
//    buffer.append("<br>path: ");
//    for (int i = 0; i < getPath().length; i++) {
//      buffer.append(getPath()[i]);
//    }
//    buffer.append("<br>selection: ");
//    for (int i = 0; i < getSelection().length; i++) {
//      buffer.append(getSelection()[i]);
//    }

    String info = "www.astrology-lab.net";
    int lab = Personalize.getFavourite(-1, Text.getId("user.session.laboratory"), -1);
    if (lab > 0) {
      info += " / " + Text.getText(lab);
    }

    int project = Personalize.getFavourite(-1, Text.getId("user.session.project"), -1);
    if (project > 0) {
      info += " / " + Text.getText(project);
    }

    int event;
    int number = 1;
    do {
      event = Personalize.getFavourite(-1, Text.getId("user.session.event." + number), -1);

      if (event > 0) {
        info += (number == 1) ? " / " : " & ";
        info += Text.getText(event);
      }
      number++;
    } while (event > 0);
    buffer.append("<script language='JavaScript'>top.document.title = '" + info + "'</script>");

    listFolders(buffer, getPath());

    buffer.append("\r\n</body>");
    buffer.append("\r\n</html>");

    try {
      return buffer.toString().getBytes("UTF-8");
    } catch (UnsupportedEncodingException uee) {
      return buffer.toString().getBytes();
    }
	}

	public String getType() {
		return null;
	}

	public String toString() {
		return "Menu Page";
	}

  private void listActions(LocalizedStringBuffer buffer, int[][] actions, String tab) {
    for (int i = 0; i < actions.length; i++) {
      buffer.append("\r\n<br />");
      buffer.append(tab);
      buffer.append("<a href='#' onclick=\"javascript:top.addPane('', 'root.");
      buffer.append(Display.getExtension(actions[i][1]));
      buffer.append("?_a=");
      buffer.append(actions[i][0]);
      buffer.append("', true)\">");
      buffer.append(Text.getText(actions[i][0]));
      buffer.append("</a>");
    }
  }

  private void listFolders(LocalizedStringBuffer buffer, int[] path) {
    listFolders(buffer, path, 0, "", "");
  }

  private void listFolders(LocalizedStringBuffer buffer, int[] path, int offset, String pathtext, String tab) {
    int view = Display.getCurrentView(this);
    int folder = (offset == 0) ? -1 : path[offset - 1];
    int[] actions = Action.getFolders(folder);

    for (int i = 0; i < actions.length; i++) {
      String newpathtext = (pathtext.length() > 0) ? (pathtext + ":" + actions[i]) : ("" + actions[i]);
      buffer.append("\r\n<br />");
      buffer.append(tab);
      buffer.append("<a href='./menu.html?_p=");
      buffer.append(newpathtext);
      buffer.append("'><i>");
      buffer.append(Text.getText(actions[i]));
      buffer.append("</i></a>");
      if ((offset < path.length) && (actions[i] == path[offset])) {
        listFolders(buffer, path, offset + 1, newpathtext, tab + "&nbsp;");
        listActions(buffer, Action.getActions(actions[i], view, getSelection().length), tab + "&nbsp;");
      }
    }
  }

}
