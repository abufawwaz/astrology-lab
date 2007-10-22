package astrolab.perspective.statistics;

import astrolab.web.SVGDisplay;
import astrolab.web.component.svg.Component;
import astrolab.web.component.svg.ComponentHorizontalScrollBar;
import astrolab.web.component.svg.ComponentVerticalScrollBar;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentCorrectionFormulaeBuilder implements Component {

  public final static String COMPONENT_ID = ComponentCorrectionFormulaeBuilder.class.getName();
  public final static String EVENT_OFFSET = COMPONENT_ID + ".offset";
  public final static String EVENT_LEVEL = COMPONENT_ID + ".level";
  public final static String EVENT_LENGTH = COMPONENT_ID + ".length";
  public final static String EVENT_ALTITUDE = COMPONENT_ID + ".altitude";

  private ComponentHorizontalScrollBar scrollBarOffset;
  private ComponentHorizontalScrollBar scrollBarLength;
  private ComponentVerticalScrollBar scrollBarLevel;
  private ComponentVerticalScrollBar scrollBarAltitude;

  private SVGDisplay display;

  private double startingOffset;
  private double startingLength;
  private double startingLevel;
  private double startingAltitude;

  public ComponentCorrectionFormulaeBuilder(SVGDisplay display, double miny, double maxy, int scale) {
    this.display = display;

    startingOffset = display.getWidth() / 6;
    startingLength = display.getWidth() / 6;
    startingLevel = 3 * display.getHeight() / 4;
    startingAltitude = display.getHeight() / 10;

    scrollBarOffset = new ComponentHorizontalScrollBar(display);
    scrollBarOffset.setY(display.getHeight() - 10);
    scrollBarOffset.setKnob(startingOffset);

    scrollBarLength = new ComponentHorizontalScrollBar(display);
    scrollBarLength.setX(startingOffset);
    scrollBarLength.setY(startingLevel);
    scrollBarLength.setKnob(startingLength);

    scrollBarLevel = new ComponentVerticalScrollBar(display);
    scrollBarLevel.setY(display.getHeight() - 10);
    scrollBarLevel.setHeight(display.getHeight() - 10);
    scrollBarLevel.setKnob(startingLevel);

    scrollBarAltitude = new ComponentVerticalScrollBar(display);
    scrollBarAltitude.setX(startingOffset - 10);
    scrollBarAltitude.setY(startingLevel);
    scrollBarAltitude.setKnob(display.getHeight() - startingAltitude);

    display.addAction(scrollBarOffset.getId() + ".adjusting", "javascript:correctionOffset(message)");
    display.addAction(scrollBarLevel.getId() + ".adjusting", "javascript:correctionLevel(message)");
    display.addAction(scrollBarLength.getId() + ".adjusting", "javascript:correctionLength(message)");
    display.addAction(scrollBarAltitude.getId() + ".adjusting", "javascript:correctionAltitude(message)");

    display.addAction(scrollBarOffset.getId(), "javascript:top.fireEvent(window, '" + EVENT_OFFSET + "', Math.floor(sOffset))");
    display.addAction(scrollBarLevel.getId(), "javascript:top.fireEvent(window, '" + EVENT_LEVEL + "', (" + miny + " + (" + (display.getHeight() - 10) + " - sLevel) * (" + maxy + " - " + miny + ") / " + scale + "))");
    display.addAction(scrollBarLength.getId(), "javascript:top.fireEvent(window, '" + EVENT_LENGTH + "', Math.floor(360 / Math.floor(sLength)) / 4)");
    display.addAction(scrollBarAltitude.getId(), "javascript:top.fireEvent(window, '" + EVENT_ALTITUDE + "', (" + miny + " + sAltitude * (" + maxy + " - " + miny + ") / " + scale + "))");

    display.addAction(SVGDisplay.TRIGGER_ON_LOAD, "top.fireEvent(this.window, '" + scrollBarAltitude.getId() + "', 1, false);");
    display.addAction(SVGDisplay.TRIGGER_ON_LOAD, "top.fireEvent(this.window, '" + scrollBarLevel.getId() + "', 1, false);");
    display.addAction(SVGDisplay.TRIGGER_ON_LOAD, "top.fireEvent(this.window, '" + scrollBarLength.getId() + "', 1, true);");
    display.addAction(SVGDisplay.TRIGGER_ON_LOAD, "top.fireEvent(this.window, '" + scrollBarOffset.getId() + "', 1, true);");
  }

  public String getId() {
    return COMPONENT_ID;
  }

  public void fill(LocalizedStringBuffer buffer) {
    scrollBarOffset.fill(buffer);
    scrollBarLength.fill(buffer);
    scrollBarLevel.fill(buffer);
    scrollBarAltitude.fill(buffer);

    buffer.append("<path id='" + getId() + "' d='m50,50 ");
    for (int i = 0; i < 5; i++) {
      buffer.append("a50 50 0 0 0 100 0 50 50 0 0 1 100 0");
    }
    buffer.append("' style='stroke:green;stroke-width:1;fill:none' />");

    fillScript(buffer);
  }

  public void fillScript(LocalizedStringBuffer buffer) {
    buffer.newline();
    buffer.append("<script type='text/javascript'>");
    buffer.append(" //<![CDATA["); buffer.newline();
    buffer.append(" var sLevel = " + startingLevel); buffer.newline();
    buffer.append(" var sOffset = " + startingOffset); buffer.newline();
    buffer.append(" var sLength = roundLength(" + startingLength + ")"); buffer.newline();
    buffer.append(" var sAltitude = " + startingAltitude); buffer.newline();
    buffer.append(" function correctionLevel(message) {");
    buffer.append("  sLevel = message;");
    buffer.append("  var node = document.getElementById('" + scrollBarLength.getId() + "');");
    buffer.append("  var x = node.transform.baseVal.getConsolidationMatrix().e;");
    buffer.append("  node.setAttribute('transform', 'translate(' + x + ',' + message + ')');");
    buffer.newline();
    buffer.append("  node = document.getElementById('" + scrollBarAltitude.getId() + "');");
    buffer.append("  x = node.transform.baseVal.getConsolidationMatrix().e;");
    buffer.append("  node.setAttribute('transform', 'translate(' + x + ',' + (message - " + display.getHeight() + ") + ')');");
    buffer.newline();
    buffer.append("  moveSinusoid();");
    buffer.append(" }");
    buffer.newline();
    buffer.append(" function correctionOffset(message) {");
    buffer.append("  sOffset = message;");
    buffer.append("  var node = document.getElementById('" + scrollBarLength.getId() + "');");
    buffer.append("  var y = node.transform.baseVal.getConsolidationMatrix().f;");
    buffer.append("  node.setAttribute('transform', 'translate(' + message + ',' + y + ')');");
    buffer.newline();
    buffer.append("  node = document.getElementById('" + scrollBarAltitude.getId() + "');");
    buffer.append("  y = node.transform.baseVal.getConsolidationMatrix().f;");
    buffer.append("  node.setAttribute('transform', 'translate(' + (message - 10) + ',' + y + ')');");
    buffer.newline();
    buffer.append("  moveSinusoid();");
    buffer.append(" }");
    buffer.newline();
    buffer.append(" function roundLength(vLength) {");
    buffer.append("  return 360 / Math.floor(360 / (vLength * 2)) / 4;");
    buffer.append(" }");
    buffer.newline();
    buffer.append(" function correctionLength(message) {");
    buffer.append("  sLength = roundLength(message);");
    buffer.append("  moveSinusoid();");
    buffer.append(" }");
    buffer.newline();
    buffer.append(" function correctionAltitude(message) {");
    buffer.append("  sAltitude = " + display.getHeight() + " - message;");
    buffer.append("  moveSinusoid();");
    buffer.append(" }");
    buffer.newline();
    buffer.append(" function moveSinusoid() {");
    buffer.append("  var node = document.getElementById('" + getId() + "');");
    buffer.append("  var path = 'm' + (sOffset - 12 * sLength) + ',' + sLevel;");
    buffer.append("  for (var cycles = 1; cycles < 20; cycles++) {");
    buffer.append("   path += ' a' + sLength + ' ' + sAltitude + ' 0 0 ' + (cycles % 2) + ' ' + (sLength * 2) + ' 0'");
    buffer.append("  }");
    buffer.append("  node.setAttribute('d', path);");
    buffer.append(" }");
    buffer.newline();
    buffer.append(" moveSinusoid();");
    buffer.newline();
    buffer.append("//]]>"); buffer.newline();
    buffer.append("</script>");
    buffer.newline();
  }

}