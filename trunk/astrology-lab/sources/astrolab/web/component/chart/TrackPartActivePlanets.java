package astrolab.web.component.chart;

import java.util.Calendar;

import astrolab.web.SVGDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class TrackPartActivePlanets extends SVGDisplay {

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
		fillContent(request, buffer, true);
	}

	public void fillContent(Request request, LocalizedStringBuffer buffer, boolean ownImage) {
		if (ownImage) {
			buffer.append("<svg:svg version='1.1' baseProfile='full' width='500px' height='500px'>");
		}

//		PointGamma gamma = new PointGamma(60, PointGamma.START_FROM_ARIES);
//		gamma.initialize(Event.getSelectedEvent());
//		fillTrajectory(gamma.getTrajectory(Track.trackStart, Track.trackEnd), buffer);
//
//	  gamma = new PointGamma(72, PointGamma.START_FROM_ARIES);
//		gamma.initialize(Event.getSelectedEvent());
//		fillTrajectory(gamma.getTrajectory(Track.trackStart, Track.trackEnd), buffer);
//
//	  gamma = new PointGamma(84, PointGamma.START_FROM_ARIES);
//		gamma.initialize(Event.getSelectedEvent());
//		fillTrajectory(gamma.getTrajectory(Track.trackStart, Track.trackEnd), buffer);
//
//	 gamma = new PointGamma(60, PointGamma.START_FROM_ASCENDANT);
//		gamma.initialize(Event.getSelectedEvent());
//		fillTrajectory(gamma.getTrajectory(Track.trackStart, Track.trackEnd), buffer);
//
//	  gamma = new PointGamma(72, PointGamma.START_FROM_ASCENDANT);
//		gamma.initialize(Event.getSelectedEvent());
//		fillTrajectory(gamma.getTrajectory(Track.trackStart, Track.trackEnd), buffer);
//
//	  gamma = new PointGamma(84, PointGamma.START_FROM_ASCENDANT);
//		gamma.initialize(Event.getSelectedEvent());
//		fillTrajectory(gamma.getTrajectory(Track.trackStart, Track.trackEnd), buffer);

		if (ownImage) {
			buffer.append("</svg:svg>");
		}
	}

	private int getY(Calendar time) {
		long path = Track.trackEnd.getTimeInMillis() - Track.trackStart.getTimeInMillis();
		long gone = time.getTimeInMillis() - Track.trackStart.getTimeInMillis();
		return (int) (Track.height * gone / path);
	}

}
