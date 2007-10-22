package astrolab.web.component.svg;

import astrolab.web.SVGDisplay;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentHorizontalScrollBar implements Component {

  private final static String SCROLL_ID_PREFIX = ComponentHorizontalScrollBar.class.getName() + "_";

  private SVGDisplay display;
  private String color = "green";

  private double x;
  private double knob;
  private double y;
  private double width;
  private double height;
  private String scrollIdPrefix = SCROLL_ID_PREFIX + hashCode() + "_";
  private String scrollBarId = scrollIdPrefix + "bar";
  private String scrollKnobId = scrollIdPrefix + "knob";

  public ComponentHorizontalScrollBar(SVGDisplay display) {
    this.display = display;
    this.x = 0;
    this.knob = 0;
    this.y = 0;
    this.width = display.getWidth();
    this.height = 10;
  }

  public String getId() {
    return scrollBarId;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public void setKnob(double x) {
    this.knob = x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public void setWidth(double width) {
    this.width = width;
  }

  public void setY(double y) {
    this.y = y;
  }

  public void fill(LocalizedStringBuffer buffer) {
    double midHeight = height / 2;

    buffer.append("<g id='" + scrollBarId + "' transform='translate(" + x + "," + y + ")' style='stroke:" + color + ";fill:" + color + ";pointer-events:all'>");
    buffer.append("<rect width='" + width + "' height='" + height + "' style='opacity:0.1' />");
    buffer.append("<line x2='" + width + "' y1='" + midHeight + "' y2='" + midHeight + "' />");
    buffer.append("<rect id='" + scrollKnobId + "' transform='translate(" + knob + ",0)' width='3' height='10' />");
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
    buffer.append("      var x"); buffer.newline();
    buffer.append("      var width = (window.innerWidth) ? window.innerWidth : document.body.clientWidth"); buffer.newline();
    buffer.append("      if (e.pageX) {"); buffer.newline();
    buffer.append("        x = e.pageX * " + display.getWidth() + " / width"); buffer.newline();
    buffer.append("      } else if (e.clientX) {"); buffer.newline();
    buffer.append("        x = e.clientX * " + display.getWidth() + " / width"); buffer.newline();
    buffer.append("      }"); buffer.newline();
    buffer.append("      x -= this.transform.baseVal.getConsolidationMatrix().e"); buffer.newline();
    buffer.append("      document.getElementById('" + scrollKnobId + "').setAttribute('transform', 'translate(' + x + ',0)')"); buffer.newline();
    buffer.append("      top.fireEvent(window, '" + getId() + ".adjusting', x, true)"); buffer.newline();
    buffer.append("    }"); buffer.newline();
    buffer.append("  }"); buffer.newline();
    buffer.append("}"); buffer.newline();
    buffer.append("//]]>"); buffer.newline();
    buffer.append("</script>"); buffer.newline();
  }

}