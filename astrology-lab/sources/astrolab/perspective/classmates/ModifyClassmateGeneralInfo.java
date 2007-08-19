package astrolab.perspective.classmates;

import astrolab.db.Database;
import astrolab.web.Modify;
import astrolab.web.server.Request;

public class ModifyClassmateGeneralInfo extends Modify {

  public final static int ID = Modify.getId(ModifyClassmateGeneralInfo.class);

  public void operate(Request request) {
    int classmate = 0;
    String classmateText = request.getParameters().getCookie(FormClassmatesLogin.CLASSMATE_ID, null);
    if (classmateText != null) {
      try {
        classmate = Integer.valueOf(classmateText);
      } catch (NumberFormatException nfe) {
      }

      if (classmate > 0) {
        String info = request.getParameters().get("general_info");
        if (info != null && info.length() > 0) {
          info = info.replace('\'', '|');
          info = info.replace('\"', '|');
          Database.execute("UPDATE project_classmates SET general_info = '" + info + "' WHERE classmate_id = " + classmate);
        }
      }
    }
  }

}
