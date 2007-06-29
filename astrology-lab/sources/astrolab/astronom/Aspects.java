package astrolab.astronom;

import astrolab.db.Text;

public class Aspects {

  public final static int CONJUNCT = Text.getId("aspect.0");
  public final static int SEXTILE = Text.getId("aspect.60");
  public final static int SQUARE = Text.getId("aspect.90");
  public final static int TRINE = Text.getId("aspect.120");
  public final static int OPPOSITION = Text.getId("aspect.180");

  public final static int[] MAJOR_ASPECTS = {
    CONJUNCT, SEXTILE, SQUARE, TRINE, OPPOSITION
  };

}