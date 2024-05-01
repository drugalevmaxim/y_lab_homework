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
    private static String dbUrl = RESOURCE_BUNDLE.getString("db_url");
    private static String dbUser = RESOURCE_BUNDLE.getString("db_user");
    private static String dbPass = RESOURCE_BUNDLE.getString("db_pass");

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
        return DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }

    public static void setConnectionCredentials(String newDbUrl, String newDbUser, String newDbPass) {
        dbUrl = newDbUrl;
        dbUser = newDbUser;
        dbPass = newDbPass;
    }
}