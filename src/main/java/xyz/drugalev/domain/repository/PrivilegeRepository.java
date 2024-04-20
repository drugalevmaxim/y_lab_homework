package xyz.drugalev.domain.repository;

import xyz.drugalev.domain.entity.Privilege;
import xyz.drugalev.domain.entity.Role;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

public interface PrivilegeRepository {
    Optional<Privilege> find(String name) throws SQLException;
    Set<Privilege> findRolePrivileges(Role role) throws SQLException;

}
