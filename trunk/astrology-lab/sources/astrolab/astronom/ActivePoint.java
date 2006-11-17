package astrolab.astronom;

import java.util.Calendar;

import astrolab.astronom.track.ActivePointTrajectory;
import astrolab.db.Event;

public abstract class ActivePoint {

	private Event birth;

	public void initialize(Event birth) {
		this.birth = birth;
	}

	public abstract String getIcon();

	public abstract String getName();

	public abstract double getPosition(Calendar calendar);

	public abstract ActivePointTrajectory getTrajectory(Calendar start, Calendar end);

	protected Event getBirth() {
		return birth;
	}

}
