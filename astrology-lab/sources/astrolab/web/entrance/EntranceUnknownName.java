package astrolab.web.entrance;

import astrolab.db.Text;
import astrolab.web.HTMLDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

class EntranceUnknownName extends HTMLDisplay implements Entrance {

  public boolean isApplicable(Request request) {
    System.out.println(" user: " + request.getUser());
    if (request.getUser() <= 0) {
      return false;
    } else {
      String userName = Text.getText(request.getUser());
      if ((userName != null) && !userName.startsWith("User")) {
        return false;
      }
    }
    
    return true;
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    buffer.append("You have not given your name! This name is needed for display purposes."); //TODO: localize
    buffer.append("<br />");
    buffer.append("Enter your name here: <input type='text' name='user_name' />");
  }

  public void modify(Request request) {
    String user_name = request.get("user_name");

    if ((user_name != null) && (user_name.length() > 0)) {
      Text.changeText(request.getUser(), user_name.trim());
    }
  }

}
