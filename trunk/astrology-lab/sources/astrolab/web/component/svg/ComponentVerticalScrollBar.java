package astrolab.web.component.svg;

import astrolab.web.SVGDisplay;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentVerticalScrollBar implements Component {

  private final static String SCROLL_ID_PREFIX = ComponentVerticalScrollBar.class.getName() + "_";

  private SVGDisplay display;
  private String color = "green";

  private double x;
  private double y;
  private double knob;
  private double width;
  private double height;
  private String scrollIdPrefix = SCROLL_ID_PREFIX + hashCode() + "_";
  private String scrollBarId = scrollIdPrefix + "bar";
  private String scrollKnobId = scrollIdPrefix + "knob";

  public ComponentVerticalScrollBar(SVGDisplay display) {
    this.display = display;
    this.x = 0;
    this.knob = 0;
    this.y = 0;
    this.width = 10;
    this.height = display.getHeight();
  }

  public String getId() {
    return scrollBarId;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public void setKnob(double y) {
    this.knob = y;
  }

  public void setX(double x) {
    this.x = x;
  }

  public void setY(double y) {
    this.y = y;
    this.knob = y;
  }

  public void setHeight(double height) {
    this.height = height;
  }

  public void fill(LocalizedStringBuffer buffer) {
    double midWidth = width / 2;

    buffer.append("<g id='" + scrollBarId + "' transform='translate(" + x + "," + (y - height) + ")' style='stroke:" + color + ";fill:" + color + ";pointer-events:all'>");
    buffer.append("<rect width='" + width + "' height='" + height + "' style='opacity:0.1' />");
    buffer.append("<line x1='" + midWidth + "' x2='" + midWidth + "' y2='" + height + "' />");
    buffer.append("<rect id='" + scrollKnobId + "' transform='translate(0," + knob + ")' width='10' height='3' />");
    buffer.append("</g>");
    fillScript(buffer);
  }

  public void fillScript(LocalizedStringBuffer buffer) {
    buffer.newline();
    buffer.append("<script language='javascript'>"); buffer.newline();
    buffer.append("//<![CDATA["); buffer.newline();
    buffer.append("var dragging = false"); buffer.newline();
    buffer.newline();
    buffer.append("document.getElementById('" + scrollKnobId + "').onmousedown = function() { dragging = true }"); buffer.newline();
    buffer.append("document.getElementById('" + scrollBarId + "').onmouseup = function() {");
    buffer.append("  dragging = false;");
    buffer.append("  top.fireEvent(window, '" + getId() + "', 1, true)");
    buffer.append("}"); buffer.newline();
    buffer.append("document.getElementById('" + scrollBarId + "').onmousemove = function(e) {"); buffer.newline();
    buffer.append("  if (dragging) {"); buffer.newline();
    buffer.append("    if (!e) e = window.event"); buffer.newline();
    buffer.append("    if (e) {"); buffer.newline();
    buffer.append("      var y"); buffer.newline();
    buffer.append("      var height = (window.innerHeight) ? window.innerHeight : document.body.clientHeight"); buffer.newline();
    buffer.append("      if (e.pageY) {"); buffer.newline();
    buffer.append("        y = e.pageY * " + display.getHeight() + " / height"); buffer.newline();
    buffer.append("      } else if (e.clientY) {"); buffer.newline();
    buffer.append("        y = e.clientY * " + display.getHeight() + " / height"); buffer.newline();
    buffer.append("      }"); buffer.newline();
    buffer.append("      y -= this.transform.baseVal.getConsolidationMatrix().f"); buffer.newline();
    buffer.append("      document.getElementById('" + scrollKnobId + "').setAttribute('transform', 'translate(0,' + y + ')')"); buffer.newline();
    buffer.append("      top.fireEvent(window, '" + getId() + ".adjusting', y, true)"); buffer.newline();
    buffer.append("    }"); buffer.newline();
    buffer.append("  }"); buffer.newline();
    buffer.append("}"); buffer.newline();
    buffer.append("//]]>"); buffer.newline();
    buffer.append("</script>"); buffer.newline();
  }

}