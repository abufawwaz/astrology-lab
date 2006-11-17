package astrolab.web.server;

import java.io.*;
import java.net.*;

public class Server {

  private static int port = 80;

  public void run() throws IOException {
    ServerSocket server = new ServerSocket(port);
    while (true) {
      new Connection(server.accept());
    }
  }

  public static void main(String[] args) throws IOException {
    try {
      port = Integer.parseInt(args[args.length - 1]);
    } catch (Exception e) {
      // no port specified
    }

    if (!argument("-nolog", args)) {
      System.out.println("Log directed to 'out.txt'.");
    	PrintStream out = new PrintStream(new FileOutputStream("out.txt"), true);
    	System.setOut(out);
    	System.setErr(out);
    }
    System.out.println("Starting server on port " + port);
    new Server().run();
  }

  private static boolean argument(String arg, String[] args) {
    for (int i = 0; i < args.length; i++) {
      if (arg.equals(args[i])) {
        return true;
      }
    }
    return false;
  }

}