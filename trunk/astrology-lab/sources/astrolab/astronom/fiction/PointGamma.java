package astrolab.astronom.fiction;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.houses.HouseSystem;
import astrolab.db.Text;

public class PointGamma extends ActivePointFictional {

  private final static int ID = Text.getId("Gamma");

  public static final int START_FROM_ARIES = 0;
	public static final int START_FROM_ASCENDANT = 1;

	private static final double YEAR = 365.24 * 24 * 60 * 60 * 1000;

	private int cycle;
	private int startFrom;

	public PointGamma(int cycle, int startFrom) {
    super(ID);

    this.cycle = cycle;
		this.startFrom = startFrom;
	}

	public String getName() {
		return "Gamma";
	}

	public double getStart() {
		return (startFrom == START_FROM_ASCENDANT) ? ActivePoint.getActivePoint(HouseSystem.HOUSES[0], getBirth()).getPosition() : 0;
	}

	public double getPosition() {
    // TODO: restore Gamma
//		double millis = calendar.getTimeInMillis() - getBirth().getTime().getTimeInMillis();
//		return Zodiac.degree(getStart() - (millis / YEAR) / cycle * 360);
    return 0;
	}

	public String getIcon() {
		return "<text x='0' y='0'>G</text>";
	}

  protected final int getSystemType() {
    return SYSTEM_OTHER;
  }

}
