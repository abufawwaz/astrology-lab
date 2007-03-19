package astrolab.project.geography;

import java.util.TimeZone;

import astrolab.db.Text;
import astrolab.web.Modify;
import astrolab.web.server.Request;
import astrolab.web.server.RequestParameters;

public class ModifyLocation extends Modify {

	public void operate(Request request) {
    try {
      String name = request.get(RequestParameters.TEXT_NAME);
      if (Text.getId(name) == 0) {
        return;
      }
      int region = ComponentSelectLocation.retrieve(request);
      double longitude = ComponentSelectLongitude.retrieve(request);
      double lattitude = ComponentSelectLattitude.retrieve(request);
      TimeZone zone = ComponentSelectTimeZone.retrieve(request);
      Location.store(name, region, longitude, lattitude, zone);
    } catch (Exception e) {
      e.printStackTrace();
    }
	}

}