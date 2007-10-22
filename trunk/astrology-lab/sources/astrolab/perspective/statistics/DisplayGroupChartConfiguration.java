package astrolab.perspective.statistics;

import astrolab.web.display.HTMLGroupDisplay;

public class DisplayGroupChartConfiguration extends HTMLGroupDisplay {

  public DisplayGroupChartConfiguration() {
    super(new int[] {
        DisplayTimeConfiguration.DISPLAY_ID,
        DisplayZodiacConfiguration.DISPLAY_ID,
        DisplayTypesConfiguration.DISPLAY_ID
    });
  }

}