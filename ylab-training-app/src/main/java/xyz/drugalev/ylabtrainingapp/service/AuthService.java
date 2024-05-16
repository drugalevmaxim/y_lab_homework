package xyz.drugalev.ylabtrainingapp.service;

import xyz.drugalev.ylabtrainingapp.dto.JwtToken;
import xyz.drugalev.ylabtrainingapp.dto.UserDto;
import xyz.drugalev.ylabtrainingapp.exception.UserAlreadyExistsException;
import xyz.drugalev.ylabtrainingapp.exception.UserDoesNotExistException;

/**
 * Authentication service.
 *
 * @author Drugalev Maxim
 */
public interface AuthService {
    /**
     * Login user.
     *
     * @param user user DTO
     * @return JWT token
     * @throws UserDoesNotExistException if user does not exist
     */
    JwtToken login(UserDto user) throws UserDoesNotExistException;

    /**
     * Register user.
     *
     * @param user user DTO
     * @return JWT token
     * @throws UserAlreadyExistsException if user already exists
     */
    JwtToken register(UserDto user) throws UserAlreadyExistsException;
}