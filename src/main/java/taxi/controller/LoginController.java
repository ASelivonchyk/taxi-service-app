package taxi.controller;

import static taxi.Constants.ERROR_MESSAGE_TAG;
import static taxi.Constants.MAIN_PACKAGE_NAME;
import static taxi.Constants.SUCCESS_MESSAGE_TAG;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import taxi.exception.AuthenticationException;
import taxi.lib.Injector;
import taxi.model.Driver;
import taxi.service.AuthenticationService;

@WebServlet(urlPatterns = "/")
public class LoginController extends HttpServlet {
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final AuthenticationService authenticationService =
            (AuthenticationService) injector.getInstance(AuthenticationService.class);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (req.getSession().getAttribute("driver_id") == null) {
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            deleteInfoMessagesFromSession(req);
        }
        resp.sendRedirect("/index");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Driver driver = authenticationService.login(
                    req.getParameter("login"), req.getParameter("password"));
            req.getSession().setAttribute("driver_id", driver.getId());
            resp.sendRedirect("/index");
        } catch (AuthenticationException e) {
            req.setAttribute("errorMsg", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }

    private void deleteInfoMessagesFromSession(HttpServletRequest req) {
        req.getSession().setAttribute(ERROR_MESSAGE_TAG, null);
        req.getSession().setAttribute(SUCCESS_MESSAGE_TAG, null);
    }
}
