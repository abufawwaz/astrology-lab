package astrolab.project.bloodtype;

import astrolab.db.Database;

public class BloodTypeRecord {

  public final static String TYPE_UNKNOWN = "?";
  public final static String TYPE_O = "O";
  public final static String TYPE_A = "A";
  public final static String TYPE_B = "B";
  public final static String TYPE_AB = "AB";

  public final static String RHESUS_UNKNOWN = "?";
  public final static String RHESUS_POSITIVE = "+";
  public final static String RHESUS_NEGATIVE = "-";

  private int subjectId;
  private String bloodType = TYPE_UNKNOWN;
  private String rhesusFactor = RHESUS_UNKNOWN;

  BloodTypeRecord(int subjectId, String bloodType, String rhesusFactor) {
    this.subjectId = subjectId;

    if (bloodType != null) {
      this.bloodType = bloodType;
    }

    if (rhesusFactor != null) {
      this.rhesusFactor = rhesusFactor;
    }
  }

  public int getSubjectId() {
    return subjectId;
  }

  public String getBloodType() {
    return bloodType;
  }

  public String getRhesusFactor() {
    return rhesusFactor;
  }

  public static BloodTypeRecord read(int subjectId) {
    String[][] values = Database.queryList(2, "SELECT blood_type, rhesus FROM project_blood_type where subject_id = " + subjectId + " LIMIT 1");
    if (values.length == 0) {
      return new BloodTypeRecord(subjectId, null, null);
    } else {
      return new BloodTypeRecord(subjectId, values[0][0], values[0][1]);
    }
  }

  public static void store(BloodTypeRecord record) {
    if (exists(record.getSubjectId())) {
      Database.execute("UPDATE project_blood_type SET blood_type = '" + record.getBloodType() + "' WHERE subject_id = " + record.getSubjectId());
      Database.execute("UPDATE project_blood_type SET rhesus = '" + record.getRhesusFactor() + "' WHERE subject_id = " + record.getSubjectId());
    } else {
      Database.execute("INSERT INTO project_blood_type VALUES (" + record.getSubjectId() + ", '" + record.getBloodType() + "', '" + record.getRhesusFactor() + "')");
    }
  }

  private static boolean exists(int subjectId) {
    return (Database.query("SELECT * FROM project_blood_type where subject_id = " + subjectId + " LIMIT 1") != null);
  }

}