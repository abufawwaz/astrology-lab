package astrolab.astronom.track;

public class ActivePointTrajectory {

	private int point = 0;
	private TrajectoryPoint[] trajectory;

	protected ActivePointTrajectory(TrajectoryPoint[] trajectory) {
		this.trajectory = trajectory;
	}

	public boolean hasNext() {
		return (point < trajectory.length);
	}

	public TrajectoryPoint getNext() {
		return (point < trajectory.length) ? trajectory[point++] : null;
	}

}
