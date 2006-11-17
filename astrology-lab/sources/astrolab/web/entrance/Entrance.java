package astrolab.web.entrance;

import astrolab.web.server.Request;

public interface Entrance {

  public boolean isApplicable(Request request);

  public void modify(Request request);

}
