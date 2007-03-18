package astrolab.web.project.finance.balance;

import java.sql.ResultSet;
import java.util.Date;

import astrolab.astronom.Time;
import astrolab.db.Database;
import astrolab.db.RecordIterator;
import astrolab.project.geography.Location;
import astrolab.web.project.archive.relocation.RelocationRecord;

public class PurchaseIterator extends RecordIterator {

  private PurchaseIterator(ResultSet set) {
    super(set);
  }

  public static PurchaseIterator iterate(int owner) {
    String QUERY = "SELECT * FROM project_financial_balance text WHERE owner_id = " + owner + " ORDER BY purchase_time";

    return new PurchaseIterator(Database.executeQuery(QUERY));
  }

  protected Object read() throws Exception {
    int owner = set.getInt(1);
    String operation = set.getString(2);
    int item_id = set.getInt(3);
    int item_type = set.getInt(4);
    Date date = set.getTimestamp(5);
    double price = set.getDouble(6);
    String currency = set.getString(7);
    double quantity = set.getDouble(8);
    String measure = set.getString(9);

    int location = RelocationRecord.getLocationOfPerson(owner, new Time(date).getTimeInMillis());
    Time timestamp = new Time(date.getTime(), Location.getLocation(location).getTimeZone());

    return new Purchase(owner, operation, item_id, item_type, timestamp, price, currency, quantity, measure);
  }

}