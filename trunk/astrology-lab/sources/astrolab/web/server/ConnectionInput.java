package astrolab.web.server;

import java.io.*;
import java.net.SocketException;

import astrolab.db.Personalize;
import astrolab.tools.Log;

public class ConnectionInput extends Thread {

  private Connection connection;
  private LineNumberReader in;
  private RequestParameters parameters;

  ConnectionInput(InputStream in, Connection connection) {
    setName("client-" + hashCode());
    this.connection = connection;
    this.in = new LineNumberReader(new InputStreamReader(in));

    start();
  }

  public void run() {
  	Request request;
    Log.log("Connection input parsed by " + Thread.currentThread());
    Personalize.clear();
    try {
      while (true) {
        // read an HTTP request
        int user;
      	String headline = in.readLine();
        String line;

        if (headline == null) {
          break;
        }

        parameters = new RequestParameters(headline);
        do {
          line = in.readLine();
          if (line == null) {
            Log.log("[Client]: EOF");
            return;
          }
          int colon = line.indexOf(':');
          if (colon > 0) {
            parameters.set(line.substring(0, colon), line.substring(colon + 1).trim());
          }

          Log.log("[Client]: " + line);
        } while ((line != null) && (line.length() > 0));

        int contentLength = Integer.parseInt(parameters.get("Content-Length", "0"));
        if (contentLength > 0) {
          char[] contentArray = new char[contentLength];
          in.read(contentArray);
          parameters.extractParameters(new String(contentArray));
        }

        user = Integer.parseInt(parameters.getCookie("session", "-1"));
        if (user > 0) {
          Personalize.set(user, parameters.get("Accept-Language"));
        }

        Log.log("Received request " + parameters);
        request = connection.getProcessor().process(parameters, user);
        if (request == null) {
        	break;
        }
      }
    } catch (SocketException se) {
      Log.log("[Client]: " + se);
    } catch (Exception e) {
      Log.log("[Client]: ", e);
    }
  }

}