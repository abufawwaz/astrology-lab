package astrolab.web.project.finance.products;

import astrolab.db.Database;
import astrolab.db.RecordIterator;
import astrolab.db.Text;
import astrolab.web.component.tree.TreeObject;

public class Product implements TreeObject {

  private int id;
  private int category;

  public Product(int id) {
    if (id != 0) {
      String data = Database.query("SELECT product_category FROM project_financial_products WHERE product_id = " + id);
      this.id = id;
      this.category = Integer.parseInt(data);
    } else {
      this.id = 0;
      this.category = 0;
    }
  }

  Product(int id, int category) {
    this.id = id;
    this.category = category;
  }

  public int getId() {
    return id;
  }

  public int getCategory() {
    return category;
  }

  public String toString() {
    return Text.getText(id);
  }

  public String toFullString() {
    if (category > 0) {
      StringBuffer buffer = new StringBuffer();
      buffer.append(Text.getText(category));
      buffer.append(" ");
      buffer.append(Text.getText(id));
      return buffer.toString();
    } else {
      return Text.getText(id);
    }
  }

  public TreeObject getById(int id) {
    return new Product(id);
  }

  public String getText(boolean toSelect) {
    return toString();
  }

  public TreeObject getParent() {
    return new Product(getCategory());
  }

  public RecordIterator iterateChildren() {
    return ProductIterator.iterateCategory(getId());
  }

  public RecordIterator iterateSubTrees() {
    return ProductIterator.iterateSubCategories(getId());
  }

  protected static void store(String name, int category) {
    int id = Text.getId(name);
    if (id <= 0) {
      id = Text.reserve(name, Text.TYPE_PRODUCTS);
      Database.execute("INSERT INTO project_financial_products VALUES (" + id + ", " + category + ")");
    } else {
      Database.execute("UPDATE project_financial_products SET product_category = " + category + " WHERE product_id = " + id);
    }
  }

}