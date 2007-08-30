package astrolab.project.geography;

import java.util.TimeZone;

import astrolab.db.Personalize;
import astrolab.db.Text;
import astrolab.web.Modify;
import astrolab.web.server.Request;

public class ModifyLocation extends Modify {

	public void operate(Request request) {
    int locationId = request.getParameters().getInt("_location_id");
    String nameEn = request.get(FormAddLocation.KEY_NAME_EN);
    String nameBg = request.get(FormAddLocation.KEY_NAME_BG);
    int region = ComponentSelectLocation.retrieve(request);
    double longitude = ComponentSelectLongitude.retrieve(request);
    double lattitude = ComponentSelectLattitude.retrieve(request);
    TimeZone zone = ComponentSelectTimeZone.retrieve(request);

    if (locationId > 0) {
      Location.update(locationId, region, longitude, lattitude, zone);
      if ((nameEn != null) && (nameEn.length() > 0)) {
        Text.changeText(locationId, nameEn, Personalize.LANGUAGE_EN);
      }
      if ((nameBg != null) && (nameBg.length() > 0)) {
        Text.changeText(locationId, nameBg, Personalize.LANGUAGE_BG);
      }
    } else {
      if (Personalize.LANGUAGE_EN.equals(Personalize.getLanguage())) {
        if ((nameEn != null) && (nameEn.length() > 0)) {
          locationId = Location.store(nameEn, region, longitude, lattitude, zone);
          if ((nameBg != null) && (nameBg.length() > 0)) {
            Text.changeText(locationId, nameBg, Personalize.LANGUAGE_BG);
          }
        }
      } else if (Personalize.LANGUAGE_BG.equals(Personalize.getLanguage())) {
        if ((nameBg != null) && (nameBg.length() > 0)) {
          locationId = Location.store(nameBg, region, longitude, lattitude, zone);
          if ((nameEn != null) && (nameEn.length() > 0)) {
            Text.changeText(locationId, nameEn, Personalize.LANGUAGE_EN);
          }
        }
      }
    }
	}

}