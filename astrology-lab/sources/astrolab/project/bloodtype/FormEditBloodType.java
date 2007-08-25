package astrolab.project.bloodtype;

import astrolab.db.Event;
import astrolab.web.HTMLFormDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class FormEditBloodType extends HTMLFormDisplay {

  private final static String PARAMETER_SUBJECT = "user.session.event.1";
  private final static String PARAMETER_BLOOD_TYPE = "_blood_type";
  private final static String PARAMETER_BLOOD_RHESUS = "_blood_rhesus";

  private final static int ID = HTMLFormDisplay.getId(FormEditBloodType.class);

  public FormEditBloodType() {
    super("Blood Type", ID, true);
    super.addAction("event", "user.session.event.1");
  }

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    int subject = Event.getSelectedEvent().getId();

    checkInput(request, subject);

    BloodTypeRecord record = BloodTypeRecord.read(subject);
    Event natalRecord = new Event(subject);

    buffer.append("<table border='0'>");
    buffer.append("<tr><td>");
    buffer.localize(natalRecord.getType());
    buffer.append("</td><td>");
    buffer.localize(record.getSubjectId());
    buffer.append("</td></tr>");

    buffer.append("<tr><td>");
    buffer.localize("Location");
    buffer.append(":</td><td>");
    buffer.localize(natalRecord.getLocation().getId());
    buffer.append("</td></tr>");

    buffer.append("<tr><td>");
    buffer.localize("Time");
    buffer.append(":</td><td>");
    buffer.localize(natalRecord.toSimpleString());
    buffer.append("</td></tr>");

    buffer.append("<tr><td>");
    buffer.localize("Blood type");
    buffer.append("</td><td>");
    buffer.append("<select id='" + PARAMETER_BLOOD_TYPE + "' name='" + PARAMETER_BLOOD_TYPE + "'>");
    appendType(buffer, record, BloodTypeRecord.TYPE_UNKNOWN);
    appendType(buffer, record, BloodTypeRecord.TYPE_O);
    appendType(buffer, record, BloodTypeRecord.TYPE_A);
    appendType(buffer, record, BloodTypeRecord.TYPE_B);
    appendType(buffer, record, BloodTypeRecord.TYPE_AB);
    buffer.append("</select>");
    buffer.append("</td></tr>");

    buffer.append("<tr><td>");
    buffer.localize("Rh factor");
    buffer.append("</td><td>");
    buffer.append("<select id='" + PARAMETER_BLOOD_RHESUS + "' name='" + PARAMETER_BLOOD_RHESUS + "'>");
    appendRhesus(buffer, record, BloodTypeRecord.RHESUS_UNKNOWN);
    appendRhesus(buffer, record, BloodTypeRecord.RHESUS_POSITIVE);
    appendRhesus(buffer, record, BloodTypeRecord.RHESUS_NEGATIVE);
    buffer.append("</select>");
    buffer.append("</td></tr>");

    buffer.append("</table>");
    super.addSubmit(buffer, "Submit");

    buffer.append("<input type='hidden' id='" + PARAMETER_SUBJECT + "' name='" + PARAMETER_SUBJECT + "' value='" + subject + "'/>");
  }

  private void checkInput(Request request, int subject) {
    String bloodType = request.getParameters().get(PARAMETER_BLOOD_TYPE);
    if (bloodType != null) {
      String rhesusFactor = request.getParameters().get(PARAMETER_BLOOD_RHESUS);
      BloodTypeRecord record = new BloodTypeRecord(subject, bloodType, rhesusFactor);
      BloodTypeRecord.store(record);
    }
  }

  private final void appendType(LocalizedStringBuffer buffer, BloodTypeRecord record, String option) {
    if (option.equals(record.getBloodType())) {
      buffer.append("<option value='" + option + "' selected='true'>");
    } else {
      buffer.append("<option value='" + option + "'>");
    }
    buffer.append(option);
    buffer.append("</option>");
  }

  private final void appendRhesus(LocalizedStringBuffer buffer, BloodTypeRecord record, String option) {
    if (option.equals(record.getRhesusFactor())) {
      buffer.append("<option value='" + option + "' selected='true'>");
    } else {
      buffer.append("<option value='" + option + "'>");
    }
    buffer.append(option);
    buffer.append("</option>");
  }

}