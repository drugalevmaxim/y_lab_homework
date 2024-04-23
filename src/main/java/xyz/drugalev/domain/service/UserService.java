package xyz.drugalev.domain.service;

import lombok.NonNull;
import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.exception.ValidationException;

import java.sql.SQLException;
import java.util.Optional;

/**
 * An interface that defines user service.
 *
 * @author Drugalev Maxim
 */
public interface UserService {
    /**
     * Saves given user.
     *
     * @param username username to save.
     * @param password user password to save.
     * @throws SQLException        the sql exception
     * @throws ValidationException the validation exception
     */
    void save(@NonNull String username, @NonNull String password) throws SQLException, ValidationException;

    /**
     * Finds a user by its username.
     *
     * @param username users username.
     * @return User if found, empty otherwise, wrapped in an {@link Optional}.
     * @throws SQLException the sql exception
     */
    Optional<User> findUser(@NonNull String username) throws SQLException;
}