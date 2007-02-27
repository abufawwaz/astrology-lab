package astrolab.web.component.time;

import java.util.Properties;

import astrolab.astronom.Time;
import astrolab.tools.Template;
import astrolab.web.project.archive.relocation.RelocationRecord;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

// TODO: combine with location!
public class ComponentSelectTime {

  /**
   * @deprecated will be private soon!
   */
  public static final String PARAMETER_KEY = "_date";

  private static String RAW_CONTENTS = Template.template("astrolab/web/component/time/time_chooser.html");

  public static void fill(LocalizedStringBuffer buffer, String choiceId) {
    Properties parameters = new Properties();
    parameters.setProperty("{0}", choiceId);
    parameters.setProperty("{1}", "true");
    parameters.setProperty("{2}", "");
    parameters.setProperty("{3}", "false");

    buffer.append(Template.populate(RAW_CONTENTS, parameters));
  }

  public static void fill(LocalizedStringBuffer buffer, Time time, String choiceId, boolean singleSubmit) {
    String date = "" + time.get(Time.YEAR) + ", " + time.get(Time.MONTH) + ", " + time.get(Time.DATE) + ", " + time.get(Time.HOUR) + ", " + time.get(Time.MINUTE) + ", " + time.get(Time.SECOND) + "";

    Properties parameters = new Properties();
    parameters.setProperty("{0}", choiceId);
    parameters.setProperty("{1}", "false");
    parameters.setProperty("{2}", date);
    parameters.setProperty("{3}", String.valueOf(singleSubmit));

    buffer.append(Template.populate(RAW_CONTENTS, parameters));
  }

  public static int retrieve(Request request) {
    return Integer.parseInt(request.get(PARAMETER_KEY));
  }

  public static Time retrieve(Request request, String choiceId) {
    String timeText = request.get(choiceId);

    if (timeText != null) {
      int user = request.getUser();
      // TODO: fix this!
      int location = RelocationRecord.getLocationOfPerson(user, new Time(timeText, 0).getTimeInMillis());
      return new Time(timeText, location);
    } else {
      return null;
    }
  }

}
