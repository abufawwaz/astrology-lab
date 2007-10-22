package astrolab.web.display;

import java.io.UnsupportedEncodingException;

import astrolab.web.Display;
import astrolab.web.server.content.LocalizedStringBuffer;

public abstract class AJAXDataResponse extends Display {

  public byte[] getContent() {
    LocalizedStringBuffer buffer = new LocalizedStringBuffer();

    fillData(buffer);

    try {
      return buffer.toString().getBytes("UTF-8");
    } catch (UnsupportedEncodingException uee) {
      return buffer.toString().getBytes();
    }
  }

  public String getType() {
    return "ajax/text";
  }

  protected abstract void fillData(LocalizedStringBuffer buffer);

}
