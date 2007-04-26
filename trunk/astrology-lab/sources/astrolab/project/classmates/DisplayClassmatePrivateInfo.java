package astrolab.project.classmates;

import astrolab.db.Database;
import astrolab.web.Display;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.component.ComponentSelectNumber;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayClassmatePrivateInfo extends HTMLFormDisplay {

  private final static int ID = Display.getId(DisplayClassmatePrivateInfo.class);

  public DisplayClassmatePrivateInfo() {
    super(ID, true);
    super.setSubmitModify(ModifyClassmatePrivateInfo.ID);

    if (Request.getCurrentRequest().getParameters().getCookie(FormClassmatesLogin.CLASSMATE_ID, null) == null) {
      FormClassmatesLogin login = new FormClassmatesLogin();
      login.initialize(this);
    }
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    if (request.getParameters().getCookie(FormClassmatesLogin.CLASSMATE_ID, null) == null) {
      FormClassmatesLogin login = new FormClassmatesLogin();
      login.initialize(this);
      login.fillBodyContent(request, buffer);
    } else {
      int classmate = request.getParameters().getInt("classmate");
  
      if (classmate <= 0) {
        buffer.append("Избери някой от съучениците като посочиш с мишката образа му в снимката!");
      } else if (request.getParameters().getCookie(FormClassmatesLogin.CLASSMATE_ID, "").equals(String.valueOf(classmate))) {
        buffer.append("<textarea name='private_info' cols='20%' rows='12' >");
        buffer.append(getInfo(classmate).replaceAll("<", "&lt;"));
        buffer.append("</textarea>");
        ComponentSelectNumber.fillHidden(buffer, "classmate", classmate);
        buffer.append("<br />");
        super.addSubmit(buffer, "Запази");
      } else {
        buffer.append("<pre>");
        buffer.append(getInfo(classmate));
        buffer.append("</pre>");
      }
    }
  }

  private String getInfo(int classmate) {
    String info = Database.query("SELECT private_info FROM project_classmates WHERE classmate_id = " + classmate);
    if (info == null) {
      info = "Няма информация ...";
    }
    return info;
  }
}
