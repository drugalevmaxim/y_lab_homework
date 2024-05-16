package xyz.drugalev.service;

import xyz.drugalev.util.JwtToken;
import xyz.drugalev.dto.UserDto;
import xyz.drugalev.exception.UserAlreadyExistsException;
import xyz.drugalev.exception.UserDoesNotExistsException;

/**
 * Service for authentication and registration of users.
 */
public interface AuthService {
    /**
     * Login user and return JWT token.
     *
     * @param user user to login
     * @return JWT token
     * @throws UserDoesNotExistsException if user does not exist
     */
    JwtToken login(UserDto user) throws UserDoesNotExistsException;

    /**
     * Register user and return JWT token.
     *
     * @param user user to register
     * @return JWT token
     * @throws UserAlreadyExistsException if user already exists
     */
    JwtToken register(UserDto user) throws UserAlreadyExistsException;
}