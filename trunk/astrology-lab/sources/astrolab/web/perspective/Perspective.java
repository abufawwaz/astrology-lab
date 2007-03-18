package astrolab.web.perspective;

import astrolab.db.Database;
import astrolab.web.Display;
import astrolab.web.server.Request;
import astrolab.web.server.SessionManager;
import astrolab.web.server.content.LocalizedStringBuffer;

public class Perspective extends Display {

  private final static String PERSPECTIVE_KEY = "_perspective";

  public static boolean isPerspectiveRequest() {
    Request request = Request.getCurrentRequest();
    String requestedResource = request.get("GET");
    return ((requestedResource == null) || (requestedResource.indexOf('.') < 0));
  }

  public static int[] listPerspectives() {
    // TODO: consider the user's preferences
    return Database.queryIds("SELECT perspective_id FROM views_perspective WHERE perspective_id > 0");
  }

  public Perspective() {
    super.addAction("project", "user.session.project");
  }

  public String getType() {
    return "application/xhtml+xml";
  }

  public void fillContent(Request request, LocalizedStringBuffer buffer) {
    SessionManager.establishSession(request);

    buffer.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:svg=\"http://www.w3.org/2000/svg\">"); buffer.newline();
    buffer.append("<head>"); buffer.newline();
    buffer.append(" <META name='verify-v1' content='QugwE0IKpM7Zrdqn/7KqZGbrX1PVtMBkhx/ZCi6mvZM=' />"); buffer.newline();
    buffer.append(" <title>"); buffer.newline();
    buffer.append("  www.astrology-lab.net"); buffer.newline();
    buffer.append(" </title>"); buffer.newline();
    buffer.append("</head>"); buffer.newline();
    buffer.append(""); buffer.newline();

    buffer.append("<script language='javascript' src='./events.js' />"); buffer.newline();
    buffer.append("<script language='javascript' src='./control.js' />"); buffer.newline();
    buffer.append("<script language='javascript'>"); buffer.newline();
    buffer.append("  function switchPerspective(perspectiveId) {"); buffer.newline();
    buffer.append("    top.window.location.href = '/?");
    buffer.append(PERSPECTIVE_KEY);
    buffer.append("=' + perspectiveId"); buffer.newline();
    buffer.append("  }"); buffer.newline();
    buffer.append("</script>"); buffer.newline();

    super.fillActionScript(request, buffer);
    buffer.append(getPerspectiveHtml(request.get(PERSPECTIVE_KEY))); buffer.newline();

    buffer.append("</html>"); buffer.newline();
  }

  private String getPerspectiveHtml(String id) {
    StringBuffer query = new StringBuffer();
    query.append("SELECT perspective_html FROM views_perspective");
    query.append(" WHERE perspective_id = '");
    query.append(id);
    query.append("' OR perspective_id = 0");
    query.append(" ORDER BY perspective_id DESC LIMIT 1");

    return Database.query(query.toString());
  }

}