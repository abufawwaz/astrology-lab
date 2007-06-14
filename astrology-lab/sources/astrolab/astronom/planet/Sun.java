package astrolab.astronom.planet;

import astrolab.db.Text;

public class Sun extends Planet {

  private final static int ID = Text.getId(SolarSystem.SUN);

  public Sun(PlanetSystem system) {
    super(ID, system);

    accordPoint.setCoordinates(0, 0, 0);
    coordinates.setCoordinates(0, 0, 0);
  }

  public void markToBePositioned() {
  }

  public boolean position(double time) {
    return true;
  }

  public String getName() {
  	return SolarSystem.SUN;
  }

	public String getIcon() {
		return "<g style='stroke:gold;stroke-width:3;fill:none'><circle r='10' /><circle r='1' /></g>";
	}

}