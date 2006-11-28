package astrolab.project.match;

import astrolab.astronom.util.Zodiac;
import astrolab.db.Event;
import astrolab.web.HTMLDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class DisplayPartnerMatch extends HTMLDisplay {

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    Event person1 = Event.getSelectedEvent(1);
    Event person2 = Event.getSelectedEvent(2);

    Factors partner1 = FactorsFactory.getFactors(person1);
    Factors partner2 = FactorsFactory.getFactors(person2);
    Factor[] factorsPartner1 = partner1.toArray();
    Factor[] factorsPartner2 = partner2.toArray();

    double vi1 = 0;
    double vi2 = 0;
    double c1 = 0;
    double ir1 = 0;
    double ir2 = 0;
    double c2 = 0;

    buffer.append("<table border='2'>");
    buffer.append("<tr><th rowspan='3'>");
    buffer.localize("Factor");
    buffer.append("</th><th></th><th colspan='3'>");
    buffer.localize(person1.getSubjectId());
    buffer.append("</th><th></th><th colspan='3'>");
    buffer.localize(person2.getSubjectId());
    buffer.append("</th></tr>");

    buffer.append("<tr><th></th><th rowspan='2'>Position</th><th colspan='2'>Influence</th><th></th><th rowspan='2'>Position</th><th colspan='2'>Influence</th></tr>");

    buffer.append("<tr><th></th><th>General</th><th>Indiviual</th><th></th><th>General</th><th>Indiviual</th></tr>");

    for (int i = 0; i < factorsPartner1.length; i++) {
      buffer.append("<tr><td>");
      buffer.localize(factorsPartner1[i].getName());
      buffer.append("</td><td>");
      buffer.append("</td><td>");
      buffer.append(Zodiac.toString(factorsPartner1[i].getPosition(), "DD ZZZ MM"));
      buffer.append("</td><td>");
      displayRelativeInfluence(buffer, factorsPartner1[i].getGeneralInfluence(), factorsPartner1[i].getRelativeGeneralInfluence(), -1);
      buffer.append("</td><td>");
      displayRelativeInfluence(buffer, factorsPartner1[i].getIndividualInfluence(), factorsPartner1[i].getRelativeIndividualInfluence(), factorsPartner1[i].getRelativeGeneralInfluence());
      buffer.append("</td><td>");
      buffer.append("</td><td>");
      buffer.append(Zodiac.toString(factorsPartner2[i].getPosition(), "DD ZZZ MM"));
      buffer.append("</td><td>");
      displayRelativeInfluence(buffer, factorsPartner2[i].getGeneralInfluence(), factorsPartner2[i].getRelativeGeneralInfluence(), -1);
      buffer.append("</td><td>");
      displayRelativeInfluence(buffer, factorsPartner2[i].getIndividualInfluence(), factorsPartner2[i].getRelativeIndividualInfluence(), factorsPartner2[i].getRelativeGeneralInfluence());
      buffer.append("</td></tr>");
    }
    buffer.append("</table>");

    buffer.append("<br />");

    buffer.append("<table border='2'>");
    buffer.append("<tr><th rowspan='2'>Factor</th><th></th><th colspan='5'>" + person1.getSubject() + "</th><th></th><th colspan='5'>" + person2.getSubject() + "</th></tr>");
    buffer.append("<tr><th></th><th>Vision</th><th>V-I</th><th>Image</th><th>I-R</th><th>Reality</th><th></th><th>Vision</th><th>V-I</th><th>Image</th><th>I-R</th><th>Reality</th></tr>");
    for (int i = 0; i < factorsPartner1.length; i++) {
      double cvi1 = factorsPartner1[i].getVisionVsImage(partner2);
      double cir1 = factorsPartner1[i].getImageVsReality(partner2);
      double cvi2 = factorsPartner2[i].getVisionVsImage(partner1);
      double cir2 = factorsPartner2[i].getImageVsReality(partner1);

      vi1 += cvi1;
      ir1 += cir1;
      vi2 += cvi2;
      ir2 += cir2;

      buffer.append("<tr><td>");
      buffer.localize(factorsPartner1[i].getName());
      buffer.append("</td><td>");
      buffer.append("</td>");

      buffer.append("<td>");
      displayPercentage(buffer, factorsPartner1[i].getRelativeVisionOfPartner(partner2));
      buffer.append("</td><td>");
      displayCombination(buffer, cvi1);
      buffer.append("</td><td>");
      displayPercentage(buffer, factorsPartner1[i].getRelativeImageOfPartner(partner2));
      buffer.append("</td><td>");
      displayCombination(buffer, cir1);
      buffer.append("</td><td>");
      displayPercentage(buffer, factorsPartner1[i].getRelativeRealityOfPartner(partner2));
      buffer.append("</td>");

      buffer.append("<td></td>");

      buffer.append("<td>");
      displayPercentage(buffer, factorsPartner2[i].getRelativeVisionOfPartner(partner1));
      buffer.append("</td><td>");
      displayCombination(buffer, cvi2);
      buffer.append("</td><td>");
      displayPercentage(buffer, factorsPartner2[i].getRelativeImageOfPartner(partner1));
      buffer.append("</td><td>");
      displayCombination(buffer, cir2);
      buffer.append("</td><td>");
      displayPercentage(buffer, factorsPartner2[i].getRelativeRealityOfPartner(partner1));
      buffer.append("</td>");

      buffer.append("</tr>");
    }
    buffer.append("</table>");

    buffer.append("<br />");

    buffer.append("<table border='2'>");
    buffer.append("<tr><th>Person</th><th>Vision vs. Image</th><th>Image vs. Reality</th><th>Compatibility</th></tr>");

    buffer.append("<tr><td>");
    buffer.localize(person1.getSubjectId());
    buffer.append("</td><td>");
    vi1 /= 100;
    buffer.append((int) vi1);
    buffer.append("</td><td>");
    ir1 /= 100;
    buffer.append((int) ir1);
    buffer.append("</td><td>");
    c1 = Math.sqrt(vi1 * ir1);
    buffer.append((int) c1);
    buffer.append("</td></tr>");

    buffer.append("<tr><td>");
    buffer.localize(person2.getSubjectId());
    buffer.append("</td><td>");
    vi2 /= 100;
    buffer.append((int) vi2);
    buffer.append("</td><td>");
    ir2 /= 100;
    buffer.append((int) ir2);
    buffer.append("</td><td>");
    c2 = Math.sqrt(vi2 * ir2);
    buffer.append((int) c2);
    buffer.append("</td></tr>");

    buffer.append("</table>");

    buffer.append("<hr /> Overall Compatibility: ");
    buffer.append((int) Math.sqrt(c1 * c2));
  }

  private final void displayRelativeInfluence(LocalizedStringBuffer buffer, double influence, double relative, double originalRelative) {
    boolean colored = false;
    if (originalRelative >= 0) {
      if (originalRelative > relative * 1.1) {
        buffer.append("<font color='red'>");
        colored = true;
      } else if (relative > originalRelative * 1.1) {
        buffer.append("<font color='green'>");
        colored = true;
      }
    }

    displayPercentage(buffer, relative);

    if (colored) {
      buffer.append("</font>");
    }

    if (influence >= 0) {
      buffer.append(" (");
      buffer.append(String.valueOf((int) influence));
      buffer.append(")");
    }
  }

  private final void displayCombination(LocalizedStringBuffer buffer, double combination) {
    buffer.append((int) combination);
  }

  private final void displayPercentage(LocalizedStringBuffer buffer, double value) {
    String combinationText = (Math.abs(value) > 0.001) ? "00" + String.valueOf(value * 100) : "00";
    int index = combinationText.indexOf('.');
    if (index > 0) {
      combinationText = combinationText.substring(index - 2, index);
    } else {
      combinationText = combinationText.substring(combinationText.length() - 2);
    }
    buffer.append(combinationText);
    buffer.append("%");
  }

}