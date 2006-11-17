package astrolab;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import astrolab.web.server.Connection;

public class Ping {

  static byte[] CONTENTS;

  public static void main(String[] args) throws Exception {
    ServerSocket server = new ServerSocket(8080);
    while (true) {
      process(server.accept());
    }
  }

  public static void process(Socket socket) throws Exception {
    ServerSocket server = new ServerSocket(8080);
    while (true) {
      process(server.accept());
    }
  }

  static {
    try {
      FileInputStream fis = new FileInputStream(new File("wedding.html"));
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
}
