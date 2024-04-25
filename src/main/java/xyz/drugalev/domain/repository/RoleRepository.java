package xyz.drugalev.domain.repository;


import xyz.drugalev.domain.entity.Role;
import xyz.drugalev.domain.entity.User;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

/**
 * The interface Role repository.
 *
 * @author Drugalev Maxim
 */
public interface RoleRepository {
    /**
     * Find role by name.
     *
     * @param name the role name
     * @return role if found, empty otherwise, wrapped in an {@link Optional}.
     * @throws SQLException the sql exception
     */
    Optional<Role> find(String name) throws SQLException;

    /**
     * Find user roles set.
     *
     * @param userId the user id
     * @return the set of user roles
     * @throws SQLException the sql exception
     */
    Set<Role> findUserRoles(int userId) throws SQLException;

    /**
     * Add role to user
     *
     * @param user the user
     * @param role the role to add
     * @throws SQLException the sql exception
     */
    void add(User user, Role role) throws SQLException;
}
