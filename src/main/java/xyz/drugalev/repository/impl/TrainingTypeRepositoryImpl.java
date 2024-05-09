package xyz.drugalev.repository.impl;

import xyz.drugalev.database.JDBCConnection;
import xyz.drugalev.entity.TrainingType;
import xyz.drugalev.repository.TrainingTypeRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The Training type repository implementation.
 */
public class TrainingTypeRepositoryImpl implements TrainingTypeRepository {
    @Override
    public Optional<TrainingType> find(int id) throws SQLException {
        try (Connection connection = JDBCConnection.getConnection()) {
            String findQuery = "SELECT * FROM ylab_trainings.training_types WHERE id = ?;";
            PreparedStatement findStatement = connection.prepareStatement(findQuery);
            findStatement.setInt(1, id);

            ResultSet rsFind = findStatement.executeQuery();

            if (rsFind.next()) {
                return Optional.of(new TrainingType(rsFind.getInt("id"), rsFind.getString("name")));
            }

            return Optional.empty();
        }
    }

    @Override
    public Optional<TrainingType> find(String trainingTypeName) throws SQLException {
        String findQuery = "SELECT * FROM ylab_trainings.training_types WHERE name = ?;";
        try (Connection connection = JDBCConnection.getConnection()) {
            try (PreparedStatement findStatement = connection.prepareStatement(findQuery)) {
                findStatement.setString(1, trainingTypeName);
                try(ResultSet rsFind = findStatement.executeQuery()) {
                    if (rsFind.next()) {
                        return Optional.of(new TrainingType(rsFind.getInt("id"), rsFind.getString("name")));
                    }
                    return Optional.empty();
                }
            }
        }
    }

    @Override
    public List<TrainingType> findAll() throws SQLException {
        String findQuery = "SELECT * FROM ylab_trainings.training_types;";
        List<TrainingType> trainingTypes = new ArrayList<>();
        try (Connection connection = JDBCConnection.getConnection()) {
            try (PreparedStatement findStatement = connection.prepareStatement(findQuery)) {
               try ( ResultSet rsFind = findStatement.executeQuery()) {
                   while (rsFind.next()) {
                       trainingTypes.add(new TrainingType(rsFind.getInt("id"), rsFind.getString("name")));
                   }
                   return trainingTypes;
               }
            }
        }
    }

    @Override
    public void save(String trainingTypeName) throws SQLException {
        try (Connection connection = JDBCConnection.getConnection()) {
            String findQuery = "INSERT INTO ylab_trainings.training_types(name) VALUES (?);";
            PreparedStatement findStatement = connection.prepareStatement(findQuery);
            findStatement.setString(1, trainingTypeName);

            findStatement.execute();
        }
    }
}
