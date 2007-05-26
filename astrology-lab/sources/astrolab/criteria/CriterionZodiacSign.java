package astrolab.criteria;

import astrolab.astronom.ActivePoint;
import astrolab.db.Database;
import astrolab.db.Text;
import astrolab.web.server.Request;

public class CriterionZodiacSign extends CriterionPosition {

  public final static int SIGN_ARIES       = 0x001;
  public final static int SIGN_TAURUS      = 0x002;
  public final static int SIGN_GEMINI      = 0x004;
  public final static int SIGN_CANCER      = 0x008;
  public final static int SIGN_LEO         = 0x010;
  public final static int SIGN_VIRGO       = 0x020;
  public final static int SIGN_LIBRA       = 0x040;
  public final static int SIGN_SCORPIO     = 0x080;
  public final static int SIGN_SAGITTARIUS = 0x100;
  public final static int SIGN_CAPRICORN   = 0x200;
  public final static int SIGN_AQUARIUS    = 0x400;
  public final static int SIGN_PISCES      = 0x800;

  public CriterionZodiacSign(ActivePoint activePoint, int signs, boolean isFlags, String color) {
    super(Criterion.TYPE_ZODIAC_SIGN, activePoint, color);

    int position = 0;
    if (isFlags) {
      int mask = SIGN_ARIES;
      for (int i = 0; i < 12; i++) {
        if ((mask & signs) > 0) {
          for (int z = 0; z < 30; z++) {
            this.setMark(position + z, 1);
          }
        }
        position += 30;
        mask <<= 1;
      }
    } else {
      position = (signs - Text.getId("Aries")) * 30;
      for (int z = 0; z < 30; z++) {
        this.setMark(position + z, 1);
      }
    }
  }

  public int getFactor() {
    int aries = Text.getId("Aries");
    for (int i = 0; i < 360; i += 30) {
      if (this.getMark(i) > 0) {
        return (aries + (i / 30));
      }
    }
    return 0;
  }

}
