package astrolab.astronom.fiction;

import java.util.Calendar;

import astrolab.astronom.util.Zodiac;

import astrolab.astronom.houses.PlacidusSystem;
import astrolab.astronom.track.ActivePointTrajectory;

public class PointGamma extends ActivePointFictional {

	public static final int START_FROM_ARIES = 0;
	public static final int START_FROM_ASCENDANT = 1;

	private static final double YEAR = 365.24 * 24 * 60 * 60 * 1000;

	private int cycle;
	private int startFrom;

	public PointGamma(int cycle, int startFrom) {
		this.cycle = cycle;
		this.startFrom = startFrom;
	}

	public String getName() {
		return "Gamma";
	}

	public double getStart() {
		return (startFrom == START_FROM_ASCENDANT) ? new PlacidusSystem(getBirth()).getHouse(1) : 0;
	}

	public double getPosition(Calendar calendar) {
		double millis = calendar.getTimeInMillis() - getBirth().getTime().getTimeInMillis();
		return Zodiac.degree(getStart() - (millis / YEAR) / cycle * 360);
	}

	public String getIcon() {
		return "<text x='0' y='0'>G</text>";
	}

	public ActivePointTrajectory getTrajectory(Calendar start, Calendar end) {
		return PointGammaTrajectory.getTrajectory(this, start, end, YEAR * cycle);
	}

}
