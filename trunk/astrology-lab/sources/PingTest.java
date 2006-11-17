import java.io.OutputStream;
import java.net.*;

public class PingTest {

  public static void main(String[] args) throws Exception {
    final byte[] CRLF = "\r\n".getBytes();
    final String responseText = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head><body><h1>Text-az / Текст-ая</h1></body></html>";

    System.out.println(" starting a server socket on port 8888 ...");
    ServerSocket server = new ServerSocket(8888);
    Socket s;
    while (true) {
      s = server.accept();
      System.out.println(" accepted socket: " + s);

      OutputStream out = s.getOutputStream();

      byte[] response = responseText.getBytes("UTF-8");
      out.write("HTTP/1.1 200 OK".getBytes());
      out.write(CRLF);
      out.write("Content-Length: ".getBytes());
      out.write((String.valueOf(response.length)).getBytes());
      out.write(CRLF);
      out.write(CRLF);
      out.write(response);
      out.flush();
    }
  }
}
