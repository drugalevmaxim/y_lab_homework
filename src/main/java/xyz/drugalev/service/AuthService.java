package xyz.drugalev.service;

import xyz.drugalev.entity.User;
import xyz.drugalev.exception.InvalidParametersException;
import xyz.drugalev.exception.UserAlreadyExistsException;
import xyz.drugalev.exception.UserNotFoundException;

import java.sql.SQLException;

/**
 * Interface for authentication service.
 */
public interface AuthService {
    /**
     * Logs in the user with the specified username and password.
     *
     * @param username the username of the user to log in
     * @param password the password of the user to log in
     * @return the logged-in user
     */
    User login(String username, String password) throws UserNotFoundException, SQLException;

    /**
     * Registers a new user with the specified username and password.
     *
     * @param username the username of the new user
     * @param password the password of the new user
     * @return the registered user
     */
    User register(String username, String password) throws UserAlreadyExistsException, SQLException, InvalidParametersException;
}
