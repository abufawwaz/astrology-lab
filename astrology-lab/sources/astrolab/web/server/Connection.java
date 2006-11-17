package astrolab.web.server;

import java.io.IOException;
import java.net.*;

public class Connection {

  private ConnectionInput input;
  private ConnectionOutput output;
  private Processor processor;

  Connection(Socket socket) throws IOException {
    System.out.println("Connection accepted");
    processor = new Processor(this);
    input = new ConnectionInput(socket.getInputStream(), this);
    output = new ConnectionOutput(socket.getOutputStream());
  }

  public ConnectionInput getInput() {
    return input;
  }

  public ConnectionOutput getOutput() {
    return output;
  }

  public Processor getProcessor() {
    return processor;
  }

}