package astrolab.web.project.finance.products;

import astrolab.web.component.tree.ComponentTree;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentSelectProduct extends ComponentTree {

  public static void fill(LocalizedStringBuffer buffer) {
    ComponentTree.fill(buffer, new DisplayProductList());
  }

  public static int retrieve(Request request) {
    return Integer.parseInt(request.get(new DisplayProductList().getChoiceId()));
  }

}
