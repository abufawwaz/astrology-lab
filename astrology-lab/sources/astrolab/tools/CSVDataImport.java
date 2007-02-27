package astrolab.tools;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

import astrolab.db.Database;

public class CSVDataImport {

  private final static String PROJECT = "exchange";
  private final static String PROFILE = "2000005";
  private final static String FILENAME = "D:/develop/programs/nwdevstudio/workspace/astrology-lab/data/EURUSD1.csv";

  public static void main(String[] args) throws Exception {
    DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm");
    LineNumberReader lines = new LineNumberReader(new InputStreamReader(new FileInputStream(FILENAME)));

    String line;
    String oldDate = null;

    PreparedStatement sql = Database.prepareStatement("INSERT INTO project_" + PROJECT + " VALUES (" + PROFILE + ", ?, ?, ?, ?, ?, ?)");

    while ((line = lines.readLine()) != null) {
      StringTokenizer tokens = new StringTokenizer(line, ",");
      String date = tokens.nextToken().trim();
      String hour = tokens.nextToken().trim();
      String open = tokens.nextToken().trim();
      String high = tokens.nextToken().trim();
      String low = tokens.nextToken().trim();
      String close = tokens.nextToken().trim();
      String volume = tokens.nextToken().trim();

      sql.setTimestamp(1, new Timestamp(df.parse(date + " " + hour).getTime()));
      sql.setString(2, open);
      sql.setString(3, high);
      sql.setString(4, low);
      sql.setString(5, close);
      sql.setString(6, volume);

      sql.executeUpdate();

      if (!date.equals(oldDate)) {
        System.out.println("imported date: " + date);
        oldDate = date;
      }
    }
  }
}