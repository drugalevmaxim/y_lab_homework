package xyz.drugalev.domain.repository;


import xyz.drugalev.domain.entity.Role;
import xyz.drugalev.domain.entity.User;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository {
    Optional<Role> find(String name) throws SQLException;

    Set<Role> findUserRoles(int userId) throws SQLException;

    void add(User user, Role role) throws SQLException;
}
