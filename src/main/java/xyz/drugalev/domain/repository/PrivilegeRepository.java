package xyz.drugalev.domain.repository;

import xyz.drugalev.domain.entity.Privilege;
import xyz.drugalev.domain.entity.Role;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

/**
 * The interface for Privilege repository.
 *
 * @author Drugalev Maxim
 */
public interface PrivilegeRepository {

    /**
     * Find privilege by name.
     *
     * @param name the name of privilege
     * @return privilege if found, empty otherwise, wrapped in an {@link Optional}
     * @throws SQLException the sql exception
     */
    Optional<Privilege> find(String name) throws SQLException;

    /**
     * Find role privileges set.
     *
     * @param role the role
     * @return the set of role privileges
     * @throws SQLException the sql exception
     */
    Set<Privilege> findRolePrivileges(Role role) throws SQLException;

}
