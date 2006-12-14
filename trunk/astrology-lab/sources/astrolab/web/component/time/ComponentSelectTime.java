package astrolab.web.component.time;

import java.util.Properties;

import astrolab.astronom.Time;
import astrolab.tools.Template;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

// TODO: combine with location!
public class ComponentSelectTime {

  /**
   * @deprecated will be private soon!
   */
  public static final String PARAMETER_KEY = "_date";

  private static String RAW_CONTENTS = Template.template("astrolab/web/component/time/time_chooser.html");

  public static void fill(LocalizedStringBuffer buffer, String choice_id) {
    Properties parameters = new Properties();
    parameters.setProperty("{0}", choice_id);
    parameters.setProperty("{1}", "true");
    parameters.put("{2}", "");

    buffer.append(Template.populate(RAW_CONTENTS, parameters));
  }

  public static void fill(LocalizedStringBuffer buffer, Time time, String choice_id) {
    String date = "" + time.get(Time.YEAR) + ", " + time.get(Time.MONTH) + ", " + time.get(Time.DATE) + ", " + time.get(Time.HOUR) + ", " + time.get(Time.MINUTE) + ", " + time.get(Time.SECOND) + "";

    Properties parameters = new Properties();
    parameters.setProperty("{0}", choice_id);
    parameters.put("{1}", "false");
    parameters.put("{2}", date);

    buffer.append(Template.populate(RAW_CONTENTS, parameters));
  }

  public static int retrieve(Request request) {
    return Integer.parseInt(request.get(PARAMETER_KEY));
  }

}
