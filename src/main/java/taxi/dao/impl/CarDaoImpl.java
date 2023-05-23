package taxi.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import taxi.dao.CarDao;
import taxi.exception.DataProcessingException;
import taxi.lib.Dao;
import taxi.model.Car;
import taxi.model.Driver;
import taxi.model.Manufacturer;
import taxi.util.ConnectionUtil;

@Dao
public class CarDaoImpl implements CarDao {
    private static final int ZERO_PLACEHOLDER = 0;
    private static final int SHIFT = 2;

    private static final String CREATE_CAR_QUERY = "INSERT INTO cars (model, manufacturer_id)"
            + "VALUES (?, ?)";
    private static final String GET_CAR_QUERY = "SELECT c.id as id, model, manufacturer_id, "
            + "m.name as manufacturer_name, "
            + "m.country as manufacturer_country "
            + "FROM cars c "
            + "JOIN manufacturers m on c.manufacturer_id = m.id "
            + "where c.id = ? AND c.is_deleted = false";
    private static final String GET_ALL_CARS_QUERY = "SELECT c.id as id, "
            + "model, "
            + "manufacturer_id, "
            + "m.name as manufacturer_name, "
            + "m.country as manufacturer_country "
            + "FROM cars c"
            + " JOIN manufacturers m on c.manufacturer_id = m.id"
            + " where c.is_deleted = false";
    private static final String UPDATE_CAR_QUERY = "UPDATE cars SET model = ?, manufacturer_id = ? "
            + "WHERE id = ? and is_deleted = false";
    private static final String SOFT_DELETE_CAR_QUERY = "UPDATE cars SET is_deleted = true "
            + "WHERE id = ? and is_deleted = false";
    private static final String GET_ALL_CARS_BY_DRIVER_QUERY = "SELECT c.id as id, model, "
            + "manufacturer_id, m.name as manufacturer_name, m.country as manufacturer_country "
            + "FROM cars c "
            + "JOIN manufacturers m on c.manufacturer_id = m.id "
            + "JOIN cars_drivers cd on c.id = cd.car_id "
            + "JOIN drivers d on cd.driver_id = d.id "
            + "where c.is_deleted = false and driver_id = ? "
            + "and d.is_deleted = false";
    private static final String GET_ALL_CAR_DRIVERS_QUERY = "SELECT id, name, license_number, "
            + "login FROM cars_drivers cd "
            + "JOIN drivers d on cd.driver_id = d.id "
            + "where car_id = ? AND is_deleted = false";
    private static final Logger logger;

    static {
        logger = Logger.getLogger(CarDaoImpl.class);
    }

    @Override
    public Car create(Car car) {
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement createCarStatement =
                        connection.prepareStatement(
                                CREATE_CAR_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            createCarStatement.setString(1, car.getModel());
            createCarStatement.setLong(2, car.getManufacturer().getId());
            createCarStatement.executeUpdate();
            ResultSet resultSet = createCarStatement.getGeneratedKeys();
            if (resultSet.next()) {
                car.setId(resultSet.getObject(1, Long.class));
            }
        } catch (SQLException e) {
            logger.error("SQLError while adding Car: " + car + " to DB. " + e.getMessage());
            throw new DataProcessingException("Can't create car " + car, e);
        }
        insertAllDrivers(car);
        return car;
    }

    @Override
    public Optional<Car> get(Long id) {
        Car car = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getCarStatement =
                        connection.prepareStatement(GET_CAR_QUERY)) {
            getCarStatement.setLong(1, id);
            ResultSet resultSet = getCarStatement.executeQuery();
            if (resultSet.next()) {
                car = parseCarFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            logger.error("SQLError while getting Car: " + id + " from DB. " + e.getMessage());
            throw new DataProcessingException("Can't get car by id: " + id, e);
        }
        if (car != null) {
            car.setDrivers(getAllDriversByCarId(car.getId()));
        }
        return Optional.ofNullable(car);
    }

