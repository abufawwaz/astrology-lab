package astrolab.astronom.track;

import java.util.Calendar;

public class TrajectoryPoint {

	private boolean isForward;
	private double position;
	private Calendar time;

	public TrajectoryPoint(Calendar time, double position, boolean isForward) {
		this.time = time;
		this.position = position;
		this.isForward = isForward;
	}

	public double getPosition() {
		return position;
	}

	public Calendar getTime() {
		return time;
	}

	public boolean isForward() {
		return isForward;
	}

	public String toString() {
		return "[" + time.getTime() + ": " + position + " " + (isForward ? "forward" : "backward") + "]";
	}
}
