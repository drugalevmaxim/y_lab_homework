package xyz.drugalev.usecase.user;

import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.exception.UserNotFoundException;
import xyz.drugalev.domain.service.UserService;

import java.util.Optional;

/**
 * Use case for user logging in
 */
public class LoginUserUseCase {
    private final UserService userService;

    /**
     * Default constructor.
     *
     * @param userService service that use case works with.
     */
    public LoginUserUseCase(UserService userService) {
        this.userService = userService;
    }

    public User login(String username, String password) throws UserNotFoundException {
        Optional<User> user = userService.findUser(username);

        if (user.isEmpty() || !user.get().getPassword().equals(password)) {
            throw new UserNotFoundException(username);
        }

        return user.get();
    }
}
