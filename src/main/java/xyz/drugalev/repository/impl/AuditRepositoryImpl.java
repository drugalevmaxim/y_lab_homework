package xyz.drugalev.repository.impl;

import lombok.RequiredArgsConstructor;
import xyz.drugalev.database.JDBCConnection;
import xyz.drugalev.entity.Audit;
import xyz.drugalev.entity.User;
import xyz.drugalev.repository.AuditRepository;
import xyz.drugalev.repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class AuditRepositoryImpl implements AuditRepository {
    private final UserRepository userRepository;

    @Override
    public void save(User user, String action) throws SQLException {
        String findQuery = "INSERT INTO ylab_trainings.audit (\"user\", \"action\") VALUES (?, ?);";
        try (Connection connection = JDBCConnection.getConnection()) {
            try (PreparedStatement findStatement = connection.prepareStatement(findQuery)) {
                findStatement.setLong(1, user.getId());
                findStatement.setString(2, action);
                findStatement.executeUpdate();
            }
        }
    }

    @Override
    public List<Audit> findAll() throws SQLException {
        String findQuery = "SELECT * FROM ylab_trainings.audit;";
        List<Audit> audits = new ArrayList<>();
        try (Connection connection = JDBCConnection.getConnection()) {
            try (PreparedStatement findStatement = connection.prepareStatement(findQuery)) {
                try ( ResultSet rsFind = findStatement.executeQuery()) {
                    while (rsFind.next()) {
                        audits.add(new Audit(
                                userRepository.find(rsFind.getInt("user")).orElse(new User(0, "unknown", "unknown")),
                                rsFind.getString("action"),
                                rsFind.getTimestamp("time").toLocalDateTime())
                        );
                    }
                    return audits;
                }
            }
        }
    }
}
