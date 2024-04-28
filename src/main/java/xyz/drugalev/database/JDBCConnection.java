package xyz.drugalev.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * A class that provides a connection to the PostgreSQL database.
 */
public class JDBCConnection {
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("database/credentials");
    private static final String URL = RESOURCE_BUNDLE.getString("db_url");
    private static final String DB_USER = RESOURCE_BUNDLE.getString("db_user");
    private static final String DB_PASS = RESOURCE_BUNDLE.getString("db_pass");
    /**
     * Get a connection to the PostgreSQL database.
     *
     * @return A connection to the PostgreSQL database.
     * @throws SQLException If there is a problem connecting to the database.
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return DriverManager.getConnection(URL, DB_USER, DB_PASS);
    }
}