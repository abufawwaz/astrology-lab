package astrolab.web.server;

import java.io.*;
import java.util.Properties;

import astrolab.db.Personalize;

public class ConnectionInput extends Thread {

  private Connection connection;
  private LineNumberReader in;
  private Properties headers = new Properties();

  ConnectionInput(InputStream in, Connection connection) {
    setName("client-" + hashCode());
    this.connection = connection;
    this.in = new LineNumberReader(new InputStreamReader(in));

    start();
  }

  public void run() {
  	Request request;
    System.out.println("Connection input parsed by " + Thread.currentThread());
    Personalize.clear();
    try {
      while (true) {
        // read an HTTP request
        int user;
      	String headline = in.readLine();
        String line;
        String content = null;

        headers = new Properties();
        do {
          line = in.readLine();
          if (line == null) {
            System.out.println("[Client]: EOF");
            return;
          }
          int colon = line.indexOf(':');
          if (colon > 0) {
            headers.setProperty(line.substring(0, colon), line.substring(colon + 1).trim());
          }

          System.out.println("[Client]: " + line);
        } while ((line != null) && (line.length() > 0));

        int contentLength = Integer.parseInt(headers.getProperty("Content-Length", "0"));
        if (contentLength > 0) {
          char[] contentArray = new char[contentLength];
          in.read(contentArray);
          content = new String(contentArray);
        }

        user = Integer.parseInt(getCookie("session", "-1"));
        if (user > 0) {
          Personalize.set(user, headers.getProperty("Accept-Language"));
        }
        request = process(headline, headers, user, content);
        if (request == null) {
        	break;
        }
      }
    } catch (Exception e) {
      System.err.println("[Client]: " + e);
      e.printStackTrace();
    }
  }

  public String getCookie(String name, String defaultValue) {
    String cookies = headers.getProperty("Cookie");
    if (cookies == null) {
      return defaultValue;
    }
    int index1 = cookies.indexOf(name);
    if (index1 < 0) {
      return defaultValue;
    }
    int index2 = cookies.indexOf('=', index1);
    int index3 = cookies.indexOf(';', index2);
    return (index3 > 0) ? cookies.substring(index2 + 1, index3).trim() : cookies.substring(index2 + 1).trim();
  }

  private Request process(String request, Properties headers, int user, String content) {
	  if (request == null) return null;
    System.out.println("[Client]: " + request);
    int index = request.indexOf(" ");
    if (request.charAt(index + 1) == '/') {
      index++;
    }
    String transforms = request.substring(index + 1, request.lastIndexOf(" ")).trim();
    if (content != null) {
      transforms += ((transforms.indexOf('?') >= 0) ? "&" : "?") + content;
    }
    return connection.getProcessor().process(transforms, headers, user);
  }

}