package xyz.drugalev.repository.impl;

import lombok.RequiredArgsConstructor;
import xyz.drugalev.database.JDBCConnection;
import xyz.drugalev.entity.Role;
import xyz.drugalev.entity.User;
import xyz.drugalev.repository.RoleRepository;
import xyz.drugalev.repository.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The User repository implementation.
 *
 * @author Drugalev Maxim
 */
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    RoleRepository roleRepository = new RoleRepositoryImpl(new PrivilegeRepositoryImpl());

    @Override
    public User save(User user) throws SQLException {
        String query = "INSERT INTO ylab_trainings.users (username, password) VALUES (?, ?) RETURNING id;";
        User savedUser;
        try (Connection connection = JDBCConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, user.getUsername());
                statement.setString(2, user.getPassword());

                statement.executeUpdate();

                try (ResultSet rs = statement.getGeneratedKeys()) {
                    rs.next();

                    savedUser = new User(rs.getLong(1), user.getUsername(), user.getPassword());
                    savedUser.setId(rs.getLong(1));
                    savedUser.setUsername(user.getUsername());
                    savedUser.setPassword(user.getPassword());

                    return savedUser;
                }
            }
        }
    }

    @Override
    public Optional<User> find(String username) throws SQLException {
        String query = "SELECT * FROM ylab_trainings.users WHERE username = ?;";
        try (Connection connection = JDBCConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                try (ResultSet rs = statement.executeQuery()) {
                    return mapUser(rs);
                }
            }
        }
    }

    @Override
    public Optional<User> find(int id) throws SQLException {
        String query = "SELECT * FROM ylab_trainings.users WHERE id = ?;";
        try (Connection connection = JDBCConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setLong(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    return mapUser(rs);
                }
            }
        }
    }

    @Override
    public List<User> findAll() throws SQLException {
        String query = "SELECT * FROM ylab_trainings.users;";
        List<User> users = new ArrayList<>();
        try (Connection connection = JDBCConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet rs = statement.executeQuery()) {
                    for (Optional<User> user = mapUser(rs); user.isPresent(); user = mapUser(rs)) {
                        users.add(user.get());
                    }
                    return users;
                }
            }
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
