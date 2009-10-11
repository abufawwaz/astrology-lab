package open.osiva.core.view.html;

import open.osiva.core.controller.Controller;
import open.osiva.core.controller.ControllerCollection;
import open.osiva.core.view.PageSourceBuilder;
import open.osiva.core.view.View;

public class ReadonlyCollectionView implements View {

  private ControllerCollection controller;

  public ReadonlyCollectionView(ControllerCollection controller) {
    this.controller = controller;
  }

  public void populateView(PageSourceBuilder page) {
    page.startLine("<table border='1' width='100%'>");
    page.goIn();

    for (Controller child: controller.children()) {
      page.startLine("<tr><td>");
      page.goIn();
      child.getView().populateView(page);
      page.goOut();
      page.startLine("</td></tr>");
    }
    page.goOut();
    page.startLine("</table>");
  }

}