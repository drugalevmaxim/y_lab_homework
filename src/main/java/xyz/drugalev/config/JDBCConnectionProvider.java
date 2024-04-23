package xyz.drugalev.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * The Jdbc connection provider.
 *
 * @author Drugalev Maxim
 */
public class JDBCConnectionProvider {
    private JDBCConnectionProvider() {
    }

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("database/credentials");
    private static String url = RESOURCE_BUNDLE.getString("db_url");
    private static String db_user = RESOURCE_BUNDLE.getString("db_user");
    private static String pass = RESOURCE_BUNDLE.getString("db_pass");

    /**
     * Sets connection credentials.
     *
     * @param new_url     the new url
     * @param new_db_user the new db user
     * @param new_pass    the new pass
     */
    public static void setConnectionCredentials(String new_url, String new_db_user, String new_pass) {
        url = new_url;
        db_user = new_db_user;
        pass = new_pass;
    }

    /**
     * Get a JDBC Connection object.
     *
     * @return Connection object
     * @throws SQLException if connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, db_user, pass);
    }
}