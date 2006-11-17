package astrolab.db;

import java.sql.ResultSet;

public class LocationIterator extends RecordIterator {

//  private final static String QUERY_REGION = "SELECT id FROM locations WHERE id > 0 AND region = ";
//  private final static String QUERY_SUBREGIONS = "SELECT DISTINCT(id) FROM locations LEFT JOIN (SELECT locations.id as l1_id, locations.region as l1_reg from locations GROUP BY l1_id) AS TABLE1 ON locations.id = TABLE1.l1_reg WHERE TABLE1.l1_id IS NOT NULL AND locations.id > 0 AND locations.region = ";

  private LocationIterator(ResultSet set) {
    super(set);
  }

  public static LocationIterator iterate(int region) {
    String QUERY = "SELECT locations.id FROM locations, text WHERE locations.id = text.id AND locations.id > 0 AND region = " + region + " ORDER BY text." + Personalize.getLanguage();

    return new LocationIterator(Database.executeQuery(QUERY));
  }

  public static LocationIterator iterateSubRegions(int region) {
    String QUERY = "SELECT DISTINCT(locations.id) FROM locations, text LEFT JOIN (SELECT locations.id as l1_id, locations.region as l1_reg from locations GROUP BY l1_id) AS TABLE1 ON text.id = TABLE1.l1_reg WHERE TABLE1.l1_id IS NOT NULL AND locations.id = text.id AND locations.id > 0 AND locations.region = " + region + " ORDER BY text." + Personalize.getLanguage();

    return new LocationIterator(Database.executeQuery(QUERY));
  }

  protected Object read() throws Exception {
    int location = set.getInt(1);
    
    return Location.getLocation(location);
  }

  //SELECT DISTINCT(region) FROM locations
  //LEFT JOIN (SELECT locations.id as l1_id, locations.region as l1_reg from locations GROUP BY l1_id) AS TABLE1
  //ON locations.id = TABLE1.l1_reg
}