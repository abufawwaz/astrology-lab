package astrolab.project.test;

import astrolab.astronom.SpacetimeEvent;
import astrolab.db.Database;
import astrolab.web.SVGDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public class TestPsychoReaction extends SVGDisplay {

  private final static String PARAMETER_SEQUENCE = "_test_sequence";
  private final static String PARAMETER_TIME = "_test_time";
  private final static String PARAMETER_RECORD = "_test_record";

  private final static int ID = SVGDisplay.getId(TestPsychoReaction.class);

  private int sequence = 0;
  private long testTime = 0;

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    storeResult(request);

    if (sequence >= 50) {
      buffer.append("<text x='20' y='40'>Test is over! Do it again tomorrow.</text>\r\n"); // get text from database
    } else {
	    buffer.append("<script type=\"text/javascript\">\r\n");
	    buffer.append("//<![CDATA[\r\n");
	    buffer.append("\r\n var time;");
	    buffer.append("\r\n var pass = Math.floor(1000 * Math.random() + 500);");
	    buffer.append("\r\n setTimeout('initTest()', pass);");
	    buffer.append("\r\n function initTest() {");
	    buffer.append("\r\n  time = new Date()");
	    buffer.append("\r\n  document.getElementById('block_rect').setAttribute('visibility', 'visible')");
	    buffer.append("\r\n }");
	    buffer.append("\r\n function test(evt) {");
	    buffer.append("\r\n  document.getElementById('block_rect').setAttribute('visibility', 'hidden')");
      buffer.append("\r\n  document.location.href='view.html?_d=");
      buffer.append(ID);
      buffer.append("&");
      buffer.append(PARAMETER_SEQUENCE);
      buffer.append("=");
      buffer.append(sequence + 1);
      buffer.append("&");
      buffer.append(PARAMETER_TIME);
      buffer.append("=");
      buffer.append(testTime);
      buffer.append("&");
      buffer.append(PARAMETER_RECORD);
      buffer.append("=' + (new Date() - time)");
	    buffer.append("\r\n }");
	    buffer.append("\r\n//]]>\r\n");
	    buffer.append("</script>\r\n");

      buffer.append("<text x='40' y='20'>");
      buffer.append(sequence);
      buffer.append(" / ");
      buffer.append("50");
      buffer.append("</text>\r\n");

	    buffer.append("<a id='block' xlink:href=''>\r\n");
	    buffer.append("<rect id='block_rect' x='40' y='40' rx='3' ry='3' width='20' height='20' fill='red' stroke='none' onclick='test(evt)' visibility='hidden' />");
	    buffer.append("</a>\r\n");
    }
	}

  private void storeResult(Request request) {
    sequence = request.getParameters().getInt(PARAMETER_SEQUENCE);

    if (sequence > 0) {
      String timeText;
      testTime = (long) request.getParameters().getDouble(PARAMETER_TIME);
      int record = request.getParameters().getInt(PARAMETER_RECORD);
      int user = request.getUser();
  
      if (testTime <= 0) {
        // add a record
        testTime = System.currentTimeMillis();
        timeText = new SpacetimeEvent(testTime).toMySQLString();

        Database.execute("INSERT INTO project_test_psycho_reaction (subject_id, time) VALUES (" + user + ", '" + timeText + "')");
      } else {
        timeText = new SpacetimeEvent(testTime).toMySQLString();
      }

      Database.execute("UPDATE project_test_psycho_reaction SET r" + sequence + " = " + record + " WHERE subject_id = " + user + " AND time = '" + timeText + "'");
    }
  }

}
