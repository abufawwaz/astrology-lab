package astrolab.web.server.content;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import astrolab.web.server.Request;
import astrolab.web.server.RequestParameters;
import astrolab.web.server.Response;

public class StaticPage extends Request implements Response {

	private byte[] CONTENTS = new byte[0];
  private String filename;
  private String type;

  public StaticPage(String filename, String type) {
    super(null, 0, new RequestParameters("GET " + filename + " HTTP/1.1"));

    this.filename = filename;
    this.type = type;

    try {
      FileInputStream fis = new FileInputStream(new File(filename));
      int read;
      byte[] data = new byte[500];
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      while (fis.available() > 0) {
        read = fis.read(data);
        baos.write(data, 0, read);
      }
      CONTENTS = baos.toByteArray();
      fis.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Response getResponse() {
    return this;
  }

	public byte[] getBytes() {
		return CONTENTS;
	}

	public String getType() {
		return type;
	}

	public String toString() {
		return "Static page: " + filename;
	}

}
