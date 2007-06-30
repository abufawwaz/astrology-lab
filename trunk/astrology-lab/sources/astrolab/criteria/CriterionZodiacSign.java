package astrolab.criteria;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.SpacetimeEvent;
import astrolab.db.Text;
import astrolab.web.server.content.LocalizedStringBuffer;

public class CriterionZodiacSign extends Criterion {

  public final static int SIGN_ARIES       = Text.getId("Aries");
  public final static int SIGN_TAURUS      = Text.getId("Taurus");
  public final static int SIGN_GEMINI      = Text.getId("Gemini");
  public final static int SIGN_CANCER      = Text.getId("Cancer");
  public final static int SIGN_LEO         = Text.getId("Leo");
  public final static int SIGN_VIRGO       = Text.getId("Virgo");
  public final static int SIGN_LIBRA       = Text.getId("Libra");
  public final static int SIGN_SCORPIO     = Text.getId("Scorpio");
  public final static int SIGN_SAGITTARIUS = Text.getId("Sagittarius");
  public final static int SIGN_CAPRICORN   = Text.getId("Capricorn");
  public final static int SIGN_AQUARIUS    = Text.getId("Aquarius");
  public final static int SIGN_PISCES      = Text.getId("Pisces");

  public CriterionZodiacSign() {
    super();
  }

  public CriterionZodiacSign(int id, int activePoint, int sign) {
    super(id, Criterion.TYPE_ZODIAC_SIGN, activePoint, sign);
  }

  public String getName() {
    return "PositionSign";
  }

  public String[] getActorTypes() {
    return new String[] { "Planet", "'position'", "Sign" };
  }

  public int getMark(SpacetimeEvent periodStart, SpacetimeEvent periodEnd) {
    double position = ActivePoint.getActivePoint(getActivePoint(), periodStart).getPosition();
    return (((int) (position / 30)) == (getFactor() - SIGN_ARIES)) ? 1 : 0;
  }

  protected void store(String[] inputValues) {
    int sign = Integer.parseInt(inputValues[2]);
    new CriterionZodiacSign(getId(), Integer.parseInt(inputValues[0]), sign).store();
  }

  public void toString(LocalizedStringBuffer output) {
    output.localize(getActor());
    output.append(" ");
    output.localize("position");
    output.append(" ");
    output.localize("in");
    output.append(" ");
    output.localize(getFactor());
  }

}
