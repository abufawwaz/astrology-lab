package astrolab.project.classmates;

import astrolab.db.Database;
import astrolab.web.Display;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.component.ComponentSelectNumber;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayClassmateGeneralInfo extends HTMLFormDisplay {

  private final static int ID = Display.getId(DisplayClassmateGeneralInfo.class);

  public DisplayClassmateGeneralInfo() {
    super(ID, true);
    super.setSubmitModify(ModifyClassmateGeneralInfo.ID);
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    int classmate = request.getParameters().getInt("classmate");

    if (classmate <= 0) {
      buffer.append("Избери някой от съучениците като посочиш с мишката образа му в снимката!");
    } else if (request.getParameters().getCookie(FormClassmatesLogin.CLASSMATE_ID, "").equals(String.valueOf(classmate))) {
      buffer.append("<textarea name='general_info' cols='80%' rows='12' >");
      buffer.append(getInfo(classmate));
      buffer.append("</textarea>");
      ComponentSelectNumber.fillHidden(buffer, "classmate", classmate);
      super.addSubmit(buffer, "Запази");
    } else {
      buffer.append("<pre>");
      buffer.append(getInfo(classmate));
      buffer.append("</pre>");
    }
  }

  private String getInfo(int classmate) {
    String info = Database.query("SELECT general_info FROM project_classmates WHERE classmate_id = " + classmate);
    if (info == null) {
      info = "Няма информация ...";
    }
    return info;
  }

}
