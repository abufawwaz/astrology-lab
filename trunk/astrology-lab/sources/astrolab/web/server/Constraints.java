package astrolab.web.server;

public class Constraints {

	public int getHeight() {
		return 500;
	}

	public int getWidth() {
		return 500;
	}

	public int getRadius() {
		return Math.min(getWidth(), getHeight()) / 2;
	}
}