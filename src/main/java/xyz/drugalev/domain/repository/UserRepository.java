package xyz.drugalev.domain.repository;

import lombok.NonNull;
import xyz.drugalev.domain.entity.User;

import java.sql.SQLException;
import java.util.Optional;

/**
 * An interface that defines operations with users in repository.
 *
 * @author Drugalev Maxim
 */
public interface UserRepository {

    /**
     * Saves given user.
     * Note: First added user will be admin.
     *
     * @param username user name.
     * @param password user password.
     */
    void save(@NonNull String username, @NonNull String password) throws SQLException;

    /**
     * Finds the user with the given username.
     *
     * @param username the username of the user to find.
     * @return an optional containing the user if it was found, or empty otherwise
     */
    Optional<User> find(@NonNull String username) throws SQLException;

    /**
     * Finds the user with the given username.
     *
     * @param id the id of the user to find.
     * @return an optional containing the user if it was found, or empty otherwise
     */
    Optional<User> find( int id) throws SQLException;
}