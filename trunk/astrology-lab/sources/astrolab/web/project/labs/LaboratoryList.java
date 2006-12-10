package astrolab.web.project.labs;

import astrolab.db.Personalize;
import astrolab.db.Project;
import astrolab.db.ProjectIterator;
import astrolab.db.Text;
import astrolab.web.HTMLDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class LaboratoryList extends HTMLDisplay {

	public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    int selected_lab = Personalize.getFavourite(-1, Text.getId("user.session.laboratory"), -1);
    int row = 0;

    buffer.localize("The laboratories are:");
		buffer.append("<hr />");
    ProjectIterator laboratories = ProjectIterator.iterateLabs();
		while (laboratories.hasNext()) {
			Project lab = laboratories.next();

      buffer.append("<a href='rot.html?_s=");
      buffer.append(lab.getLaboratory());
      buffer.append("'>");
      if (selected_lab == lab.getLaboratory()) {
        buffer.append("<b><font color='green'>");
      }
			buffer.append(Text.getText(lab.getLaboratory()));
      if (selected_lab == lab.getLaboratory()) {
        buffer.append("</font></b>");
      }
      buffer.append("</a>");
      buffer.append("<br />");
      row++;
		}
	}

}
