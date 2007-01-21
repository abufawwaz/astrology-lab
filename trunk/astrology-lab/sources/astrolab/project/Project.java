package astrolab.project;

import java.sql.Timestamp;

import astrolab.astronom.planet.Planet;
import astrolab.astronom.planet.SolarSystem;
import astrolab.db.Database;
import astrolab.project.statistics.InMemoryEvent;

public class Project {

  final static String TABLE_PREFIX = "project_";

  private String name;
  private ProjectDataKey[] keys;

  public Project(String name) {
    this.name = name;
    listKeys();
  }

  public String getName() {
    return name;
  }

  public ProjectDataKey[] getKeys() {
    return keys;
  }

  public ProjectData getData() {
    return new ProjectData(this);
  }

  public ProjectData getData(String[] formula, String base) {
    checkKey(base);
    for (int f = 0; f < formula.length; f++) {
      checkKey(formula[f]);
    }
    return new ProjectData(this, formula, base);
  }

  private void listKeys() {
    String[] keyStrings = Database.queryList("SHOW FIELDS FROM " + TABLE_PREFIX + name);
    keys = new ProjectDataKey[keyStrings.length];
    for (int i = 0; i < keys.length; i++) {
      keys[i] = new ProjectDataKey(keyStrings[i], null);
    }
  }

  private void checkKey(String key) {
    if (key.indexOf(' ') >= 0 || key.indexOf('(') >= 0) {
      // TODO: find atoms more precisely!
      return;
    }

    boolean found = false;
    for (int k = 0; k < keys.length; k++) {
      if (key.equalsIgnoreCase(keys[k].getName())) {
        found = true;
        break;
      }
    }
    if (!found) {
      addKey(key);
    }
  }

  private void addKey(String key) {
    double position;
    SolarSystem solar = new SolarSystem();
    Planet earth = solar.getPlanet(SolarSystem.EARTH);
    ProjectData data = new ProjectData(this);

    if (data.begin()) {
      Database.execute("ALTER TABLE " + TABLE_PREFIX + name + " ADD COLUMN " + key + " DOUBLE");

      do {
        String sqltimestamp = new Timestamp(data.getTime().getTimeInMillis()).toString();
        solar.calculate(new InMemoryEvent(data.getTime())); // TODO: calculate based on time!
        position = solar.getPlanet(key).positionAround(earth);
        Database.execute("UPDATE " + TABLE_PREFIX + name + " SET " + key + " = " + position + " WHERE time = '" + sqltimestamp + "'");
      } while (data.move());
    }

    listKeys();
  }

}