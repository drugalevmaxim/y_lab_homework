package xyz.drugalev.repository.impl;

import xyz.drugalev.database.JDBCConnection;
import xyz.drugalev.entity.Privilege;
import xyz.drugalev.entity.Role;
import xyz.drugalev.repository.PrivilegeRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * The Privilege repository  implementation.
 *
 * @author Drugalev Maxim
 */
public class PrivilegeRepositoryImpl implements PrivilegeRepository {
    @Override
    public Optional<Privilege> find(String name) throws SQLException {
        try (Connection connection = JDBCConnection.getConnection()) {
            String findPrivilegeQuery = "SELECT * FROM ylab_trainings.privileges WHERE name = ?";
            PreparedStatement findPrivilegeStatement = connection.prepareStatement(findPrivilegeQuery);
            findPrivilegeStatement.setString(1, name);

            ResultSet rsPrivilege = findPrivilegeStatement.executeQuery();

            if (rsPrivilege.next()) {
                return Optional.of(new Privilege(rsPrivilege.getInt("id"), rsPrivilege.getString("name")));
            }
            return Optional.empty();
        }
    }

    @Override
    public Set<Privilege> findRolePrivileges(Role role) throws SQLException {
        Set<Privilege> privilegeSet =  new HashSet<>();
        try (Connection connection = JDBCConnection.getConnection()) {
            String findPrivilegeQuery = "SELECT * FROM ylab_trainings.privileges WHERE id IN (SELECT id FROM ylab_trainings.role_privileges WHERE role_id = ?)";
            PreparedStatement findPrivilegeStatement = connection.prepareStatement(findPrivilegeQuery);
            findPrivilegeStatement.setInt(1, role.getId());

            ResultSet rsPrivilege = findPrivilegeStatement.executeQuery();

            while (rsPrivilege.next()) {
                privilegeSet.add(new Privilege(rsPrivilege.getInt("id"),rsPrivilege.getString("name")));
            }

            return privilegeSet;
        }
    }
}
