package astrolab;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.*;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import open.osiva.core.Center;
import open.osiva.core.view.PageSourceBuilder;

@SuppressWarnings("serial")
public class EndpointServlet extends HttpServlet {

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Principal user = request.getUserPrincipal();

    if (user != null) {
      String service = request.getServletPath();
  
      if (service.indexOf('.') >= 0) {
        serveStaticPage(request, response);
      } else {
        serveDynamicPage(user.getName(), request, response);
      }
    } else {
      UserService userService = UserServiceFactory.getUserService();

      System.err.println(request.getRequestURL().toString());
      response.getWriter().println("<p>Please, <a href=\"" + userService.createLoginURL(request.getRequestURL().toString()) + "\">sign in</a>!</p>");
    }
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Principal user = request.getUserPrincipal();

    if (user != null) {
      serveAjaxRequest(user.getName(), request, response);
    } else {
      response.getWriter().println("<p>You should have already authenticated! Either your session is over or you used an outdated link!</p>");
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);      
    }
  }

  private void serveStaticPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String page = request.getServletPath();
    response.setContentType(StaticPage.getContentType(page));
    response.getOutputStream().write(StaticPage.getStaticPage(page));
    response.getOutputStream().flush();
  }

  private void serveDynamicPage(String user, HttpServletRequest request, HttpServletResponse response) throws IOException {
    PageSourceBuilder page = new PageSourceBuilder();;

    page.startLine("<html>");
    page.goIn();
    page.startLine("<head>");
    page.goIn();
    page.startLine("<script src='/ajax.js'></script>");
    page.startLine("<script src='/controls.js'></script>");
    page.goOut();
    page.startLine("</head>");
    page.startLine("<body>");
    page.startLine(user);
    page.startLine(", you are in project " + request.getRequestURI());
    page.goIn();
    page.startLine("<form>");
    page.goIn();

    Center.view(user, page, readService(request));

    page.goOut();
    page.startLine("</form>");
    page.goOut();
    page.startLine("</body>");
    page.goOut();
    page.startLine("</html>");

    response.setContentType("text/html");
    response.getOutputStream().print(page.toString());
  }

  private void serveAjaxRequest(String user, HttpServletRequest request, HttpServletResponse response) throws IOException {
    PageSourceBuilder page = new PageSourceBuilder();

    Center.call(user, page, readService(request), request.getParameter("action"));

    response.getOutputStream().print(page.toString());
  }

  private final String readService(HttpServletRequest request) {
    String service = request.getServletPath();

    if (service.startsWith("/")) {
      service = service.substring(1);
    }

    return service.replaceAll("/", ".");
  }

}