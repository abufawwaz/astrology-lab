package astrolab.tools;

import java.io.*;
import java.util.*;

import astrolab.astronom.util.LocationParser;
import astrolab.db.Database;
import astrolab.db.Text;
import astrolab.project.geography.ProjectGeography;

public class LocationImport {

	public static void main(String[] args) {
		System.out.println("Import 'not set' place");
		importLocation(0, 0, 0, 0, 0);
		System.out.println("Start import of locations.data ...");
    FileInputStream in = null;

    try {
      in = new FileInputStream("C:\\stephan\\develop\\astro\\settings\\astronomy\\locations.data");
      LineNumberReader reader = new LineNumberReader(new InputStreamReader(in));
      StringTokenizer tokens = null;
      String line = reader.readLine();

      while (line != null) {
        tokens = new StringTokenizer(line, ",;");
        String name = tokens.nextToken();
        String country = tokens.nextToken();
        String longitude = tokens.nextToken();
        String latitude = tokens.nextToken();
        String zone = tokens.nextToken();

        importLocation(name, country, longitude, latitude, zone);

        line = reader.readLine();
      }
    } catch (NoSuchElementException nsee) {
    } catch (Throwable e) {
      e.printStackTrace();
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (Exception ee) {
          ee.printStackTrace();
        }
      }
    }
		
	}

	private static void importLocation(String city, String country, String longitude, String lattitude, String zone) {
		importLocation(Text.reserve(city, Text.TYPE_REGION), Text.reserve(country, Text.TYPE_REGION), LocationParser.parseLongitude(longitude), LocationParser.parseLattitude(lattitude), LocationParser.parseTimeZone(zone));
	}

	private final static void importLocation(int city, int country, double longitude, double lattitude, double zone) {
		try {
  		Database.execute("INSERT INTO " + ProjectGeography.TABLE_NAME + " values (" + city + ", 0, " + country + ", 0, 0, " + city + ", 0, 0, " + longitude + ", " + lattitude + ", " + zone + " )");
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

}
