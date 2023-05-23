package taxi.util;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import taxi.exception.DataProcessingException;

public class ConnectionUtil {
    public static Connection getConnection() {
        try {
            Context context = new InitialContext();
            DataSource source =
                    (DataSource) context.lookup("java:comp/env/jdbc/taxi");
            return source.getConnection();
        } catch (NamingException | SQLException e) {
            throw new DataProcessingException(e.getMessage(), e);
        }
    }
}