    @Override
    public List<Car> getAll() {
        List<Car> cars = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getAllCarsStatement =
                        connection.prepareStatement(GET_ALL_CARS_QUERY)) {
            ResultSet resultSet = getAllCarsStatement.executeQuery();
            while (resultSet.next()) {
                cars.add(parseCarFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            logger.error("SQLError while getting all Cars from DB. " + e.getMessage());
            throw new DataProcessingException("Can't get all cars", e);
        }
        cars.forEach(car -> car.setDrivers(getAllDriversByCarId(car.getId())));
        return cars;
    }

    @Override
    public Car update(Car car) {
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement updateCarStatement =
                        connection.prepareStatement(UPDATE_CAR_QUERY)) {
            updateCarStatement.setString(1, car.getModel());
            updateCarStatement.setLong(2, car.getManufacturer().getId());
            updateCarStatement.setLong(3, car.getId());
            updateCarStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQLError while updating Car: " + car + " in DB. " + e.getMessage());
            throw new DataProcessingException("Can't update car " + car, e);
        }
        deleteAllDriversExceptList(car);
        insertAllDrivers(car);
        return car;
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement deleteCarStatement =
                         connection.prepareStatement(SOFT_DELETE_CAR_QUERY)) {
            deleteCarStatement.setLong(1, id);
            return deleteCarStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("SQLError while soft deleting Car by id: "
                    + id + " in DB. " + e.getMessage());
            throw new DataProcessingException("Can't delete car by id " + id, e);
        }
    }

    @Override
    public List<Car> getAllByDriver(Long driverId) {
        List<Car> cars = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getAllCarsByDriverStatement =
                        connection.prepareStatement(GET_ALL_CARS_BY_DRIVER_QUERY)) {
            getAllCarsByDriverStatement.setLong(1, driverId);
            ResultSet resultSet = getAllCarsByDriverStatement.executeQuery();
            while (resultSet.next()) {
                cars.add(parseCarFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            logger.error("SQLError getting Cars by Driver id: "
                    + driverId + " from DB. " + e.getMessage());
            throw new DataProcessingException("Can't get all cars by driver id:" + driverId, e);
        }
        cars.forEach(car -> car.setDrivers(getAllDriversByCarId(car.getId())));
        return cars;
    }

    private void insertAllDrivers(Car car) {
        Long carId = car.getId();
        List<Driver> drivers = car.getDrivers();
        if (drivers.size() == 0) {
            return;
        }
        String insertQuery = "INSERT INTO cars_drivers (car_id, driver_id) VALUES "
                + drivers.stream().map(driver -> "(?, ?)").collect(Collectors.joining(", "))
                + " ON DUPLICATE KEY UPDATE car_id = car_id";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement linkDriverToCarStatement =
                        connection.prepareStatement(insertQuery)) {
            for (int i = 0; i < drivers.size(); i++) {
                Driver driver = drivers.get(i);
                linkDriverToCarStatement.setLong((i * SHIFT) + 1, carId);
                linkDriverToCarStatement.setLong((i * SHIFT) + 2, driver.getId());
            }
            linkDriverToCarStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQLError while inserting drivers: "
                    + drivers + " to car: " + car + ", " + e.getMessage());
            throw new DataProcessingException("Can't insert drivers " + drivers, e);
        }
    }

    private void deleteAllDriversExceptList(Car car) {
        Long carId = car.getId();
        List<Driver> exceptions = car.getDrivers();
        int size = exceptions.size();
        String insertQuery = "DELETE FROM cars_drivers WHERE car_id = ? "
                + "AND NOT driver_id IN ("
                + ZERO_PLACEHOLDER + ", ?".repeat(size)
                + ");";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement deleteAllDriversExceptLinkedStatement =
                        connection.prepareStatement(insertQuery)) {
            deleteAllDriversExceptLinkedStatement.setLong(1, carId);
            for (int i = 0; i < size; i++) {
                Driver driver = exceptions.get(i);
                deleteAllDriversExceptLinkedStatement.setLong(
                        (i) + SHIFT, driver.getId());
            }
            deleteAllDriversExceptLinkedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQLError while deleting drivers from car: "
                    + car + ", " + e.getMessage());
            throw new DataProcessingException("Can't delete drivers " + exceptions, e);
        }
    }

    private List<Driver> getAllDriversByCarId(Long carId) {
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getDriversByCarIdStatement =
                        connection.prepareStatement(GET_ALL_CAR_DRIVERS_QUERY)) {
            getDriversByCarIdStatement.setLong(1, carId);
            ResultSet resultSet = getDriversByCarIdStatement.executeQuery();
            List<Driver> drivers = new ArrayList<>();
            while (resultSet.next()) {
                drivers.add(parseDriverFromResultSet(resultSet));
            }
            return drivers;
        } catch (SQLException e) {
            logger.error("SQLError while getting car drivers by car id: "
                    + carId + ", " + e.getMessage());
            throw new DataProcessingException("Can't get all drivers by car id" + carId, e);
        }
    }

    private Driver parseDriverFromResultSet(ResultSet resultSet) throws SQLException {
        Driver driver = new Driver();
        driver.setId(resultSet.getObject("id", Long.class));
        driver.setName(resultSet.getNString("name"));
        driver.setLicenseNumber(resultSet.getNString("license_number"));
        driver.setLogin(resultSet.getNString("login"));
        return driver;
    }

    private Car parseCarFromResultSet(ResultSet resultSet) throws SQLException {
        Car car = new Car();
        car.setId(resultSet.getObject("id", Long.class));
        car.setModel(resultSet.getNString("model"));
        car.setManufacturer(parseManufacturerFromResultSet(resultSet));
        return car;
    }

    private Manufacturer parseManufacturerFromResultSet(ResultSet resultSet) throws SQLException {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(resultSet.getObject("manufacturer_id", Long.class));
        manufacturer.setName(resultSet.getNString("manufacturer_name"));
        manufacturer.setCountry(resultSet.getNString("manufacturer_country"));
        return manufacturer;
    }
}
