package xyz.drugalev.repository;

import xyz.drugalev.entity.Privilege;
import xyz.drugalev.entity.Role;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

/**
 * Interface for working with the privilege table in the database.
 */
public interface PrivilegeRepository {
    /**
     * Finds a privilege by its name.
     *
     * @param name the name of the privilege to find
     * @return an optional containing the privilege, or an empty optional if the privilege does not exist
     * @throws SQLException if there is an error querying the database
     */
    Optional<Privilege> find(String name) throws SQLException;

    /**
     * Finds all privileges that are assigned to a given role.
     *
     * @param role the role to find privileges for
     * @return a set of all privileges that are assigned to the role
     * @throws SQLException if there is an error querying the database
     */
    Set<Privilege> findRolePrivileges(Role role) throws SQLException;
}