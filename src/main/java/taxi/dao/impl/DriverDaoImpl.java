package taxi.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.log4j.Logger;
import taxi.dao.DriverDao;
import taxi.exception.DataProcessingException;
import taxi.lib.Dao;
import taxi.model.Driver;
import taxi.util.ConnectionUtil;

@Dao
public class DriverDaoImpl implements DriverDao {
    private static final String CREATE_DRIVER_QUERY = "INSERT INTO drivers "
            + "(name, license_number, login, password) VALUES (?, ?, ?, ?)";
    private static final String GET_DRIVER_BY_ID_QUERY = "SELECT * "
            + "FROM drivers WHERE id = ? AND is_deleted = FALSE";
    private static final String FIND_DRIVER_BY_LOGIN_QUERY = "SELECT * "
            + "FROM drivers WHERE login = ? AND is_deleted = FALSE";
    private static final String GET_ALL_DRIVERS_QUERY = "SELECT * "
            + "FROM drivers WHERE is_deleted = FALSE";
    private static final String UPDATE_DRIVER_QUERY = "UPDATE drivers "
            + "SET name = ?, license_number = ?, login = ?, "
            + "password = ? WHERE id = ? AND is_deleted = FALSE";
    private static final String SOFT_DELETE_DRIVER_QUERY = "UPDATE drivers "
            + "SET is_deleted = TRUE WHERE id = ?";
    private static final Logger logger;

    static {
        logger = Logger.getLogger(DriverDaoImpl.class);
    }

    @Override
    public Driver create(Driver driver) {
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement createDriverStatement =
                        connection.prepareStatement(CREATE_DRIVER_QUERY,
                                Statement.RETURN_GENERATED_KEYS)) {
            createDriverStatement.setString(1, driver.getName());
            createDriverStatement.setString(2, driver.getLicenseNumber());
            createDriverStatement.setString(3, driver.getLogin());
            createDriverStatement.setString(4, driver.getPassword());
            createDriverStatement.executeUpdate();
            ResultSet resultSet = createDriverStatement.getGeneratedKeys();
            if (resultSet.next()) {
                driver.setId(resultSet.getObject(1, Long.class));
            }
            return driver;
        } catch (SQLException e) {
            logger.error("SQLError while adding Driver: " + driver + " to DB. " + e.getMessage());
            throw new DataProcessingException("Couldn't create driver: " + driver + ". ", e);
        }
    }

    @Override
    public Optional<Driver> get(Long id) {
        Driver driver = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getDriverStatement =
                        connection.prepareStatement(GET_DRIVER_BY_ID_QUERY)) {
            getDriverStatement.setLong(1, id);
            ResultSet resultSet = getDriverStatement.executeQuery();
            if (resultSet.next()) {
                driver = parseDriverFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            logger.error("SQLError while getting Driver id: " + id + " from DB. " + e.getMessage());
            throw new DataProcessingException("Couldn't get driver by id " + id, e);
        }
        return Optional.ofNullable(driver);
    }

    @Override
    public Optional<Driver> findByLogin(String login) {
        Driver driver = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getDriverStatement =
                        connection.prepareStatement(FIND_DRIVER_BY_LOGIN_QUERY)) {
            getDriverStatement.setString(1, login);
            ResultSet resultSet = getDriverStatement.executeQuery();
            if (resultSet.next()) {
                driver = parseDriverFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            logger.error("SQLError while getting Driver by login: "
                    + login + " from DB. " + e.getMessage());
            throw new DataProcessingException("Couldn't get driver by login " + login, e);
        }
        return Optional.ofNullable(driver);
    }

    @Override
    public List<Driver> getAll() {
        List<Driver> drivers = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getAllDriversStatement =
                        connection.prepareStatement(GET_ALL_DRIVERS_QUERY)) {
            ResultSet resultSet = getAllDriversStatement.executeQuery();
            while (resultSet.next()) {
                drivers.add(parseDriverFromResultSet(resultSet));
            }
            return drivers;
        } catch (SQLException e) {
            logger.error("SQLError while getting Drivers from DB. " + e.getMessage());
            throw new DataProcessingException("Couldn't get a list of drivers from driversDB.", e);
        }
    }

    @Override
    public Driver update(Driver driver) {
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement updateDriverStatement
                        = connection.prepareStatement(UPDATE_DRIVER_QUERY)) {
            updateDriverStatement.setString(1, driver.getName());
            updateDriverStatement.setString(2, driver.getLicenseNumber());
            updateDriverStatement.setString(3, driver.getLogin());
            updateDriverStatement.setString(4, driver.getPassword());
            updateDriverStatement.setLong(5, driver.getId());
            updateDriverStatement.executeUpdate();
            return driver;
        } catch (SQLException e) {
            logger.error("SQLError while updating Driver: " + driver + " in DB." + e.getMessage());
            throw new DataProcessingException("Couldn't update " + driver + " in driversDB.", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement deleteDriverStatement =
                        connection.prepareStatement(SOFT_DELETE_DRIVER_QUERY)) {
            deleteDriverStatement.setLong(1, id);
            return deleteDriverStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("SQLError while soft deleting Driver id: "
                    + id + " in DB." + e.getMessage());
            throw new DataProcessingException("Couldn't delete driver with id " + id, e);
        }
    }

    private Driver parseDriverFromResultSet(ResultSet resultSet) throws SQLException {
        Driver driver = new Driver();
        driver.setId(resultSet.getObject("id", Long.class));
        driver.setName(resultSet.getString("name"));
        driver.setLicenseNumber(resultSet.getString("license_number"));
        driver.setLogin(resultSet.getString("login"));
        driver.setPassword(resultSet.getString(("password")));
        return driver;
    }

}
