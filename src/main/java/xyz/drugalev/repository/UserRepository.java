package xyz.drugalev.repository;

import xyz.drugalev.entity.User;

import java.util.Optional;

/**
 * Interface for working with the user table in the database.
 */
public interface UserRepository {
    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user to find
     * @return an optional containing the user, or an empty optional if the user does not exist
     */
    Optional<User> findById(long id);

    /**
     * Saves a new user to the database.
     *
     * @param user the user to save
     * @return the saved user
     */
    User save(User user);

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user to find
     * @return an optional containing the user, or an empty optional if the user does not exist
     */
    Optional<User> findByName(String username);
}
