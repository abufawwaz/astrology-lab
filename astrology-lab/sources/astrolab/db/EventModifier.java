package astrolab.db;

public class EventModifier {

	public static void modifyTime(int event, int year, int month, int date, int hour, int minute, int second) {
		String time = "" + year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
		Database.execute("UPDATE archive SET event_time = '" + time + "' WHERE event_id = " + event);
	}

	public static void modifyLocation(int event, int location) {
		Database.execute("UPDATE archive SET location = '" + location + "' WHERE event_id = " + event);
	}

	public static void modifyValue(int event, double value) {
		Database.execute("UPDATE archive SET event_value = '" + value + "' WHERE event_id = " + event);
	}

	public static void modifySubject(int event, int subject) {
		Database.execute("UPDATE archive SET subject_id = '" + subject + "' WHERE event_id = " + event);
	}

}
