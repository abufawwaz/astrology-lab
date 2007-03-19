package astrolab.project.geography;

import astrolab.web.component.tree.ComponentTree;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class ComponentSelectLocation extends ComponentTree {

  public static void fill(LocalizedStringBuffer buffer) {
    ComponentTree.fill(buffer, new DisplayLocationList());
  }

  public static void fill(LocalizedStringBuffer buffer, Location location) {
    ComponentTree.fill(buffer, new DisplayLocationList(), location);
  }

  public static int retrieve(Request request) {
    return Integer.parseInt(request.get(new DisplayLocationList().getChoiceId()));
  }

}
