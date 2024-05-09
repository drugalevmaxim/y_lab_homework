
package xyz.drugalev.repository;

import xyz.drugalev.entity.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Interface for working with the user table in the database.
 */
public interface UserRepository {
    /**
     * Saves a new user to the database.
     *
     * @param user the user to save
     * @return the saved user
     * @throws SQLException if there is an error saving the user to the database
     */
    User save(User user) throws SQLException;

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user to find
     * @return an optional containing the user, or an empty optional if the user does not exist
     * @throws SQLException if there is an error querying the database
     */
    Optional<User> find(String username) throws SQLException;

    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user to find
     * @return an optional containing the user, or an empty optional if the user does not exist
     * @throws SQLException if there is an error querying the database
     */
    Optional<User> find(int id) throws SQLException;

    /**
     * Finds all users from the database.
     *
     * @return a list of all users
     * @throws SQLException if there is an error retrieving the users from the database
     */
    List<User> findAll() throws SQLException;
}