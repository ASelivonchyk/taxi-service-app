package taxi.controller.driver;

import static taxi.Constants.DB_ERROR_MESSAGE;
import static taxi.Constants.ERROR_MESSAGE_TAG;
import static taxi.Constants.MAIN_PACKAGE_NAME;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import taxi.exception.DataProcessingException;
import taxi.lib.Injector;
import taxi.model.Driver;
import taxi.service.DriverService;

@WebServlet(urlPatterns = "/drivers")
public class GetAllDriversController extends HttpServlet {
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final DriverService driverService = (DriverService) injector
            .getInstance(DriverService.class);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<Driver> drivers = driverService.getAll();
            req.setAttribute("drivers", drivers);
        } catch (DataProcessingException e) {
            req.setAttribute(ERROR_MESSAGE_TAG, DB_ERROR_MESSAGE);
        } finally {
            req.getRequestDispatcher("/WEB-INF/views/drivers/all.jsp").forward(req, resp);
            req.getSession().setAttribute(ERROR_MESSAGE_TAG, null);
        }
    }
}
