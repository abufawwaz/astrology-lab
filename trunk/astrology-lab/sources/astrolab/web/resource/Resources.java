package astrolab.web.resource;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class Resources extends ReferenceQueue<CloseableResource> implements Runnable {

//  private static ArrayList<WeakReference<CloseableResource>> list = new ArrayList<WeakReference<CloseableResource>>();
//  private static Resources resources = new Resources();
//
//  private Resources() {
//    new Thread("Resource Closer").start();
//  }
//
//  public static synchronized void add(CloseableResource resource) {
//    list.add(new WeakReference<CloseableResource>(resource, resources));
//  }

  public void run() {
//    while (true) {
//      try {
//        CloseableResource resource = remove().get();
//        System.out.println(" close: " + resource);
//        resource.close();
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//        return;
//      } catch (Exception e) {
//        e.printStackTrace();
//      }
//    }
  }

}