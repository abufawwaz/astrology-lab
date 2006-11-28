package astrolab.web.server;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class ConnectionOutput {

  private OutputStream out;
  private String cookies = null;

  ConnectionOutput(OutputStream out) {
    this.out = out;
  }

  public void setCookie(String key, String value) {
    cookies = "Set-Cookie: " + key + "=" + value + "; expires=Sun, 2-Jan-2050 01:00:00 GMT";
  }

  public void respond(Request request) {
    final byte[] CRLF = "\r\n".getBytes();
    byte[] response = request.getResponse().getBytes();
    int length = response.length;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    try {
      baos.write("HTTP/1.1 200 OK".getBytes());
      baos.write(CRLF);
      if (request.getResponse().getType() != null) {
        baos.write("Content-Type: ".getBytes());
        baos.write(request.getResponse().getType().getBytes());
        baos.write(CRLF);
      }
      baos.write("Content-Length: ".getBytes());
      baos.write((String.valueOf(length)).getBytes());
      baos.write(CRLF);
      if (cookies != null) {
        baos.write(cookies.getBytes());
        baos.write(CRLF);
      }
      baos.write(CRLF);
      baos.write(response);

//System.out.println("[Server]: ====================\r\n" + baos.toString() + "\r\n===================");

      out.write(baos.toByteArray());
      out.flush();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}