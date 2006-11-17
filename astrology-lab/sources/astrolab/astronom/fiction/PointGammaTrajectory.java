package astrolab.astronom.fiction;

import java.util.Calendar;

import astrolab.astronom.track.ActivePointTrajectory;
import astrolab.astronom.track.TrajectoryPoint;

class PointGammaTrajectory extends ActivePointTrajectory {

	private PointGammaTrajectory(TrajectoryPoint[] trajectory) {
		super(trajectory);
	}

	// speed is the millis needed for a full cycle
	static PointGammaTrajectory getTrajectory(PointGamma gamma, Calendar start, Calendar end, double speed) {
		double startPosition = gamma.getPosition(start);
		double endPosition = gamma.getPosition(end);
		long path = end.getTimeInMillis() - start.getTimeInMillis();
		System.out.println(" end: " + end.getTimeInMillis());
		System.out.println(" start: " + start.getTimeInMillis());
		int ariesCrossings = (int) (path / speed);
		if (((double) (path % speed) / speed) * 360 > startPosition) {
			System.out.println(" path: " + path);
			System.out.println(" speed: " + speed);
			System.out.println(" year: " + (path % speed));
			System.out.println(" year: " + (- ariesCrossings * speed));
			System.out.println(" year: " + (path - ariesCrossings * speed));
			System.out.println(" portion: " + ((path % speed) / speed));
			System.out.println(" degrees: " + ((path % speed) / speed * 360));
			System.out.println(" gone: " + startPosition);
			ariesCrossings++;
		}
		TrajectoryPoint[] trajectory = new TrajectoryPoint[ariesCrossings + 2];
		trajectory[0] = new TrajectoryPoint(start, startPosition, false);
		Calendar c;
		long time = start.getTimeInMillis() + (long) (speed * startPosition / 360); // it's moving backwards
		for (int i = 1; i < trajectory.length - 1; i++) {
			c = Calendar.getInstance();
			c.setTimeInMillis(time);
  		trajectory[i] = new TrajectoryPoint(c, 0, false);
  		time += speed;
		}
		trajectory[trajectory.length - 1] = new TrajectoryPoint(end, endPosition, false);

		return new PointGammaTrajectory(trajectory);
	}

}
