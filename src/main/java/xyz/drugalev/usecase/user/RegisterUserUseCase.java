package xyz.drugalev.usecase.user;

import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.exception.UserAlreadyExistsException;
import xyz.drugalev.domain.exception.ValidationException;
import xyz.drugalev.domain.service.UserService;

/**
 * Use case for user registration
 *
 * @author Drugalev Maxim
 */
public class RegisterUserUseCase {
    private final UserService userService;

    /**
     * Default constructor.
     *
     * @param userService service that use case works with.
     */
    public RegisterUserUseCase(UserService userService) {
        this.userService = userService;
    }

    /**
     * Register user.
     *
     * @param username username of user to register.
     * @param password password of user to register.
     * @return registered user.
     * @throws UserAlreadyExistsException if user with given username already exists.
     * @throws ValidationException        if passed username/password is invalid.
     */
    public User register(String username, String password) throws UserAlreadyExistsException, ValidationException {
        return userService.save(new User(username, password));
    }
}
