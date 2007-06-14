package astrolab.astronom.planet;

import astrolab.astronom.util.Latia;
import astrolab.db.Text;

public class Saturn extends Planet {

  private final static int ID = Text.getId(SolarSystem.SATURN);

  public Saturn(PlanetSystem system) {
    super(ID, system);

    mL = new Latia(174.2153, 1223.50796, 0);
    eL = new Latia(.05423, -.2E-3, 0);
    au = 9.5525;
    apL = new Latia(338.9117, -.3167, 0);
    anL = new Latia(112.8261, .8259, 0);
    i_nL = new Latia(2.4908, -.0047, 0);
  }

  public String getName() {
  	return SolarSystem.SATURN;
  }

	public String getIcon() {
		return "<g style='stroke:darkolivegreen;stroke-width:3;fill:none'><path d='M-4 -10 L-4 -2 A7 7 0 0 1 6 9' /><line x1='-10' y1='-6' x2='2' y2='-6' /></g>";
	}

}