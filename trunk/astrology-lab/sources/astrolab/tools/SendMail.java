package astrolab.tools;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.net.Socket;

import astrolab.db.User;

public class SendMail {

  private final static String SMPT_SERVER = "mail.astrology-lab.net";
//  private final static String SMPT_SERVER = "mail.ekk.bg";

  public static boolean send(int receiverId, String to, String subject, String content) {
    try {
      System.out.println("[TO " + SMPT_SERVER + "]: connecting to SMTP server " + SMPT_SERVER + ":25");
      Socket socket = new Socket(SMPT_SERVER, 25);
      InputStream in = socket.getInputStream();
      LineNumberReader reader = new LineNumberReader(new InputStreamReader(in));
      OutputStream out = socket.getOutputStream();
  
      send(out, reader, "EHLO astrology-lab.net");
      send(out, reader, "MAIL FROM:<system@astrology-lab.net>");
      send(out, reader, "RCPT TO:<" + to + ">");
      send(out, reader, "DATA");
      send(out, reader, "From: www.astrology-lab.net <system@astrology-lab.net>\r\n" +
                        "To: " + to + "\r\n" +
                        "Subject: " + subject + "\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: " + content.length() + "\r\n" +
                        "\r\n" +
                        content + "\r\n" +
                        ".");
      send(out, reader, "QUIT");
  
      while (true) {
        String line;
        if ((line = reader.readLine()) != null) {
          System.out.println("[FROM " + SMPT_SERVER + "]: " + line);
        } else {
          System.out.println("[FROM " + SMPT_SERVER + "] Bye.");
          break;
        }
      }

      User.updateSentInvitation(receiverId, to);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  private final static void send(OutputStream out, LineNumberReader reader, String message) throws Exception {
    System.out.println("[TO " + SMPT_SERVER + "]: " + message);
    out.write(message.getBytes("UTF-8"));
    out.write("\r\n".getBytes());
    out.flush();
 
    String line;
    while (true) {
      line = reader.readLine();
      if (line != null) {
        System.out.println("[FROM " + SMPT_SERVER + "]: " + line);
        if ((line.length() <= 3) || (line.charAt(3) == ' ')) {
          break;
        }
      } else {
        break;
      }
    }
  }
}
