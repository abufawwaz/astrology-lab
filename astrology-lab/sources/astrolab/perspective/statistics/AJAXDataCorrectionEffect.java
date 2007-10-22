package astrolab.perspective.statistics;

import astrolab.db.Database;
import astrolab.formula.Formulae;
import astrolab.project.Projects;
import astrolab.web.display.AJAXDataResponse;
import astrolab.web.server.Request;
import astrolab.web.server.RequestParameters;
import astrolab.web.server.content.LocalizedStringBuffer;

public class AJAXDataCorrectionEffect extends AJAXDataResponse {

  public final static int ID = AJAXDataCorrectionEffect.getId(AJAXDataCorrectionEffect.class);

  protected void fillData(LocalizedStringBuffer buffer) {
    Formulae base = StatisticsFormulae.getFormulae();
    RequestParameters input = Request.getCurrentRequest().getParameters();
    StatisticsFormulaeCorrection correction = new StatisticsFormulaeCorrection(new String[] {
        input.get("clevel", "0"),
        input.get("coffset", "0"),
        input.get("caltitude", "0"),
        input.get("clength", "0"),
        String.valueOf(base.getId())
    });
    String series = new IteratorProjectZodiacData(Projects.getProject()).getSeries()[0].getText();
    for (Formulae c: StatisticsFormulaeCorrection.readCorrections(0)) {
      series += " - " + c.getSQL();
    }
    String result = Database.query("SELECT AVG(ABS(" + series + ")) - AVG(ABS(" + series + " - " + correction.getSQL() + ")) FROM project_" + Projects.getProject().getName());
    buffer.append(result);
  }

}