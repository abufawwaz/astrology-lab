package astrolab.perspective.classmates;

import java.io.File;
import java.io.InputStream;

import astrolab.web.display.JPGDisplay;
import astrolab.web.server.Request;

public class DisplayClassmateImage extends JPGDisplay {

  private final static String PARAMETER_ID = "classmate";

  protected InputStream getFileStream() {
    int classmateId = Request.getCurrentRequest().getParameters().getInt(PARAMETER_ID);
    String packageName = (getClass().getPackage().getName() + ".img.").replace('.', File.separatorChar);
    return this.getClass().getClassLoader().getResourceAsStream(packageName + "classmate" + classmateId + ".jpg");
  }

}