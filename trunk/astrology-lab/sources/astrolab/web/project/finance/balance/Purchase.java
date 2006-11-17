package astrolab.web.project.finance.balance;

import java.sql.Timestamp;

import astrolab.astronom.Time;
import astrolab.db.Database;
import astrolab.db.Text;

public class Purchase {

  private int owner;
  private int item_id;
  private int type;
  private double price;
  private double quantity;
  private Time time;
  private String currency;
  private String operation;
  private String measure;

  Purchase(int owner, String operation, int item_id, int type, Time time, double price, String currency, double quantity, String measure) {
    this.owner = owner;
    this.operation = operation;
    this.item_id = item_id;
    this.type = type;
    this.price = price;
    this.currency = currency;
    this.quantity = quantity;
    this.time = time;
    this.measure = measure;
  }

  public int getItemId() {
    return item_id;
  }

  public String getMeasure() {
    return measure;
  }

  public String getCurrency() {
    return currency;
  }

  public String getOperation() {
    return operation;
  }

  public int getOwner() {
    return owner;
  }

  public double getPrice() {
    return price;
  }

  public double getQuantity() {
    return quantity;
  }

  public int getType() {
    return type;
  }

  public Time getTime() {
    return time;
  }

  public String toString() {
    return Text.getText(owner) + " " + operation + " " + quantity + " " + Text.getText(type) + " at " + price;
  }

  public String toFullString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append(Text.getText(type));
    buffer.append(" ");
    buffer.append(getPrice());
    buffer.append(" ");
    buffer.append(getQuantity());
    buffer.append(")");
    return buffer.toString();
  }

  public static void store(int owner, String operation, int item_id, int item_type, double price, String currency, double quantity, String measure, long timestamp) {
    String sqltimestamp = new Timestamp(timestamp).toString();
    Database.execute("INSERT INTO project_financial_balance VALUES (" + owner + ", '" + operation + "', " + item_id + ", " + item_type + ", '" + sqltimestamp + "', " + price + ", '" + currency + "', " + quantity + ", '" + measure + "')");
  }

}