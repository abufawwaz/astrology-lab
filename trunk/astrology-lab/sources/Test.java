import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.net.*;
import java.util.Calendar;
import java.util.TimeZone;

public class Test extends Thread {
  
  private static String FILENAME = "table.txt";
  private static String CONTENTS = "";
  
  static {
    try {
      byte[] BYTES_CONTENTS;
      InputStream fis = Test.class.getClassLoader().getResourceAsStream(FILENAME);
      int read;
      byte[] data = new byte[500];
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      while (fis.available() > 0) {
        read = fis.read(data);
        baos.write(data, 0, read);
      }
      BYTES_CONTENTS = baos.toByteArray();
      fis.close();
  
      CONTENTS = new String(BYTES_CONTENTS);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws Exception {
    boolean skip = false;
    boolean command = false;
    int cellsize = 0;
    int length = CONTENTS.length();
    for (int i = 0; i < length; i++) {
      char c = CONTENTS.charAt(i);

      switch (c) {
      case '<': {
        skip = true;
        command = true;
        break;
      }
      case '>': {
        skip = false;
        break;
      }
      default: {
        if (!skip) {
          System.out.print(c);
          cellsize++;
        } else if (command) {
          switch (c) {
          case 'f': {
            command = false;
            break;
          }
          case '/': {
            command = false;
            break;
          }
          case 'r': {
            System.out.println();
            command = false;
            break;
          }
          case 'd':
          case 'h': {
            for (int z = cellsize; z < 8; z++) {
              System.out.print(" ");
            }
            System.out.print("| ");
            cellsize = 0;
            command = false;
            break;
          }
          }
        }
      }
      }
    }
  }

  public static void main3(String[] args) throws Exception {
    System.out.println(" daylight savings ... ");
    Calendar calendar = Calendar.getInstance();
    System.out.println(" current instance: " + calendar);
    System.out.println(" time zone: " + calendar.getTimeZone());
    System.out.println(" time zone: '" + calendar.getTimeZone().getID() + "'");

    System.out.println(" GMT  time zone: " + Calendar.getInstance(TimeZone.getTimeZone("GMT")));
    System.out.println(" GMTO time zone: " + Calendar.getInstance(TimeZone.getTimeZone("GMTO")));

//    String[] zones = TimeZone.getAvailableIDs();
//    for (int i = 0; i < zones.length; i++) {
//      TimeZone zone = TimeZone.getTimeZone(zones[i]);
//      calendar = Calendar.getInstance(zone);
//      System.out.println(" [" + i + "]: " + zones[i] + " -> " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
//    }
  }

  private static LineNumberReader reader;
  private static OutputStream out;

  public static void send(String message) throws Exception {
    System.out.println("[Client]: " + message);
    out.write(message.getBytes());
    out.write("\r\n".getBytes());
    out.flush();

    String line;
    while (true) {
      System.out.println(" ... read ");
      line = reader.readLine();
      System.out.println(" ... read : " + line);
      if (line != null) {
        System.out.println("[Server]: " + line);
        if ((line.length() <= 3) || (line.charAt(3) == ' ')) {
          break;
        }
      } else {
        break;
      }
    }
    System.out.println(" ... ");
  }

  public static void main2(String[] args) throws Exception {
    System.out.println(" starting a socket on port 25");
    Socket socket = new Socket("mail.astrology-lab.net", 25);
    out = socket.getOutputStream();
    reader = new LineNumberReader(new InputStreamReader(socket.getInputStream()));
    System.out.println(" accepted socket: " + socket);

    String text = "<html><body><center>Keep this e-mail and use it to enter <b><i>www.</i></b><b><i>astrology-lab</i></b><b><i>.net</i></b> with your profile!</center><br><br><center><form method='post' action='http://www.astrology-lab.net'><input type='hidden' name='session' value='2000001'><input type='submit' value='Enter www.astrology-lab.net'></form></center><br><br></body></html>";

    send("EHLO astrology-lab.net");
    send("MAIL FROM: <system@astrology-lab.net>");
    send("RCPT TO:<stephan.zlatarev@gmail.com>");
    send("DATA");
    send("From: www.astrology-lab.net <system@astrology-lab.net>\r\n" +
         "To: stephan.zlatarev@gmail.com\r\n" +
         "Subject: Stephan, Your registration at www.astrology-lab.net has been successful!\r\n" +
         "Content-Type: text/html\r\n" +
         "Content-Length: " + text.length() + "\r\n" +
         "\r\n" +
         text + "\r\n" +
         ".");
    send("QUIT");
  }

  public static void main1(String[] args) throws Exception {
    System.out.println(" starting a server socket on port specified by first argument ...");
    ServerSocket server = new ServerSocket(Integer.parseInt(args[0]));
    Socket s = server.accept();
    System.out.println(" accepted socket: " + s);
  }
}
