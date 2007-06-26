package astrolab.criteria;

import java.util.Hashtable;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.SpacetimeEvent;
import astrolab.astronom.planet.SolarSystem;
import astrolab.db.Text;
import astrolab.web.server.content.LocalizedStringBuffer;

public class CriterionRulerOfHouse extends Criterion {

  private final static Hashtable<Integer, int[]> RULERS = new Hashtable<Integer, int[]>();

  static {
    RULERS.put(0, new int[] {Text.getId(SolarSystem.MARS)}); //Aries ruled by Mars
    RULERS.put(1, new int[] {Text.getId(SolarSystem.VENUS)}); //Taurus ruled by Venus
    RULERS.put(2, new int[] {Text.getId(SolarSystem.MERCURY)}); //Gemini ruled by Mercury
    RULERS.put(3, new int[] {Text.getId(SolarSystem.MOON)}); //Cancer ruled by the Moon
    RULERS.put(4, new int[] {Text.getId(SolarSystem.SUN)}); //Leo ruled by the Sun
    RULERS.put(5, new int[] {Text.getId(SolarSystem.MERCURY)}); //Virgo ruled by Mercury
    RULERS.put(6, new int[] {Text.getId(SolarSystem.VENUS)}); //Libra ruled by Venus
    RULERS.put(7, new int[] {Text.getId(SolarSystem.MARS), Text.getId(SolarSystem.PLUTO)}); //Scorpio ruled by Mars and Pluto
    RULERS.put(8, new int[] {Text.getId(SolarSystem.JUPITER)}); //Sagittarius ruled by Jupiter
    RULERS.put(9, new int[] {Text.getId(SolarSystem.SATURN)}); //Capricorn ruled by Saturn
    RULERS.put(10, new int[] {Text.getId(SolarSystem.SATURN), Text.getId(SolarSystem.URANUS)}); //Aquarius ruled by Saturn and Uranus
    RULERS.put(11, new int[] {Text.getId(SolarSystem.JUPITER), Text.getId(SolarSystem.NEPTUNE)}); //Pisces ruled by Jupiter and Neptune
  }

  public CriterionRulerOfHouse() {
    super();
  }

  public CriterionRulerOfHouse(int id, int activePoint, int house) {
    super(id, TYPE_IS_HOUSE_RULER, activePoint, house);
  }

  public int getMark(SpacetimeEvent periodStart, SpacetimeEvent periodEnd) {
    int houseStart = (int) (ActivePoint.getActivePoint(getFactor(), periodStart).getPosition() / 30);
    int houseEnd = (int) (ActivePoint.getActivePoint(getFactor(), periodEnd).getPosition() / 30);

    if (houseEnd == houseStart) {
      houseEnd = houseStart + 1;
    }

    for (int i = houseStart; i < houseEnd; i++) {
      for (int ruler: RULERS.get(i)) {
        if (ruler == getActor()) {
          return 1;
        }
      }
    }
    return 0;
  }

  public String getName() {
    return "RulerOfHouse";
  }

  public String[] getActorTypes() {
    return new String[] { "Planet", "'rules'", "House" };
  }

  protected void store(String[] inputValues) {
    new CriterionRulerOfHouse(getId(), Integer.parseInt(inputValues[0]), Integer.parseInt(inputValues[2])).store();
  }

  public void toString(LocalizedStringBuffer output) {
    output.localize(getActor());
    output.append(" ");
    output.localize("rules");
    output.append(" ");
    output.localize(getFactor());
  }

}
