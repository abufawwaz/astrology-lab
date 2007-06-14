package astrolab.astronom.planet;

import astrolab.astronom.util.Latia;
import astrolab.db.Text;

public class Mercury extends Planet {

  private final static int ID = Text.getId(SolarSystem.MERCURY);

  public Mercury(PlanetSystem system) {
    super(ID, system);

    mL = new Latia(102.2794, 149472.515, 0);
    eL = new Latia(.205614, .2E-4, 0);
    au = .3871;
    apL = new Latia(28.7538, .3703, .0001);
    anL = new Latia(47.1459, 1.1852, .0002);
    i_nL = new Latia(7.009, .00186, 0);
  }

  public String getName() {
  	return SolarSystem.MERCURY;
  }

	public String getIcon() {
		return "<circle r='5' cy='-3' style='stroke:yellow;stroke-width:3;fill:none' /><path d='M-4 -10 A8 8 0 0 0 4 -10' style='stroke:yellow;stroke-width:3;fill:none' /><line x1='0' y1='3' x2='0' y2='10' style='stroke:yellow;stroke-width:3' /><line x1='-4' y1='6' x2='4' y2='6' style='stroke:yellow;stroke-width:3' />";
	}

}