package xyz.drugalev.repository.impl;

import lombok.RequiredArgsConstructor;
import xyz.drugalev.database.JDBCConnection;
import xyz.drugalev.entity.Privilege;
import xyz.drugalev.entity.Role;
import xyz.drugalev.entity.User;
import xyz.drugalev.repository.PrivilegeRepository;
import xyz.drugalev.repository.RoleRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * The Role repository  implementation.
 *
 */
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {
    private final PrivilegeRepository privilegeRepository;

    @Override
    public Optional<Role> find(String name) throws SQLException {
        try (Connection connection = JDBCConnection.getConnection()) {
            String findRoleQuery = "SELECT * FROM ylab_trainings.roles WHERE name = ?";
            PreparedStatement findRoleStatement = connection.prepareStatement(findRoleQuery);
            findRoleStatement.setString(1, name);
            ResultSet rsRole = findRoleStatement.executeQuery();

            Role role;
            if (rsRole.next()) {
                role = new Role(rsRole.getInt("id"), rsRole.getString("name"));

                for (Privilege rolePrivilege : privilegeRepository.findRolePrivileges(role)) {
                    role.getPrivileges().add(rolePrivilege);
                }

                return Optional.of(role);
            }
            return Optional.empty();
        }
    }

    @Override
    public Set<Role> findUserRoles(long userId) throws SQLException {
        try (Connection connection = JDBCConnection.getConnection()) {
            String userRolesQuery = "SELECT * FROM ylab_trainings.roles WHERE id IN (SELECT role_id FROM ylab_trainings.user_roles WHERE user_id = ?);";
            PreparedStatement userRolesStatement = connection.prepareStatement(userRolesQuery);
            userRolesStatement.setLong(1, userId);
            ResultSet rsRoles = userRolesStatement.executeQuery();

            Set<Role> userRoles = new HashSet<>();

            while (rsRoles.next()) {
                Role role = new Role(rsRoles.getInt("id"), rsRoles.getString("name"));

                for (Privilege rolePrivilege : privilegeRepository.findRolePrivileges(role)) {
                    role.getPrivileges().add(rolePrivilege);
                }

                userRoles.add(role);
            }

            return userRoles;
        }
    }

    @Override
    public void add(User user, Role role) throws SQLException {
        try (Connection connection = JDBCConnection.getConnection()) {
            String addRoleQuery = "INSERT INTO ylab_trainings.user_roles(user_id, role_id) VALUES (?, ?);";
            PreparedStatement addRoleStatement = connection.prepareStatement(addRoleQuery);
            addRoleStatement.setLong(2, role.getId());
            addRoleStatement.setLong(1, user.getId());
            addRoleStatement.execute();
            user.getRoles().add(role);
        }
    }
}
