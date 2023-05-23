package taxi.controller.manufacturer;

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
import taxi.model.Manufacturer;
import taxi.service.ManufacturerService;

@WebServlet(urlPatterns = "/manufacturers/add")
public class AddManufacturerController extends HttpServlet {
    private static final String SUCCESS_MESSAGE = "Manufacturer successfully added to database.";
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final ManufacturerService manufacturerService = (ManufacturerService) injector
            .getInstance(ManufacturerService.class);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/manufacturers/add.jsp").forward(req, resp);
        req.getSession().setAttribute(ERROR_MESSAGE_TAG, null);
        req.getSession().setAttribute(SUCCESS_MESSAGE_TAG, null);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            manufacturerService.create(parseManufacturerFromRequest(req));
            req.getSession().setAttribute(SUCCESS_MESSAGE_TAG, SUCCESS_MESSAGE);
        } catch (DataProcessingException e) {
            req.getSession().setAttribute(ERROR_MESSAGE_TAG, DB_ERROR_MESSAGE);
        } finally {
            resp.sendRedirect(req.getContextPath() + "/manufacturers/add");
        }
    }

    private Manufacturer parseManufacturerFromRequest(HttpServletRequest req) {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName(req.getParameter("name"));
        manufacturer.setCountry(req.getParameter("country"));
        return manufacturer;
    }
}
