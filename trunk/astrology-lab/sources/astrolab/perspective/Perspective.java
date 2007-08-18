package astrolab.perspective;

import java.io.File;
import java.io.FileInputStream;

import astrolab.astronom.SpacetimeEvent;
import astrolab.db.Database;
import astrolab.db.Project;
import astrolab.project.ProjectDataKey;
import astrolab.project.Projects;
import astrolab.web.Display;
import astrolab.web.server.Request;
import astrolab.web.server.RequestParameters;
import astrolab.web.server.SessionManager;
import astrolab.web.server.content.LocalizedStringBuffer;

public class Perspective extends Display {

  private final static String PERSPECTIVE_KEY = "_perspective";
  private final static int PERSPECTIVE_STATISTICS = 40041; // Get from database

  public static boolean isPerspectiveRequest() {
    Request request = Request.getCurrentRequest();
    String requestedResource = request.get("GET");
    return ((requestedResource == null) || (requestedResource.indexOf('.') < 0));
  }

  public static int[] listPerspectives() {
    // TODO: consider the user's preferences
    return Database.queryIds("SELECT perspective_id FROM views_perspective WHERE perspective_id > 0");
  }

  // TODO: Make per perspective if a second one demands it
  public static boolean isProjectAccepted(Project projectRecord) {
    switch (getPerspectiveId()) {
      case PERSPECTIVE_STATISTICS: {
        astrolab.project.Project project = Projects.getProject(projectRecord.getId());

        for (ProjectDataKey key: project.getKeys()) {
          if ("time".equalsIgnoreCase(key.getName())) {
            return true;
          }
        }

        return false;
      }
    }
    return true;
  }

  public Perspective() {
    super.addAction("project", RequestParameters.PROJECT_ID);
  }

  public String getType() {
    return "application/xhtml+xml";
  }

  public void fillContent(Request request, LocalizedStringBuffer buffer) {
    SessionManager.establishSession(request);

    buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>"); buffer.newline();
    buffer.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Frameset//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd\">"); buffer.newline();
    buffer.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">"); buffer.newline();
//    buffer.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:svg=\"http://www.w3.org/2000/svg\">"); buffer.newline();
    buffer.append("<head>"); buffer.newline();
    buffer.append(" <META name='verify-v1' content='QugwE0IKpM7Zrdqn/7KqZGbrX1PVtMBkhx/ZCi6mvZM=' />"); buffer.newline();
    buffer.append(" <title>"); buffer.newline();
    buffer.append("  www.astrology-lab.net"); buffer.newline();
    buffer.append(" </title>"); buffer.newline();

//    buffer.append("<script language='javascript' src='./events.js' />"); buffer.newline();
//    buffer.append("<script language='javascript' src='./control.js' />"); buffer.newline();

    buffer.append("</head>"); buffer.newline();
    buffer.append(""); buffer.newline();

    includeScripts(request, buffer);

    buffer.append(getPerspectiveHtml()); buffer.newline();

    buffer.append("</html>"); buffer.newline();
  }

  private final static int getPerspectiveId() {
    int id = Request.getCurrentRequest().getParameters().getInt(PERSPECTIVE_KEY);

    if (id <= 0) {
      int user = Request.getCurrentRequest().getUser();
      String textId = Database.query("SELECT perspective_id FROM project_webstats WHERE subject_id = " + user + " ORDER BY time DESC LIMIT 1");

      if (textId != null) {
        return Integer.parseInt(textId);
      }
    }

    return id;
  }

  private String getPerspectiveHtml() {
    int perspectiveId = getPerspectiveId();
    int user = Request.getCurrentRequest().getUser();
    int project = Request.getCurrentRequest().getParameters().getInt(RequestParameters.PROJECT_ID);
    SpacetimeEvent time = new SpacetimeEvent(System.currentTimeMillis(), SpacetimeEvent.GMT_TIME_ZONE);
    StringBuffer webstatsUpdate = new StringBuffer();
    webstatsUpdate.append("INSERT INTO project_webstats VALUES (");
    webstatsUpdate.append(user);
    webstatsUpdate.append(", '");
    webstatsUpdate.append(time.toMySQLString());
    webstatsUpdate.append("', ");
    webstatsUpdate.append(perspectiveId);
    webstatsUpdate.append(", ");
    webstatsUpdate.append(project);
    webstatsUpdate.append(")");
    Database.execute(webstatsUpdate.toString());

    StringBuffer query = new StringBuffer();
    query.append("SELECT perspective_html FROM views_perspective");
    query.append(" WHERE perspective_id = '");
    query.append(perspectiveId);
    query.append("' OR perspective_id = 0");
    query.append(" ORDER BY perspective_id DESC LIMIT 1");

    return Database.query(query.toString());
  }

  private void includeScripts(Request request, LocalizedStringBuffer buffer) {
    try {
      File scripts = new File("./scripts");
      File[] files = scripts.listFiles();

      buffer.newline();
      buffer.append("<script language='javascript'>");
      buffer.newline();
      buffer.append("//<![CDATA[");
      buffer.newline();
      for (int i = 0; i < files.length; i++) {
        if (files[i].getName().endsWith("js")) {
          includeScript(files[i], buffer);
        }
      }
      buffer.newline();
      super.fillActionScript(request, buffer, true);
      buffer.newline();
      buffer.append("//]]>");
      buffer.newline();
      buffer.append("</script>");
      buffer.newline();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void includeScript(File file, LocalizedStringBuffer buffer) {
    try {
      FileInputStream fis = new FileInputStream(file);
      int read;
      byte[] data = new byte[500];
      while (fis.available() > 0) {
        read = fis.read(data);
        buffer.append(new String(data, 0, read));
      }
      fis.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}