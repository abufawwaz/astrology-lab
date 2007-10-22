package astrolab;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.*;

import astrolab.astronom.ActivePoint;
import astrolab.astronom.SpacetimeEvent;
import astrolab.astronom.planet.SolarSystem;
import astrolab.db.Database;
import astrolab.db.Event;
import astrolab.db.Project;
import astrolab.db.ProjectIterator;
import astrolab.db.Text;
import astrolab.formula.Element;
import astrolab.formula.ElementSet;
import astrolab.formula.FormulaGeneratorElement;
import astrolab.formula.Formulae;
import astrolab.formula.FormulaGenerator;
import astrolab.project.Projects;
import astrolab.project.geography.Location;
import astrolab.tools.Log;

public class Test {

//  public static void main(String[] args) throws Exception {
//    astrolab.project.Project p = Projects.getProject(30030);
//    SpacetimeEvent start = p.getMinTime();
//    SpacetimeEvent end = p.getMaxTime();
//    SpacetimeEvent last = start;
//    SpacetimeEvent current = start;
//    int[] direction = new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1 };
//    int[] position = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
//    String[] points = new String[] {
//        SolarSystem.SUN, // SolarSystem.MOON,
//        SolarSystem.MERCURY, SolarSystem.VENUS,
//        SolarSystem.MARS, SolarSystem.JUPITER, SolarSystem.SATURN,
//        SolarSystem.URANUS, SolarSystem.NEPTUNE, SolarSystem.PLUTO
//    };
//
//    while (current.getTimeInMillis() < end.getTimeInMillis()) {
//      boolean stop = false;
//
//      for (int i = 0; i < points.length; i++) {
//        int pos = (int) ActivePoint.getActivePoint(points[i], current).getPosition();
//        if (((int) (pos / 30)) != ((int) (position[i] / 30))) {
//          // sign ingression
//          stop = true;
//        }
//        int dir = pos - position[i];
//        if (Math.abs(dir) > 180) {
//          dir *= -1;
//        }
//        dir = (dir >= 0) ? 1 : -1;
//
//        if (direction[i] != dir) {
//          // sign ingression
//          stop = true;
//        }
//
//        position[i] = pos;
//        direction[i] = dir;
//      }
//
//      if (stop) {
//        if (current.getTimeInMillis() - last.getTimeInMillis() > 1000L * 60 * 60 * 24 * 15) {
//          System.out.println("period: " + last.toMySQLString() + " -> " + current.toMySQLString());
//        } else {
//          // ignore short period
//        }
//        last = current;
//      }
//
//      // move to next day
//      current = current.getMovedSpacetimeEvent(SpacetimeEvent.DAY_OF_MONTH, 1);
////      System.err.println("\r\n calculate: " + current.toSimpleString());
//    }
//  }

  public static void main(String[] args) throws Exception {
    new Test().test();
  }

  public void test() throws Exception {
    final int ITERATIONS = 10000000;
    String string1 = "aadadaasd";
    String string2 = "fsdfsdfsdfsdfsdfs";
    String result = null;

    long time = System.currentTimeMillis();
    for (int i = 0; i < ITERATIONS; i++) {
      test1(string1, string2);
    }
    System.out.println("elapsed time: " + (System.currentTimeMillis() - time));

    time = System.currentTimeMillis();
    for (int i = 0; i < ITERATIONS; i++) {
      test2(string1, string2);
    }
    System.out.println("elapsed time: " + (System.currentTimeMillis() - time));

    time = System.currentTimeMillis();
    for (int i = 0; i < ITERATIONS; i++) {
      result = new StringBuffer(string1.length() + string2.length() + 1).append(string1).append("*").append(string2).toString();
    }
    System.out.println("elapsed time: " + (System.currentTimeMillis() - time));

    time = System.currentTimeMillis();
    for (int i = 0; i < ITERATIONS; i++) {
      HashMap<String, String> hm = strings.get(string1);
      if (hm == null) {
        hm = new HashMap<String, String>(11);
        hm.put(string2, string1 + "*" + string2);
        strings.put(string1, hm);
      }
      result = hm.get(string2);
    }
    System.out.println("elapsed time: " + (System.currentTimeMillis() - time));
  }

