package astrolab.project.geography;

import java.util.TimeZone;

import astrolab.web.Modify;
import astrolab.web.server.Request;
import astrolab.web.server.RequestParameters;

public class ModifyLocation extends Modify {

	public void operate(Request request) {
    int locationId = request.getParameters().getInt("_location_id");
    String name = request.get(RequestParameters.TEXT_NAME);
    int region = ComponentSelectLocation.retrieve(request);
    double longitude = ComponentSelectLongitude.retrieve(request);
    double lattitude = ComponentSelectLattitude.retrieve(request);
    TimeZone zone = ComponentSelectTimeZone.retrieve(request);

    if (locationId > 0) {
      Location.update(locationId, region, longitude, lattitude, zone);
    } else if ((name != null) && (name.length() > 0)) {
      Location.store(name, region, longitude, lattitude, zone);
    }
	}

}