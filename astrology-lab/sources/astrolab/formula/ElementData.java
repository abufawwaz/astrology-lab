package astrolab.formula;

import java.util.Calendar;
import java.util.Date;

import astrolab.astronom.Place;
import astrolab.astronom.houses.HouseSystem;
import astrolab.astronom.houses.PlacidusSystem;
import astrolab.astronom.planet.Planet;
import astrolab.astronom.planet.SolarSystem;

public class ElementData {

  private int target;
  private Date date;
  private double[] values = new double[Element.getElements().length];

  public ElementData(Date date, int target) {
//    this.date = date;
//    this.target = target;
//
//    Calendar c = Calendar.getInstance();
//    c.setTime(date);
//
//    Place sz = Place.getPlace("Earth", "Bulgaria", "Sofia");
//
//    Location location = new Location();
//    location.setTime(c);
//    location.setPlace(sz);
//    SolarSystem solar = new SolarSystem();
//    Planet center = solar.getPlanet(location.getPlace().getPlanet());
////    Planet center = solar.getPlanet(SolarSystem.SUN);
//    solar.calculate(location.getStandardYearTime());
//
//    HouseSystem houses = new PlacidusSystem(location);
//    values[Element.HOUSE_ASC.getIndex()] = houses.getHouse(1);
//    values[Element.HOUSE_2.getIndex()] = houses.getHouse(2);
//    values[Element.HOUSE_3.getIndex()] = houses.getHouse(3);
//    values[Element.HOUSE_4.getIndex()] = houses.getHouse(4);
//    values[Element.HOUSE_5.getIndex()] = houses.getHouse(5);
//    values[Element.HOUSE_6.getIndex()] = houses.getHouse(6);
//    values[Element.PLANET_SUN.getIndex()] = solar.getPlanet(SolarSystem.SUN).positionAround(center);
////    values[Element.PLANET_SUN.getIndex()] = solar.getPlanet(SolarSystem.EARTH).positionAround(center);
//    values[Element.PLANET_MOON.getIndex()] = solar.getPlanet(SolarSystem.MOON).positionAround(center);
//    values[Element.PLANET_MERCURY.getIndex()] = solar.getPlanet(SolarSystem.MERCURY).positionAround(center);
//    values[Element.PLANET_VENUS.getIndex()] = solar.getPlanet(SolarSystem.VENUS).positionAround(center);
//    values[Element.PLANET_MARS.getIndex()] = solar.getPlanet(SolarSystem.MARS).positionAround(center);
//    values[Element.PLANET_JUPITER.getIndex()] = solar.getPlanet(SolarSystem.JUPITER).positionAround(center);
//    values[Element.PLANET_SATURN.getIndex()] = solar.getPlanet(SolarSystem.SATURN).positionAround(center);
  }

  public double getValue(Element element) {
    return values[element.getIndex()];
  }

  public int getTarget() {
    return target;
  }

  public Date getDate() {
    return date;
  }

}