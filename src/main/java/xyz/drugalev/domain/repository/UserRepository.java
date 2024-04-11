package xyz.drugalev.domain.repository;

import lombok.NonNull;
import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.exception.UserAlreadyExistsException;

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
     * @param user user to save.
     * @return saved user.
     */
    User save(@NonNull User user) throws UserAlreadyExistsException;

    /**
     * Finds a user by its username.
     *
     * @param username users name.
     * @return user if found, empty otherwise, wrapped in an {@link Optional}.
     */
    Optional<User> findByUsername(@NonNull String username);
}