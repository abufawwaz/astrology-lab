package astrolab;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.*;

import astrolab.db.Project;
import astrolab.db.ProjectIterator;
import astrolab.formula.FormulaPlot;
import astrolab.formula.Formulae;
import astrolab.formula.FormulaGenerator;

public class Test {

  public static void main(String[] args) throws Exception {
    FileInputStream fos = new FileInputStream("testrecords.txt");
    LineNumberReader reader = new LineNumberReader(new InputStreamReader(fos));
    Class.forName("com.mysql.jdbc.Driver");
    Statement connection = DriverManager.getConnection("jdbc:mysql://astrology-lab!!!-change this when ready-!!!.net:3306/astrolab", "develop", "develop").createStatement();
//    Statement connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/astrolab", "develop", "develop").createStatement();
    String line = null;
    while ((line = reader.readLine()) != null) {
      StringTokenizer tokens = new StringTokenizer(line, "\t");
      String event_id = tokens.nextToken();
      String time = tokens.nextToken();
      String event_description = tokens.nextToken();
      String elapsed = tokens.nextToken();
      String answer = tokens.nextToken();
      String color = tokens.nextToken();
      String mouse_x = tokens.nextToken();
      String mouse_y = tokens.nextToken();
      String sequence = tokens.nextToken();
      connection.execute("INSERT INTO text VALUES ('" + event_id + "', NULL, '" + event_description + "', '" + event_description + "')");
      connection.execute("INSERT INTO archive VALUES ('" + event_id + "', '2000001', '" + time + "', '0')");
      if (event_description.startsWith("Test1")) {
        connection.execute("INSERT INTO project_test_psycho_color_blocks VALUES ('" + event_id + "', '" + sequence + "', '" + answer + "', '" + color + "', '" + elapsed + "', '" + mouse_x + "', '" + mouse_y + "')");
      } else {
        connection.execute("INSERT INTO project_test_psycho_reaction VALUES ('" + event_id + "', '" + sequence + "', '" + answer + "', '" + color + "', '" + elapsed + "', '" + mouse_x + "', '" + mouse_y + "')");
      }
    }
    fos.close();
  }

  public static void main9(String[] args) throws Exception {
    FileOutputStream fos = new FileOutputStream("testrecords.txt");
    Class.forName("com.mysql.jdbc.Driver");
    Statement connection = DriverManager.getConnection("jdbc:mysql://astrology-lab.net:3306/astrolab", "develop", "develop").createStatement();
//    ResultSet set = connection.executeQuery("SELECT * FROM attribute WHERE object_id > 2000002 SORT BY object_id");
    ResultSet set = connection.executeQuery("SELECT event_id, event_time, text FROM archive, text WHERE event_id = id AND event_id > 2000003 ORDER BY event_id");
    int record = 1;
    connection = DriverManager.getConnection("jdbc:mysql://astrology-lab.net:3306/astrolab", "develop", "develop").createStatement();
    while (set.next()) {
      String line = "";
      int record_id = set.getInt(1);
      System.out.print("" + record + ":\t");
      line += "" + record_id + "\t" + set.getTimestamp(2) + "\t" + set.getString(3) + "\t";
      System.out.print(line);
      ResultSet set1 = connection.executeQuery("SELECT attribute_value FROM attribute WHERE object_id = " + record_id + " ORDER BY attribute_id");
      while (set1.next()) {
        int value = set1.getInt(1);
        System.out.print("" + value + "\t");
        line += value + "\t";
      }
      System.out.println();
      record++;

      line += "\r\n";
      fos.write(line.getBytes());
    }
    fos.close();
  }

  public static void main3(String[] args) throws Exception {
    getNumbers();
  }

  private static void getNumbers() throws Exception {
    FormulaPlot plot = new FormulaPlot(1000);

    String line = null;
    java.util.Date date;
    int number;
    LineNumberReader lines = new LineNumberReader(new InputStreamReader(new FileInputStream("./sources/numbers.txt")));
    while ((line = lines.readLine()) != null) {
      date = new java.util.Date(line.substring(0, 11));
//      date.setHours(20);
      date.setHours(23);
      date.setMinutes(57);
      number = Integer.parseInt(line.substring(18, 19));
//      number = Integer.parseInt(line.substring(16, 17));
      plot.feed(date, number);
    }
    System.out.println(" plot: " + plot.toString(20));
    while (true) {
      plot.step(100);
      System.out.println(" plot: " + plot.toString(20));
    }
  }

  public static void main2(String[] args) {
    System.out.println(" formulae: ");
    Formulae f = FormulaGenerator.generateNext(null);
    for (int i = 0; i < 1000; i++) {
      System.out.println(" " + (i + 1) + ": " + f);
      f = FormulaGenerator.generateNext(f);
    }
    System.out.println(" === ");
  }

  public static void main1(String[] args) {
    System.out.println(" projects: ");
    ProjectIterator projects = ProjectIterator.iterate(-1);
    while (projects.hasNext()) {
      Project p = projects.next();
      System.out.print(p.getName());
      System.out.print(" ");
      System.out.print(p.getType());
      System.out.print(" ");
      System.out.println();
    }
    System.out.println(" === ");
  }

  public static void main4(String[] args) throws Exception {
    System.out.println(" get source ... ");
    Socket socket = new Socket("jwatt.org", 80);
    OutputStream out = socket.getOutputStream();
    out.write("GET /svg/demos/xhtml-with-inline-svg.xhtml HTTP/1.1\r\n".getBytes());
//    out.write("GET /svg/demos/xhtml-with-inline-svg.xhtml HTTP/1.1\r\n".getBytes());
    out.write("Host: jwatt.org\r\n".getBytes());
    out.write("User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8) Gecko/20051111 Firefox/1.5\r\n".getBytes());
    out.write("Accept: text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5\r\n".getBytes());
    out.write("Accept-Language: en-us,en;q=0.5\r\n".getBytes());
//    out.write("Accept-Encoding: gzip,deflate\r\n".getBytes());
    out.write("Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7\r\n".getBytes());
    out.write("Keep-Alive: 300\r\n".getBytes());
    out.write("Connection: keep-alive\r\n".getBytes());
    out.write("Cache-Control: max-age=0\r\n".getBytes());
    out.write("\r\n".getBytes());
    out.flush();
    InputStream in = socket.getInputStream();
    int ch = -1;
    while ((ch = in.read()) > 0) {
    	System.out.print((char) ch);
    }
  }

}