  private String test1(String string1, String string2) {
//    return string1 + "*" + string2;
    return new StringBuffer(string1.length() + string2.length() + 1).append(string1).append("*").append(string2).toString();
  }

  HashMap<String, HashMap<String, String>> strings = new HashMap<String, HashMap<String, String>>(11);

  private final String test2(String string1, String string2) {
    HashMap<String, String> hm = strings.get(string1);
    if (hm == null) {
      hm = new HashMap<String, String>(11);
      hm.put(string2, string1 + "*" + string2);
      strings.put(string1, hm);
    }
    return hm.get(string2);
  }

  public static void main21(String[] args) throws Exception {
    String fileFolder = "D:/Astrology/astrology-estate.com/forum/language/lang_english";
    FileInputStream fis = new FileInputStream(new File(fileFolder, "lang_faq.php"));
    LineNumberReader reader = new LineNumberReader(new InputStreamReader(fis, "windows-1251"));
    String line = "";
    String html = "";
    while ((line = reader.readLine()) != null) {
      System.out.println(line);
      html += "\r\n";
      html += line;
    }
    FileOutputStream fos = new FileOutputStream(new File(fileFolder, "lang_faq.txt"));
    byte[] data = html.getBytes("utf-8");
//    for (String set: Charset.availableCharsets().keySet()) {
//      System.out.println(" set: " + set);
//    }
    fos.write(data);
    fos.close();
  }

