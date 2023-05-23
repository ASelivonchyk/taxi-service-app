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
import taxi.model.Car;
import taxi.service.CarService;

@WebServlet("/drivers/cars")
public class GetMyCurrentCarsController extends HttpServlet {
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final CarService carService = (CarService) injector.getInstance(CarService.class);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Long driverId = (Long) req.getSession().getAttribute("driver_id");
            List<Car> cars = carService.getAllByDriver(driverId);
            req.setAttribute("cars", cars);
        } catch (DataProcessingException e) {
            req.setAttribute(ERROR_MESSAGE_TAG, DB_ERROR_MESSAGE);
        }
        req.getRequestDispatcher("/WEB-INF/views/cars/all.jsp").forward(req, resp);
    }
}
