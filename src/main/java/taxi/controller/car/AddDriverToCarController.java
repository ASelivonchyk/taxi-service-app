package taxi.controller.car;

import static taxi.Constants.DB_ERROR_MESSAGE;
import static taxi.Constants.ERROR_MESSAGE_TAG;
import static taxi.Constants.MAIN_PACKAGE_NAME;
import static taxi.Constants.SUCCESS_MESSAGE_TAG;

import java.io.IOException;
import java.util.NoSuchElementException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import taxi.exception.DataProcessingException;
import taxi.lib.Injector;
import taxi.model.Car;
import taxi.model.Driver;
import taxi.service.CarService;
import taxi.service.DriverService;

@WebServlet(urlPatterns = "/cars/drivers/add")
public class AddDriverToCarController extends HttpServlet {
    private static final String SUCCESS_MESSAGE = "Driver successfully added to Car";
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final CarService carService =
            (CarService) injector.getInstance(CarService.class);
    private final DriverService driverService =
            (DriverService) injector.getInstance(DriverService.class);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/cars/drivers/add.jsp").forward(req, resp);
        deleteInfoMessagesFromSession(req);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            Driver driver = driverService.get(Long.parseLong(req.getParameter("driver_id")));
            Car car = carService.get(Long.parseLong(req.getParameter("car_id")));
            carService.addDriverToCar(driver, car);
            req.getSession().setAttribute(
                    SUCCESS_MESSAGE_TAG, SUCCESS_MESSAGE);
        } catch (NoSuchElementException e) {
            req.getSession().setAttribute(ERROR_MESSAGE_TAG, e.getMessage());
        } catch (DataProcessingException e) {
            req.getSession().setAttribute(ERROR_MESSAGE_TAG, DB_ERROR_MESSAGE);
        } finally {
            resp.sendRedirect("/cars/drivers/add");
        }
    }

    private void deleteInfoMessagesFromSession(HttpServletRequest req) {
        req.getSession().setAttribute(ERROR_MESSAGE_TAG, null);
        req.getSession().setAttribute(SUCCESS_MESSAGE_TAG, null);
    }
}
