package xyz.drugalev.ylabtrainingapp.repository;

import xyz.drugalev.entity.User;

import java.util.Optional;

/**
 * User repository.
 *
 * @author Drugalev Maxim
 */
public interface UserRepository {
    /**
     * Save user.
     *
     * @param user user
     * @return saved user
     */
    User save(User user);

    /**
     * Find user by id.
     *
     * @param id user id
     * @return optional of user
     */
    Optional<User> findById(Long id);

    /**
     * Find user by username.
     *
     * @param username username
     * @return optional of user
     */
    Optional<User> findByUsername(String username);
}