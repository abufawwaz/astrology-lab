package astrolab.web.project.archive.test;

import astrolab.web.SVGDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class Test2 extends SVGDisplay {

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    int sequence = (request.getSelection().length == 6) ? request.getSelection()[0] + 1 : 1;

    if (sequence > 50) {
      buffer.append("<text x='20' y='40'>Test is over! Do it again tomorrow.</text>\r\n"); // get text from database
    } else {
	    buffer.append("<script type=\"text/javascript\">\r\n");
	    buffer.append("//<![CDATA[\r\n");
	    buffer.append("\r\n var time;");
	    buffer.append("\r\n var pass = Math.floor(1000 * Math.random() + 500);");
	    buffer.append("\r\n setTimeout('initTest()', pass);");
	    buffer.append("\r\n function initTest() {");
	    buffer.append("\r\n  time = new Date()");
	    buffer.append("\r\n  document.getElementById('block_rect').setAttribute('visibility', 'visible')");
	    buffer.append("\r\n }");
	    buffer.append("\r\n function test(evt) {");
	    buffer.append("\r\n  document.getElementById('block_rect').setAttribute('visibility', 'hidden')");
      buffer.append("\r\n  top.window.eval('select(new Array(");
      buffer.append(sequence);
      buffer.append(",' + pass + ',5,' + (new Date() - time) + ',' + evt.screenX + ',' + evt.screenY + '))')");
	    buffer.append("\r\n }");
	    buffer.append("\r\n//]]>\r\n");
	    buffer.append("</script>\r\n");

      buffer.append("<text x='30' y='20'>Question ");
      buffer.append(sequence);
      buffer.append(" / 50");
      buffer.append("</text>\r\n");

	    buffer.append("<a id='block' xlink:href=''>\r\n");
	    buffer.append("<rect id='block_rect' x='40' y='40' rx='3' ry='3' width='20' height='20' fill='red' stroke='none' onclick='test(evt)' visibility='hidden' />");
	    buffer.append("</a>\r\n");
    }
	}

}