  public static void main3(String[] args) throws Exception {
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

  public static void main10(String[] args) throws Exception {
    getForexRates();
  }

//  private static void getNumbers() throws Exception {
//    FormulaPlot plot = new FormulaPlot(1000);
//
//    String line = null;
//    java.util.Date date;
//    int number;
//    LineNumberReader lines = new LineNumberReader(new InputStreamReader(new FileInputStream("./sources/numbers.txt")));
//    while ((line = lines.readLine()) != null) {
//      date = new java.util.Date(line.substring(0, 11));
////      date.setHours(20);
//      date.setHours(23);
//      date.setMinutes(57);
//      number = Integer.parseInt(line.substring(18, 19));
////      number = Integer.parseInt(line.substring(16, 17));
//      plot.feed(date, number);
//    }
//    System.out.println(" plot: " + plot.toString(20));
//    while (true) {
//      plot.step(100);
//      System.out.println(" plot: " + plot.toString(20));
//    }
//  }

  private static void getSolarNumbers() throws Exception {
    String datatext = null;
    Timestamp date;
    int number;

    Connection connection = Database.getConnection();
    PreparedStatement update = connection.prepareStatement("INSERT INTO project_sunspots VALUES (?, ?)");
    connection.setAutoCommit(false);

    Log.beSilent(true);
    File root = new File("D:\\Astrology\\sunspots");
    for (File file: root.listFiles()) {
      int year = Integer.parseInt(file.getName());
      System.err.println("Year " + year + " ...");
      LineNumberReader lines = new LineNumberReader(new InputStreamReader(new FileInputStream(file)));
      String line = lines.readLine(); // skip header line 1
      lines.readLine(); // skip header line 2
      lines.readLine(); // skip header line 3
      lines.readLine(); // skip header line 4
      int day = 1;

      while ((line = lines.readLine()).indexOf("---") < 0) {
        StringTokenizer tokens = new StringTokenizer(",");
//        String year = tokens.nextToken().trim();
        tokens.nextToken();

        for (int month = 0; month < 12; month++) {
          date = new Timestamp(new java.util.Date(year - 1900, month, day).getTime());
          datatext = line.substring(month * 6, month * 6 + 4).trim();
          if (datatext.length() > 0) {
            try {
              number =  Integer.parseInt(datatext);
            } catch (NumberFormatException nfe) {
              number = 0;
            }

            update.setTimestamp(1, date);
            update.setInt(2, number);
            update.execute();
          }
        }

        day++;
      }
      connection.commit();
    }
  }

  private static void getForexRates() throws Exception {
    Connection connection = Database.getConnection();
    PreparedStatement update = connection.prepareStatement(
        "INSERT INTO project_forex VALUES (2000005, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
    connection.setAutoCommit(false);

    Log.beSilent(true);
    File file = new File("D:\\Astrology\\forex\\2007\\EURUSD30.csv");

    int minute;
    int hour;
    int day;
    int year;
    int month;
    Timestamp date;

    String line;
    Location gmt = Location.getLocation(Text.getId("Greenwich"));
    LineNumberReader lines = new LineNumberReader(new InputStreamReader(new FileInputStream(file)));
    while ((line = lines.readLine()) != null) {
      StringTokenizer tokens = new StringTokenizer(line, ",");

      String dateText = tokens.nextToken().trim();
      int index1 = dateText.indexOf('.');
      int index2 = dateText.lastIndexOf('.');
      year = Integer.parseInt(dateText.substring(0, index1));
      month = Integer.parseInt(dateText.substring(index1 + 1, index2));
      day = Integer.parseInt(dateText.substring(index2 + 1));

      String hourText = tokens.nextToken().trim();
      index1 = hourText.indexOf(':');
      hour = Integer.parseInt(hourText.substring(0, index1));
      minute = Integer.parseInt(hourText.substring(index1 + 1));


      date = new Timestamp(new java.util.Date(year - 1900, month, day, hour, minute, 0).getTime());
      update.setTimestamp(1, date);

      tokens.nextToken(); // ignore open
      update.setDouble(4, Double.parseDouble(tokens.nextToken())); // max_exchange_rate_close = high
      update.setDouble(3, Double.parseDouble(tokens.nextToken())); // min_exchange_rate_close = low
      update.setDouble(2, Double.parseDouble(tokens.nextToken())); // exchange_rate_close = close
      update.setDouble(5, Double.parseDouble(tokens.nextToken())); // volume = volume

      SpacetimeEvent event = new SpacetimeEvent(date.getTime(), gmt);
      update.setDouble(6, ActivePoint.getActivePoint(SolarSystem.SUN, event).getPosition());
      update.setDouble(7, ActivePoint.getActivePoint(SolarSystem.MOON, event).getPosition());
      update.setDouble(8, ActivePoint.getActivePoint(SolarSystem.MERCURY, event).getPosition());
      update.setDouble(9, ActivePoint.getActivePoint(SolarSystem.VENUS, event).getPosition());
      update.setDouble(10, ActivePoint.getActivePoint(SolarSystem.MARS, event).getPosition());
      update.setDouble(11, ActivePoint.getActivePoint(SolarSystem.JUPITER, event).getPosition());
      update.setDouble(12, ActivePoint.getActivePoint(SolarSystem.SATURN, event).getPosition());
      update.setDouble(13, ActivePoint.getActivePoint(SolarSystem.URANUS, event).getPosition());
      update.setDouble(14, ActivePoint.getActivePoint(SolarSystem.NEPTUNE, event).getPosition());
      update.setDouble(15, ActivePoint.getActivePoint(SolarSystem.PLUTO, event).getPosition());

      update.execute();

      if (day == 1 && hour == 0 && minute == 0) {
        connection.commit();
        System.err.println("Input " + event.toSimpleString()
            + " - Sun=" + ActivePoint.getActivePoint(SolarSystem.SUN, event).getPosition()
            + " - Moon=" + ActivePoint.getActivePoint(SolarSystem.MOON, event).getPosition()
            + " - Merc=" + ActivePoint.getActivePoint(SolarSystem.MERCURY, event).getPosition()
            + " - Venus=" + ActivePoint.getActivePoint(SolarSystem.VENUS, event).getPosition()
            + " - Mars=" + ActivePoint.getActivePoint(SolarSystem.MARS, event).getPosition()
            + " - Jupiter=" + ActivePoint.getActivePoint(SolarSystem.JUPITER, event).getPosition()
            + " - Saturn=" + ActivePoint.getActivePoint(SolarSystem.SATURN, event).getPosition()
            + " - Uranus=" + ActivePoint.getActivePoint(SolarSystem.URANUS, event).getPosition()
            + " - Neptune=" + ActivePoint.getActivePoint(SolarSystem.NEPTUNE, event).getPosition()
            + " - Pluto=" + ActivePoint.getActivePoint(SolarSystem.PLUTO, event).getPosition());
      }
    }
    connection.commit();
  }

  public static void main4(String[] args) throws Exception {
    for (int day = 1; day < 600; day++) {
      int id = Text.reserve("Stats:Random:" + day, Text.TYPE_EVENT);
      Event.store(id, 0, new java.util.Date(107, 11, day).getTime(), 0, Event.TYPE_EVENT, Event.ACCURACY_DAY, Event.SOURCE_ACCURATE);
      Database.execute("INSERT INTO project_statistics_value VALUES (" + id + ", 3000027, " + ((int) (Math.random() * 500)) + ")");
    }
  }

  public static void main2(String[] args) {
    System.out.println(" formulae: ");
    Element[] formulaElements = ElementSet.getDefault().getElements();
    FormulaGeneratorElement[] formulaSource = new FormulaGeneratorElement[formulaElements.length];

    for (int i = 0; i < formulaSource.length; i++) {
      formulaSource[i] = new FormulaGeneratorElement(formulaElements[i].getId());
    }
    
    Formulae f = FormulaGenerator.generateNext(formulaSource);
    for (int i = 0; i < 10000000; i++) {
      System.out.println(" " + (i + 1) + ": " + f);
      f = FormulaGenerator.generateNext(formulaSource);
    }
    System.out.println(" === ");
  }

  public static void main1(String[] args) {
    System.out.println(" projects: ");
    ProjectIterator projects = ProjectIterator.iterate();
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

  public static void main5(String[] args) throws Exception {
    System.out.println(" HTTP request ... ");
    Socket socket = new Socket("www.businessastrologers.com", 80);
    OutputStream out = socket.getOutputStream();
    out.write("GET /secure/rs_articles.html HTTP/1.1\r\n".getBytes());
//    out.write("GET /svg/demos/xhtml-with-inline-svg.xhtml HTTP/1.1\r\n".getBytes());
    out.write("Host: www.businessastrologers.com\r\n".getBytes());
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

  public static void main7(String[] args) throws Exception {
    System.out.println(" HTTP request ... ");
    Socket socket = new Socket("localhost", 8080);
    OutputStream out = socket.getOutputStream();
    out.write("GET / HTTP/1.1\r\n".getBytes());
    out.write("Host: www.astrology-lab.net\r\n".getBytes());
    out.write("Accept: */*\r\n".getBytes());
    out.write("Connection: Keep-alive\r\n".getBytes());
    out.write("From: googlebot(at)googlebot.com\r\n".getBytes());
    out.write("User-Agent: Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.test2.com/bot.html)\r\n".getBytes());
    out.write("Accept-Encoding: gzip\r\n".getBytes());
    out.write("If-Modified-Since: Mon, 13 Aug 2007 04:16:45 GMT\r\n".getBytes());
    out.write("\r\n".getBytes());
    out.flush();

    InputStream in = socket.getInputStream();
    int ch = -1;
    while ((ch = in.read()) > 0) {
      System.out.print((char) ch);
    }
  }

}