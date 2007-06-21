/**

 //koch
 function koch(ra,ob,la) {
 a1=FNasn(Math.sin(ra)*Math.tan(la)*Math.tan(ob))

 for (i=1; i < 13; i++) {
 d=FNmod(60+30*i)
 a2=d/90-1;kn=1
 if (d>=180) {kn=-1;a2=d/90-3}
 a3=FNrad(FNmod(FNdeg(ra)+d+a2*FNdeg(a1)))
 x=Math.atan(Math.sin(a3)/(Math.cos(a3)*Math.cos(ob)-kn*Math.tan(la)*Math.sin(ob)))

 if (x<0) {x=x+Math.PI}

 if (Math.sin(a3)<0) {x=x+Math.PI}

 house[i]=FNmod(FNdeg(x))
 }

 }

 **/
package astrolab.astronom.houses;

import astrolab.astronom.SpacetimeEvent;
import astrolab.astronom.util.Trigonometry;
import astrolab.astronom.util.Zodiac;

public class KochSystem extends HouseSystem {

  public KochSystem(SpacetimeEvent event) {
    super(event);
  }

  public double getHouse(int number) {
    double a1 = Trigonometry.asn(Math.sin(event.getRa() * Math.tan(Trigonometry.radians(event.getLocation().getLattitude()) * Math.tan(event.getOb()))));

    double d = Zodiac.degree(60 + 30 * number);
    double a2 = d / 90 - 1;
    int kn = 1;

    if (d >= 180) {
      kn = -1;
      a2 = d / 90 - 3;
    }

    double a3 = Trigonometry.radians(Zodiac.degree((Trigonometry.degrees(event.getRa()) + d + a2 * Trigonometry.degrees(a1))));
    double x = Math.atan(Math.sin(a3) / (Math.cos(a3) * Math.cos(event.getOb()) - kn * Math.tan(Trigonometry.radians(event.getLocation().getLattitude())) * Math.sin(event.getOb())));

    if (x < 0) {
      x += Math.PI;
    }

    if (Math.sin(a3) < 0) {
      x += Math.PI;
    }

    return Zodiac.degree(Trigonometry.degrees(x));
  }

}