package astrolab.web.resource;

import java.util.ArrayList;
import java.util.Iterator;

public class Resources {

  private static ArrayList<CloseableResource> list = new ArrayList<CloseableResource>();

  public static synchronized void add(CloseableResource resource) {
    list.add(resource);
  }

  public static synchronized void closeAll() {
    Iterator<CloseableResource> iterator = list.iterator();

    while (iterator.hasNext()) {
      iterator.next().close();
    }

    list.clear();
  }
}