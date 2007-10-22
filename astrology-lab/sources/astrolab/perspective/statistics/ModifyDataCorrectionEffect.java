package astrolab.perspective.statistics;

import astrolab.formula.Formulae;
import astrolab.web.Modify;
import astrolab.web.server.Request;
import astrolab.web.server.RequestParameters;

public class ModifyDataCorrectionEffect extends Modify {

  public final static int ID = ModifyDataCorrectionEffect.getId(ModifyDataCorrectionEffect.class);

  public void operate(Request request) {
    Formulae base = StatisticsFormulae.getFormulae();
    RequestParameters input = Request.getCurrentRequest().getParameters();
    StatisticsFormulaeCorrection correction = new StatisticsFormulaeCorrection(new String[] {
        input.get("clevel", "0"),
        input.get("coffset", "0"),
        input.get("caltitude", "0"),
        input.get("clength", "0"),
        String.valueOf(base.getId())
    });
    correction.store(0);
  }

}