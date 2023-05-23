package taxi.controller.driver;

import static taxi.Constants.DB_ERROR_MESSAGE;
import static taxi.Constants.ERROR_MESSAGE_TAG;
import static taxi.Constants.MAIN_PACKAGE_NAME;
import static taxi.Constants.SUCCESS_MESSAGE_TAG;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import taxi.exception.DataProcessingException;
import taxi.lib.Injector;
import taxi.model.Driver;
import taxi.service.DriverService;

@WebServlet(urlPatterns = "/drivers/add")
public class AddDriverController extends HttpServlet {
    private static final String SUCCESS_MESSAGE = "Successfully registered in service.";
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final DriverService driverService = (DriverService) injector
            .getInstance(DriverService.class);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/drivers/add.jsp").forward(req, resp);
        deleteInfoMessagesFromSession(req);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        req.setCharacterEncoding("utf8");
        try {
            driverService.create(parseDriverFromRequest(req));
            req.getSession().setAttribute(SUCCESS_MESSAGE_TAG, SUCCESS_MESSAGE);
            resp.sendRedirect("/login");
        } catch (DataProcessingException e) {
            req.getSession().setAttribute(ERROR_MESSAGE_TAG, DB_ERROR_MESSAGE);
            resp.sendRedirect("/drivers/add");
        }
    }

    private Driver parseDriverFromRequest(HttpServletRequest req) {
        Driver driver = new Driver();
        driver.setName(req.getParameter("name"));
        driver.setLogin(req.getParameter("login"));
        driver.setLicenseNumber(req.getParameter("license"));
        driver.setPassword(req.getParameter("password"));
        return driver;
    }

    private void deleteInfoMessagesFromSession(HttpServletRequest req) {
        req.getSession().setAttribute(ERROR_MESSAGE_TAG, null);
        req.getSession().setAttribute(SUCCESS_MESSAGE_TAG, null);
    }
}
