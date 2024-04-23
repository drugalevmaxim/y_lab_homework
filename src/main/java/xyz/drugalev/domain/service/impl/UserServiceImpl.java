package xyz.drugalev.domain.service.impl;

import lombok.NonNull;
import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.exception.ValidationException;
import xyz.drugalev.domain.repository.UserRepository;
import xyz.drugalev.domain.service.UserService;
import xyz.drugalev.domain.validator.UserValidator;

import java.sql.SQLException;
import java.util.Optional;

/**
 * The User service implementation.
 */
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;

    /**
     * Instantiates a new User service.
     *
     * @param userRepository the user repository
     * @param userValidator  the user validator
     */
    public UserServiceImpl(@NonNull UserRepository userRepository, @NonNull UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    @Override
    public void save(@NonNull String username, @NonNull String password) throws SQLException, ValidationException {
        if (!userValidator.isValidName(username) || !userValidator.isValidPassword(password)) {
            throw new ValidationException("Invalid user data: " + userValidator.getDescription());
        }
        userRepository.save(username, password);
    }

    @Override
    public Optional<User> findUser(@NonNull String username) throws SQLException {
        return userRepository.find(username);
    }
}