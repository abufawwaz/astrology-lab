package astrolab.web.project.finance.products;

import astrolab.web.HTMLDisplay;
import astrolab.web.component.tree.DisplayTree;
import astrolab.web.component.tree.TreeObject;

public class DisplayProductList extends DisplayTree {

  public DisplayProductList() {
    super(HTMLDisplay.getId(DisplayProductList.class));
  }

  public TreeObject getRoot() {
    return new Product(0);
  }

}