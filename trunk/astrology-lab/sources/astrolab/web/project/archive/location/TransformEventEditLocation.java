package astrolab.web.project.archive.location;

import astrolab.db.Location;
import astrolab.db.LocationIterator;
import astrolab.web.component.Selectable;
import astrolab.web.server.Request;

public class TransformEventEditLocation { // TODO: implements Transform {

	public void transform(Request request) {
		LocationIterator iterator = LocationIterator.iterate(0);
//		while (iterator.hasNext()) {
//			request.getContent().add(new LocationSelectable(iterator.next()));
//		}
    new Exception().printStackTrace();
	}

//  class LocationSelectable extends Selectable {
//
//  	private Location location;
//
//  	LocationSelectable(Location location) {
//  		super(location.getId());
//  		this.location = location;
//  	}
//
//  	public void fillContent(Request request, StringBuffer buffer) {
//			this.wrap(request, buffer, location.toString());
//		}
//  	
//  }

}