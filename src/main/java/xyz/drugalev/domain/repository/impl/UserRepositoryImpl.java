package xyz.drugalev.domain.repository.impl;

import lombok.NonNull;
import xyz.drugalev.config.JDBCConnectionProvider;
import xyz.drugalev.domain.entity.Role;
import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.repository.RoleRepository;
import xyz.drugalev.domain.repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    private final RoleRepository roleRepository;

    public UserRepositoryImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void save(@NonNull String username, @NonNull String password) throws SQLException {
        try (Connection connection = JDBCConnectionProvider.getConnection()) {
            String insertUserQuery = "INSERT INTO ylab_trainings.users(username, password) VALUES (?, ?);";

            PreparedStatement insertUserStatement = connection.prepareStatement(insertUserQuery);

            insertUserStatement.setString(1, username);
            insertUserStatement.setString(2, password);

            insertUserStatement.execute();

            Optional<User> user = find(username);
            Optional<Role> role =  roleRepository.find("user");

            if (role.isPresent() && user.isPresent()) {
                roleRepository.add(user.get(), role.get());
            }
        }
    }

    @Override
    public Optional<User> find(@NonNull String username) throws SQLException {
        try (Connection connection = JDBCConnectionProvider.getConnection()) {
            String findUserQuery = "SELECT id, username, password FROM ylab_trainings.users WHERE username = ?;";

            PreparedStatement findUserStatement = connection.prepareStatement(findUserQuery);
            findUserStatement.setString(1, username);
            ResultSet rsUser = findUserStatement.executeQuery();

            return mapUser(rsUser);
        }
    }

    @Override
    public Optional<User> find(int id) throws SQLException {
        try (Connection connection = JDBCConnectionProvider.getConnection()) {
            String findUserQuery = "SELECT id, username, password FROM ylab_trainings.users WHERE id = ?;";

            PreparedStatement findUserStatement = connection.prepareStatement(findUserQuery);
            findUserStatement.setInt(1, id);
            ResultSet rsUser = findUserStatement.executeQuery();
            return mapUser(rsUser);
        }
    }

    private Optional<User> mapUser(ResultSet rsUser) throws SQLException {
        if (rsUser.next()) {
            User user = new User(rsUser.getInt("id"), rsUser.getString("username"), rsUser.getString("password"));
            for (Role userRole : roleRepository.findUserRoles(user.getId())) {
                user.getRoles().add(userRole);
            }
            return Optional.of(user);
        }
        return Optional.empty();
    }
}
