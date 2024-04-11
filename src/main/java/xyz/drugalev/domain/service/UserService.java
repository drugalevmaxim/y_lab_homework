package xyz.drugalev.domain.service;

import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.exception.UserAlreadyExistsException;
import xyz.drugalev.domain.exception.ValidationException;

import java.util.Optional;

/**
 * An interface that defines user service.
 *
 * @author Drugalev Maxim
 */
public interface UserService {
    /**
     * Saves given user.
     * Note: First added user will be admin.
     *
     * @param user user to save.
     * @return saved user.
     * @throws ValidationException if given invalid user data.
     * @throws UserAlreadyExistsException if user already exists.
     */
    User save(User user) throws ValidationException, UserAlreadyExistsException;

    /**
     * Finds a user by its username.
     *
     * @param username users name.
     * @return user if found, empty otherwise, wrapped in an {@link Optional}.
     */
    Optional<User> findUser(String username);
}