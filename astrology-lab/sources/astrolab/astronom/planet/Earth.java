package astrolab.astronom.planet;

import astrolab.astronom.util.Latia;
import astrolab.db.Text;

public class Earth extends Planet {

  private final static int ID = Text.getId(SolarSystem.EARTH);

  public Earth(PlanetSystem system) {
    super(ID, system);

    mL = new Latia(358.4758, 35999.0498, -.0002);
    eL = new Latia(.01675, -.4E-4, 0);
    au = 1;
    apL = new Latia(101.2208, 1.7192, .00045);
    anL = new Latia(0, 0, 0);
    i_nL = new Latia(0, 0, 0);
  }

  public String getName() {
  	return SolarSystem.EARTH;
  }

}
