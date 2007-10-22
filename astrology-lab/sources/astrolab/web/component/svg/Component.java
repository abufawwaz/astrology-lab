package astrolab.web.component.svg;

import astrolab.web.server.content.LocalizedStringBuffer;

public interface Component {

  public String getId();

  public void fill(LocalizedStringBuffer buffer);

}