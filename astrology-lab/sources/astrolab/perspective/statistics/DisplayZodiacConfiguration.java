package astrolab.perspective.statistics;

import java.util.ArrayList;
import java.util.Properties;

import astrolab.criteria.Criterion;
import astrolab.db.Text;
import astrolab.formula.Formulae;
import astrolab.formula.display.ModifyFormulaeSetChartColor;
import astrolab.formula.display.ModifyFormulaeSetTime;
import astrolab.project.ProjectDataFiller;
import astrolab.project.ProjectDataKey;
import astrolab.project.Projects;
import astrolab.web.Display;
import astrolab.web.Modify;
import astrolab.web.component.ComponentControllable;
import astrolab.web.component.ComponentController;
import astrolab.web.component.ComponentLink;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayZodiacConfiguration extends DisplayAbstractConfiguration {

  public final static int DISPLAY_ID = Display.getId(DisplayZodiacConfiguration.class);
  public final static int MODIFY_TIME_ID = Modify.getId(ModifyFormulaeSetTime.class);
  public final static int MODIFY_CORRECTION_ID = Modify.getId(ModifyDataCorrectionEffect.class);

  public DisplayZodiacConfiguration() {
    super("Zodiac", Display.getId(DisplayZodiacDataChart.class));
    super.addAction(ComponentCorrectionFormulaeBuilder.EVENT_OFFSET, "javascript:document.getElementById('form_correction').coffset.value=message;calculateEffect()");
    super.addAction(ComponentCorrectionFormulaeBuilder.EVENT_ALTITUDE, "javascript:document.getElementById('form_correction').caltitude.value=message;calculateEffect()");
    super.addAction(ComponentCorrectionFormulaeBuilder.EVENT_LENGTH, "javascript:document.getElementById('form_correction').clength.value=message;calculateEffect()");
    super.addAction(ComponentCorrectionFormulaeBuilder.EVENT_LEVEL, "javascript:document.getElementById('form_correction').clevel.value=message;calculateEffect()");
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    super.fillBodyContent(request, buffer);

    // Chart series
    buffer.append("<table border='0' width='100%'>");

    IteratorProjectZodiacData data = new IteratorProjectZodiacData(Projects.getProject());
    Formulae[] series = data.getSeries();
    for (int i = 0; i < series.length && i < COLORS.length; i++) {
      buffer.append("<tr>");

      buffer.append("<td width='30'>");
      Properties parameters = new Properties();
      parameters.put("_m", String.valueOf(Modify.getId(ModifyFormulaeSetChartColor.class)));
      parameters.put(ModifyFormulaeSetChartColor.KEY, String.valueOf(series[i].getId()));
      parameters.put(ModifyFormulaeSetChartColor.IS_TO_SET, "false");
      ComponentLink.fillLink(buffer, parameters, COLORS[i]);
      buffer.append("</td>");

      buffer.append("<td onmouseover='document.getElementById(\"series_correction\").style.visibility=\"visible\"' onmouseout='document.getElementById(\"series_correction\").style.visibility=\"hidden\"'>");
      buffer.append(series[i].getText());
      buffer.append("<sup>*</sup>");

      buffer.append("<div id='series_correction' style='position:absolute;background-color:#DDDDFF;opacity:0.9;visibility:hidden'>");
      buffer.append("Correction:");
      for (StatisticsFormulaeCorrection correction: StatisticsFormulaeCorrection.readCorrections(i)) {
        buffer.append(" - ");
        correction.fill(buffer);
        buffer.append("<br />");
      }
      buffer.append("<a href='javascript:document.getElementById(\"form_correction\").submit()'>Add correction</a>");
      buffer.append("</div>");
      buffer.append("</td>");

      buffer.append("</tr>");
    }

    // Chart base
    buffer.append("<tr><td>");
    buffer.localize("x-axis");
    buffer.append("</td><td>");
    buffer.append("<table><tr><td>");
    StatisticsFormulae.getFormulae().fill(buffer);
    buffer.append("</td><td>");
    fillControl(buffer, Criterion.TYPE_PLANET, "+", 1);
    buffer.append("</td><td>");
    fillControl(buffer, Criterion.TYPE_PLANET, "-", -1);
    buffer.append("</td></tr></table>");

    buffer.append("</td></tr>");
    buffer.append("</table>");

    fillBaseComponents(request, buffer);
    fillCorrectionControl(buffer);
	}

  private final void fillControl(LocalizedStringBuffer buffer, String type, String text, int factor) {
    String controlId = "control_" + type + text.hashCode();
    LocalizedStringBuffer functionBuffer = new LocalizedStringBuffer();

    functionBuffer.append("function(new_option) { window.location.href='");

    Properties parameters = new Properties();
    parameters.put("_m", String.valueOf(ModifyFormulaeIncrementFactor.MODIFY_ID));
    parameters.put(ModifyFormulaeIncrementFactor.KEY_FACTOR, String.valueOf(factor));
    ComponentLink.fillURL(functionBuffer, parameters);

    functionBuffer.append("&amp;");
    functionBuffer.append(ModifyFormulaeIncrementFactor.KEY_ELEMENT);
    functionBuffer.append("=' + new_option }");

    buffer.append("<tr><td>");
    ComponentControllable.fill(buffer, controlId, type, functionBuffer.toString());
    buffer.append("<div id='");
    buffer.append(controlId);
    buffer.append("' width='100%'><i>");
    buffer.localize(text);
    buffer.append("</i></div>");
    buffer.append("</td></tr>");
  }

  private void fillBaseComponents(Request request, LocalizedStringBuffer buffer) {
    buffer.append("<table border='0' width='100%' cellspacing='0' cellpadding='0'>");
    ArrayList<Integer> components = filterComponents(Criterion.TYPES.get(Criterion.TYPE_PLANET));

    buffer.append("<tr>");
    for (int i = 0; i < components.size(); i++) {
      if ((i > 0) && (i % 6 == 0)) {
        buffer.append("</tr><tr>");
      }
      int id = components.get(i);

      ProjectDataFiller.startFiller(Text.getDescriptiveId(id), Projects.getProject());

      String controlId = "controller_" + Criterion.TYPE_PLANET + "_" + id; 
      ComponentController.fill(buffer, controlId, Criterion.TYPE_PLANET, String.valueOf(id));
      buffer.append("<td id='");
      buffer.append(controlId);
      buffer.append("'>");
      buffer.localize(id);
      buffer.append("</td>");
    }
    buffer.append("</tr>");
    buffer.append("</table>");
  }

  private final void fillCorrectionControl(LocalizedStringBuffer buffer) {
    buffer.append("<form id='form_correction' name='form_correction' method='post' action='");
    Properties parameters = new Properties();
    parameters.put("_m", String.valueOf(MODIFY_CORRECTION_ID));
    ComponentLink.fillURL(buffer, parameters);
    buffer.append("'>");
    buffer.append("<table>");
    buffer.append("<tr><th colspan='2'>Correction</th></tr>");
    buffer.append("<tr><td>Level</td><td><input name='clevel' type='text' /></td></tr>");
    buffer.append("<tr><td>Offset</td><td><input name='coffset' type='text' /></td></tr>");
    buffer.append("<tr><td>Length</td><td><input name='clength' type='text' /></td></tr>");
    buffer.append("<tr><td>Altitude</td><td><input name='caltitude' type='text' /></td></tr>");
    buffer.append("<tr><td>Effect</td><td><input name='ceffect' type='text' /></td></tr>");
    buffer.append("</table>");
    buffer.append("</form>");
    buffer.append("<script type='text/javascript'>");
    buffer.append("function calculateEffect() {");
    buffer.append(" top.postAjaxRequest('ajax.html?_d=" + AJAXDataCorrectionEffect.ID + "', document.getElementById('form_correction'), function(ajax) {");
    buffer.append("  document.getElementById('form_correction').ceffect.value = ajax.responseText;");
    buffer.append(" })");
    buffer.append("}");
    buffer.append("</script>");
  }

  private ArrayList<Integer> filterComponents(ArrayList<Integer> components) {
    ArrayList<Integer> result = new ArrayList<Integer>();
    ProjectDataKey[] projectKeys = Projects.getProject().getKeys();
    for (ProjectDataKey key: projectKeys) {
      int keyId = Text.getId(key.getName());
      if (components.contains(keyId)) {
        result.add(keyId);
      }
    }
    return result;
  }

}