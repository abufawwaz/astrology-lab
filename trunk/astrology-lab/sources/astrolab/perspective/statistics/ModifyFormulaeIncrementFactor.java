package astrolab.perspective.statistics;

import astrolab.web.Modify;
import astrolab.web.server.Request;

public class ModifyFormulaeIncrementFactor extends Modify {

  public final static int MODIFY_ID = Modify.getId(ModifyFormulaeIncrementFactor.class);
  public final static String KEY_ELEMENT = "_element";
  public final static String KEY_FACTOR = "_factor";

  public void operate(Request request) {
    int factor = request.getParameters().getInt(KEY_ELEMENT);

    if (factor > 0) {
      StatisticsFormulae.changeFactor(factor, request.getParameters().getInt(KEY_FACTOR));
    }
  }

}
