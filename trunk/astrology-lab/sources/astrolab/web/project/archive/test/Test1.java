package astrolab.web.project.archive.test;

import java.util.Vector;

import astrolab.web.SVGDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class Test1 extends SVGDisplay {

	private static String[] COLORS = { "silver", "green", "blue", "cyan", "orange", "red", "brown" };

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    int sequence = (request.getSelection().length == 6) ? request.getSelection()[0] + 1 : 1;

    if (sequence > 50) {
      buffer.append("<text x='20' y='40'>Test is over! Do it again tomorrow.</text>\r\n"); // get text from database
    } else {
      buffer.append("<script type=\"text/javascript\">\r\n");
      buffer.append("//<![CDATA[\r\n");
      buffer.append("\r\n var time = new Date()");
      buffer.append("\r\n function darken(evt) {");
      buffer.append("\r\n  evt.target.setAttribute('stroke', 'black');");
      buffer.append("\r\n }");
      buffer.append("\r\n function lighten(evt) {");
      buffer.append("\r\n  evt.target.setAttribute('stroke', 'none');");
      buffer.append("\r\n }");
      buffer.append("\r\n function test(evt, link_id, answer, color) {");
      buffer.append("\r\n  top.window.eval('select(new Array(");
      buffer.append(sequence);
      buffer.append(",' + answer + ',' + color + ',' + (new Date() - time) + ',' + evt.screenX + ',' + evt.screenY + '))')");
      buffer.append("\r\n }");
      buffer.append("\r\n//]]>\r\n");
      buffer.append("</script>\r\n");

      buffer.append("<text x='30' y='20'>Question ");
      buffer.append(sequence);
      buffer.append(" / 50");
      buffer.append("</text>\r\n");

	    Vector[] vs = new Vector[COLORS.length];
	    int answer = generateTable(vs);
      buffer.append("<g style=\"stroke-width:1\">\r\n");
	    for (int i = 0; i < COLORS.length; i++) {
	      for (int j = 0; j < vs[i].size(); j++) {
	      	String id = "a" + i + ":" + j;
	        int[] a = (int[]) vs[i].get(j);
	        buffer.append("<a id=\"" + id + "\" xlink:href=\"\">\r\n");
	        buffer.append("<rect x='" + (6 * a[0] + 30) + "' y='" + (6 * a[1] + 30) + "' rx='1' ry='1' width='5' height='5' fill='" + COLORS[i] + "' onclick='test(evt, \"" + id + "\", " + ((i == answer) ? 1 : 0) + ", \"" + i + "\")' onmouseover='darken(evt)' onmouseout='lighten(evt)' />");
	        buffer.append("</a>\r\n");
	      }
	    }
	    buffer.append("</g>\r\n");
    }
  }

  private int generateTable(Vector[] vs) {
    int r = (int) (Math.random() * COLORS.length);
    Vector v = new Vector();

    for (int i = 0; i < 6; i++) {
      for (int j = 0; j < 6; j++) {
        v.add(new int[] { i, j });
      }
    }
    for (int i = 0; i < COLORS.length; i++) {
    	vs[i] = new Vector();
      for (int j = 0; j < ((i == r) ? 6 : 5); j++) {
        int k = (int) (Math.random() * v.size());
        vs[i].add((int[]) v.remove(k));
      }
    }
    for (int i = 0; i < COLORS.length; i++) {
    	int a = getAdjacent(vs[i]);
    	while (a >= 0) {
    		if (!switchOne(vs, i, a)) {
    			break;
    		}
    		a = getAdjacent(vs[i]);
    	}
    }
    return r;
	}

	// returns the first that is adjacent to another
	private int getAdjacent(Vector vs) {
		for (int i = 0; i < vs.size(); i++) {
			if (isAdjacent((int[]) vs.get(i), vs, i)) {
				return i;
			}
		}
		return -1;
	}

	private boolean isAdjacent(int[] a, Vector vs, int exclude) {
		int[] b;
		for (int i = 0; i < vs.size(); i++) {
			if (exclude != i) {
   			b = (int[]) vs.get(i);
		  	if (Math.abs(a[0] - b[0]) <= 1 && Math.abs(a[1] - b[1]) <= 1) {
			    return true;
			  }
			}
		}
		return false;
	}

	private boolean switchOne(Vector[] vs, int color, int adjacent) {
		int[] a = (int[]) vs[color].get(adjacent);
		int[] b;
		Vector v;
		for (int i = 0; i < vs.length; i++) {
			if (i != color) {
				v = vs[i];
				for (int j = 0; j < v.size(); j++) {
					b = (int[]) v.get(j);
					if (!isAdjacent(b, vs[color], adjacent) && !isAdjacent(a, v, j)) {
            vs[color].remove(a);
            v.remove(b);
						vs[color].insertElementAt(b, 0);
						v.add(a);
						return true;
					}
				}
			}
		}
		return false;
	}

}
