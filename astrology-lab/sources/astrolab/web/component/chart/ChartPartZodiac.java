package astrolab.web.component.chart;

import astrolab.web.SVGDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ChartPartZodiac extends SVGDisplay {

	private final static String[] COLOR = { "red", "green", "yellow", "blue" };
	private final static double OFFSET_SIGN = Math.PI / 6;
	private final static double OFFSET_HALFSIGN = Math.PI / 12;
	private final static double OFFSET_DELTA = Math.PI / 180;

	private double radius, x, y;

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
		fillContent(request, buffer, 0.0, true);
	}

  public void fillContent(Request request, LocalizedStringBuffer buffer, double offset, boolean ownImage) {
		radius = request.getConstraints().getRadius() - 50;
		x = request.getConstraints().getWidth() / 2;
		y = request.getConstraints().getHeight() / 2;

		for (int i = 0; i < 12; i++) {
			fillSign((i * 30 - offset) * OFFSET_DELTA, i, buffer);
		}
	}

	private void fillSign(double offset, int index, LocalizedStringBuffer buffer) {
		buffer.append("\r\n\t<path d=\"M");

		int x1 = (int) (x - radius * Math.cos(offset));
		int y1 = (int) (y + radius * Math.sin(offset));
		int x2 = (int) (x - radius * Math.cos(offset + OFFSET_SIGN));
		int y2 = (int) (y + radius * Math.sin(offset + OFFSET_SIGN));

		buffer.append("" + x1 + " " + y1 + " ");
		buffer.append("A" + radius + " " + radius + " 0 "); // rx ry x-axis-rotation
		buffer.append("0 0 ");                              // large-arc-flag sweep-flag
		buffer.append("" + x2 + " " + y2);                  // x y
		buffer.append("\" style=\"fill:none;stroke:" + COLOR[index % 4] + ";stroke-width:50;stroke-opacity:0.3\" />");

		int iconx = (int) (x - radius * Math.cos(offset + OFFSET_HALFSIGN));
		int icony = (int) (y + radius * Math.sin(offset + OFFSET_HALFSIGN));

		buffer.append("<g transform='translate(" + iconx + ", " + icony + ")' style='fill:none;stroke:black;stroke-width:3'>");
		switch (index) {
	    case 0: { // Aries
			  buffer.append("<path d='M-13 0 A6 6 0 0 1 0 0 A5 5 0 0 1 13 0' />");
			  buffer.append("<line y2='13' />");
			  break;
	    }
	    case 1: { // Taurus
			  buffer.append("<path d='M-6 -11 A6 6 0 0 0 6 -11'  />");
			  buffer.append("<circle cy='3' r='8' />");
			  break;
	    }
	    case 2: { // Gemini
			  buffer.append("<path d='M-6 -11 A6 3 0 0 0 6 -11'  />");
			  buffer.append("<line x1='-3' y1='-7' x2='-3' y2='7' />");
			  buffer.append("<line x1='3' y1='-7' x2='3' y2='7' />");
			  buffer.append("<path d='M-6 11 A6 3 0 0 1 6 11'  />");
			  break;
	    }
	    case 3: { // Cancer
			  buffer.append("<path d='M9 -5 A9 3 0 0 0 -9 -5 A4 4 0 1 0 -8.8 -5.2'  />");
			  buffer.append("<path d='M-9 5 A9 3 0 0 0 9 5 A4 4 0 1 0 8.8 5.2'  />");
			  break;
	    }
	    case 4: { // Leo
		  	buffer.append("<path d='M-6,0 C-8,2 -3,5 -3,0 S-6,-5 -3,-10 S7,-8 7,-4 S3,5 3,6 S3,12 8,9' />");
		  	break;
	    }
	    case 5: { // Virgo
		  	buffer.append("<path d='M-9,8 L-9,-8 S-1,-17 -3,8' />");
		  	buffer.append("<path d='M-4,-8 S5,-19 2,14' />");
		  	buffer.append("<path d='M1,-8 C9,-15 11,6 1,9' />");
		  	break;
	    }
	    case 6: { // Libra
		  	buffer.append("<path d='M-13 0 L-3.5 0 A5 5 0 1 1 3.5 0 L13 0' />");
		  	buffer.append("<path d='M-13 5 L13 5' />");
		  	break;
	    }
	    case 7: { // Scorpio
		  	buffer.append("<path d='M-9,8 L-9,-8 S-1,-17 -3,8' />");
		  	buffer.append("<path d='M-4,-8 S5,-17 3,8 C3,14 13,12 9,-3 L8.5,2' />");
		  	break;
	    }
	    case 8: { // Sagittarius
			  buffer.append("<line x1='-10' y1='10' x2='10' y2='-10' />");
			  buffer.append("<line x1='0' y1='7' x2='-7' y2='0' />");
		  	buffer.append("<path d='M0 -7 L10 -10 L7 0' />");
		  	break;
	    }
	    case 9: { // Capricorn
		  	buffer.append("<path d='M-12,-5 Q-10,-5 -8,-9 Q-7,-5 0,-7 C-7,8 0,13 8,8 S0,-14 -1,14' />");
		  	break;
	    }
	    case 10: { // Aquarius
		  	buffer.append("<path d='M-12 -2 A4 4 0 0 1 -4 -2 A4 2 0 0 0 4 -2 A4 4 0 0 1 12 -2' />");
		  	buffer.append("<path d='M-12 5 A4 4 0 0 1 -4 5 A4 2 0 0 0 4 5 A4 4 0 0 1 12 5' />");
		  	break;
	    }
	    case 11: { // Pisces
		  	buffer.append("<path d='M-10 -10 A4 5 0 1 1 -10 10' />");
		  	buffer.append("<path d='M10 -10 A4 5 0 1 0 10 10' />");
			  buffer.append("<line x1='-8' x2='8' />");
		  	break;
	    }
		}
		buffer.append("</g>");
	}

}
