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
import taxi.service.CarService;
import taxi.service.ManufacturerService;

@WebServlet(urlPatterns = "/cars/add")
public class AddCarController extends HttpServlet {
    private static final String SUCCESS_MESSAGE = "Car successfully added to database";
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final CarService carService =
            (CarService) injector.getInstance(CarService.class);
    private final ManufacturerService manufacturerService =
            (ManufacturerService) injector.getInstance(ManufacturerService.class);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/cars/add.jsp").forward(req, resp);
        deleteInfoMessagesFromSession(req);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            carService.create(parseCarFromRequest(req));
            req.getSession().setAttribute(SUCCESS_MESSAGE_TAG, SUCCESS_MESSAGE);
        } catch (NoSuchElementException e) {
            req.getSession().setAttribute(ERROR_MESSAGE_TAG, e.getMessage());
        } catch (DataProcessingException e) {
            req.getSession().setAttribute(ERROR_MESSAGE_TAG, DB_ERROR_MESSAGE);
        } finally {
            resp.sendRedirect("/cars/add");
        }
    }

    private Car parseCarFromRequest(HttpServletRequest req) {
        Car car = new Car();
        car.setModel(req.getParameter("model"));
        car.setManufacturer(manufacturerService.get(
                Long.parseLong(req.getParameter("manufacturer_id"))));
        return car;
    }

    private void deleteInfoMessagesFromSession(HttpServletRequest req) {
        req.getSession().setAttribute(ERROR_MESSAGE_TAG, null);
        req.getSession().setAttribute(SUCCESS_MESSAGE_TAG, null);
    }
}
