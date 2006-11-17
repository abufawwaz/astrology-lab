package astrolab.web.project.finance.products;

import java.sql.ResultSet;

import astrolab.db.Database;
import astrolab.db.Personalize;
import astrolab.db.RecordIterator;

public class ProductIterator extends RecordIterator {

  private ProductIterator(ResultSet set) {
    super(set);
  }

  public static ProductIterator iterateCategory(int category) {
    String QUERY = "SELECT project_financial_products.product_id, project_financial_products.product_category FROM project_financial_products, text WHERE project_financial_products.product_id = text.id AND project_financial_products.product_id > 0 AND product_category = " + category + " ORDER BY text." + Personalize.getLanguage();

    return new ProductIterator(Database.executeQuery(QUERY));
  }

  public static ProductIterator iterateSubCategories(int category) {
    String QUERY = "SELECT DISTINCT(project_financial_products.product_id), project_financial_products.product_category FROM project_financial_products, text LEFT JOIN (SELECT project_financial_products.product_id as l1_id, project_financial_products.product_category as l1_reg from project_financial_products GROUP BY l1_id) AS TABLE1 ON id = TABLE1.l1_reg WHERE TABLE1.l1_id IS NOT NULL AND project_financial_products.product_id = text.id AND project_financial_products.product_id > 0 AND project_financial_products.product_category = " + category + " ORDER BY text." + Personalize.getLanguage();

    return new ProductIterator(Database.executeQuery(QUERY));
  }

  protected Object read() throws Exception {
    int product = set.getInt(1);
    int category = set.getInt(2);
    
    return new Product(product, category);
  }

  //SELECT DISTINCT(region) FROM locations
  //LEFT JOIN (SELECT locations.id as l1_id, locations.region as l1_reg from locations GROUP BY l1_id) AS TABLE1
  //ON locations.id = TABLE1.l1_reg
}