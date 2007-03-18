package astrolab.project.geography;

import java.sql.ResultSet;

import astrolab.db.Database;
import astrolab.db.Personalize;
import astrolab.db.RecordIterator;

public class LocationIterator extends RecordIterator {

  private LocationIterator(ResultSet set) {
    super(set);
  }

  public static LocationIterator iterate(String like) {
    String query = "SELECT DISTINCT(" + ProjectGeography.TABLE_NAME + ".id) FROM " + ProjectGeography.TABLE_NAME + ", text" +
      " WHERE (text.en LIKE '%" + like + "%' OR text.bg LIKE '%" + like + "%')" +
      " AND " + ProjectGeography.TABLE_NAME + ".id = text.id";

    return new LocationIterator(Database.executeQuery(query));
  }

  public static LocationIterator iterate(int region) {
    String QUERY = "SELECT " + ProjectGeography.TABLE_NAME + ".id FROM " + ProjectGeography.TABLE_NAME + ", text WHERE " + ProjectGeography.TABLE_NAME + ".id = text.id AND " + ProjectGeography.TABLE_NAME + ".id > 0 AND region = " + region + " ORDER BY text." + Personalize.getLanguage();

    return new LocationIterator(Database.executeQuery(QUERY));
  }

  public static LocationIterator iterateSubRegions(int region) {
    String QUERY = "SELECT DISTINCT(" + ProjectGeography.TABLE_NAME + ".id) FROM " + ProjectGeography.TABLE_NAME + ", text LEFT JOIN (SELECT " + ProjectGeography.TABLE_NAME + ".id as l1_id, " + ProjectGeography.TABLE_NAME + ".region as l1_reg from " + ProjectGeography.TABLE_NAME + " GROUP BY l1_id) AS TABLE1 ON text.id = TABLE1.l1_reg WHERE TABLE1.l1_id IS NOT NULL AND " + ProjectGeography.TABLE_NAME + ".id = text.id AND " + ProjectGeography.TABLE_NAME + ".id > 0 AND " + ProjectGeography.TABLE_NAME + ".region = " + region + " ORDER BY text." + Personalize.getLanguage();

    return new LocationIterator(Database.executeQuery(QUERY));
  }

  protected Object read() throws Exception {
    int location = set.getInt(1);
    
    return Location.getLocation(location);
  }

}