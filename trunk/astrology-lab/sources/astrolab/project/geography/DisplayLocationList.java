package astrolab.project.geography;

import astrolab.web.HTMLDisplay;
import astrolab.web.component.tree.DisplayTree;
import astrolab.web.component.tree.TreeObject;

public class DisplayLocationList extends DisplayTree {

  public DisplayLocationList() {
    super(HTMLDisplay.getId(DisplayLocationList.class));
  }

  public TreeObject getRoot() {
    return Location.getLocation(0);
  }

}