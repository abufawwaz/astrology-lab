package astrolab.web.component.time;

import java.util.Properties;

import astrolab.astronom.SpacetimeEvent;
import astrolab.project.relocation.RelocationRecord;
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

  public static void fill(LocalizedStringBuffer buffer, String choiceId) {
    Properties parameters = new Properties();
    parameters.setProperty("{0}", choiceId);
    parameters.setProperty("{1}", "true");
    parameters.setProperty("{2}", "");
    parameters.setProperty("{3}", "false");

    buffer.append(Template.populate(RAW_CONTENTS, parameters));
  }

  public static void fill(LocalizedStringBuffer buffer, SpacetimeEvent time, String choiceId, boolean singleSubmit) {
    String date = "" + time.get(SpacetimeEvent.YEAR) + ", " + time.get(SpacetimeEvent.MONTH) + ", " + time.get(SpacetimeEvent.DAY_OF_MONTH) + ", " + time.get(SpacetimeEvent.HOUR_OF_DAY) + ", " + time.get(SpacetimeEvent.MINUTE) + ", " + time.get(SpacetimeEvent.SECOND) + "";

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

  public static SpacetimeEvent retrieve(Request request, String choiceId) {
    String timeText = request.get(choiceId);

    if (timeText != null) {
      int user = request.getUser();
      // TODO: fix this!
      int location = RelocationRecord.getLocationOfPerson(user, new SpacetimeEvent(timeText, 0).getTimeInMillis());
      return new SpacetimeEvent(timeText, location);
    } else {
      return null;
    }
  }

}
