package astrolab.perspective.statistics;

import astrolab.formula.display.ModifyFormulaeSetTime;
import astrolab.web.Display;
import astrolab.web.Modify;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayTypesConfiguration extends DisplayAbstractConfiguration {

  public final static int DISPLAY_ID = Display.getId(DisplayTypesConfiguration.class);
  public final static int MODIFY_TIME_ID = Modify.getId(ModifyFormulaeSetTime.class);

  public DisplayTypesConfiguration() {
    super("Types", Display.getId(DisplayTypesDataChart.class));
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    super.fillBodyContent(request, buffer);
	}

}