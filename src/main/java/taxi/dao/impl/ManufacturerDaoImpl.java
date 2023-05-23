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
import taxi.dao.ManufacturerDao;
import taxi.exception.DataProcessingException;
import taxi.lib.Dao;
import taxi.model.Manufacturer;
import taxi.util.ConnectionUtil;

@Dao
public class ManufacturerDaoImpl implements ManufacturerDao {
    private static final String CREATE_MANUFACTURER_QUERY =
            "INSERT INTO manufacturers (name, country) VALUES (?,?)";
    private static final String GET_MANUFACTURER_BY_ID_QUERY =
            "SELECT * FROM manufacturers WHERE id = ? AND is_deleted = FALSE";
    private static final String GET_ALL_MANUFACTURER_QUERY =
            "SELECT * FROM manufacturers WHERE is_deleted = FALSE";
    private static final String UPDATE_MANUFACTURER_QUERY =
            "UPDATE manufacturers SET name = ?, country = ? "
                    + "WHERE id = ? AND is_deleted = FALSE";
    private static final String SOFT_DELETE_MANUFACTURER_QUERY =
            "UPDATE manufacturers SET is_deleted = TRUE WHERE id = ?";

    private static final Logger logger;

    static {
        logger = Logger.getLogger(ManufacturerDaoImpl.class);
    }

    @Override
    public Manufacturer create(Manufacturer manufacturer) {
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                                CREATE_MANUFACTURER_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            setUpdate(statement, manufacturer).executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                manufacturer.setId(resultSet.getObject(1, Long.class));
            }
            return manufacturer;
        } catch (SQLException e) {
            logger.error("SQLError while adding Manufacturer: "
                    + manufacturer + " to DB. " + e.getMessage());
            throw new DataProcessingException("Can't create manufacturer " + manufacturer, e);
        }
    }

    @Override
    public Optional<Manufacturer> get(Long id) {
        Manufacturer manufacturer = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(GET_MANUFACTURER_BY_ID_QUERY)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                manufacturer = parseManufacturerFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            logger.error("SQLError while getting Manufacturer id: "
                    + id + " from DB. " + e.getMessage());
            throw new DataProcessingException("Can't get manufacturer by id " + id, e);
        }
        return Optional.ofNullable(manufacturer);
    }

    @Override
    public List<Manufacturer> getAll() {
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(GET_ALL_MANUFACTURER_QUERY)) {
            List<Manufacturer> manufacturers = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                manufacturers.add(parseManufacturerFromResultSet(resultSet));
            }
            return manufacturers;
        } catch (SQLException e) {
            logger.error("SQLError while getting Manufacturers from DB. " + e.getMessage());
            throw new DataProcessingException("Can't get a list of manufacturers.", e);
        }
    }

    @Override
    public Manufacturer update(Manufacturer manufacturer) {
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = setUpdate(
                        connection.prepareStatement(UPDATE_MANUFACTURER_QUERY), manufacturer)) {
            statement.setLong(3, manufacturer.getId());
            statement.executeUpdate();
            return manufacturer;
        } catch (SQLException e) {
            logger.error("SQLError while updating Manufacturer: "
                    + manufacturer + " in DB. " + e.getMessage());
            throw new DataProcessingException("Can't update a manufacturer "
                    + manufacturer, e);
        }
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(SOFT_DELETE_MANUFACTURER_QUERY)) {
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("SQLError while soft deleting Manufacturer id: "
                    + id + " from DB. " + e.getMessage());
            throw new DataProcessingException("Can't delete a manufacturer by id " + id, e);
        }
    }

    private Manufacturer parseManufacturerFromResultSet(ResultSet resultSet) throws SQLException {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(resultSet.getObject("id", Long.class));
        manufacturer.setName(resultSet.getString("name"));
        manufacturer.setCountry(resultSet.getString("country"));
        return manufacturer;
    }

    private PreparedStatement setUpdate(PreparedStatement statement,
                                        Manufacturer manufacturer) throws SQLException {
        statement.setString(1, manufacturer.getName());
        statement.setString(2, manufacturer.getCountry());
        return statement;
    }
}
