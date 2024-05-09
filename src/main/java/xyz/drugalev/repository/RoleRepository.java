package xyz.drugalev.repository;


import xyz.drugalev.entity.Role;
import xyz.drugalev.entity.User;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

/**
 * Interface for working with the role table in the database.
 */
public interface RoleRepository {
    /**
     * Finds a role by its name.
     *
     * @param name the name of the role to find
     * @return an optional containing the role, or an empty optional if the role does not exist
     * @throws SQLException if there is an error querying the database
     */
    Optional<Role> find(String name) throws SQLException;

    /**
     * Finds all roles that are assigned to a given user.
     *
     * @param userId the ID of the user to find roles for
     * @return a set of all roles that are assigned to the user
     * @throws SQLException if there is an error querying the database
     */
    Set<Role> findUserRoles(long userId) throws SQLException;

    /**
     * Adds a role to a user.
     *
     * @param user the user to add the role to
     * @param role the role to add to the user
     * @throws SQLException if there is an error updating the database
     */
    void add(User user, Role role) throws SQLException;
}