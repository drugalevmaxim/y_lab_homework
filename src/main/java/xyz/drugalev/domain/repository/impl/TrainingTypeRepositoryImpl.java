package xyz.drugalev.domain.repository.impl;

import xyz.drugalev.config.JDBCConnectionProvider;
import xyz.drugalev.domain.entity.TrainingType;
import xyz.drugalev.domain.repository.TrainingTypeRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The Training type repository implementation.
 *
 * @author Drugalev Maxim
 */
public class TrainingTypeRepositoryImpl implements TrainingTypeRepository {
    @Override
    public Optional<TrainingType> find(int id) throws SQLException {
        try (Connection connection = JDBCConnectionProvider.getConnection()) {
            String findQuery = "SELECT id, name FROM ylab_trainings.training_types WHERE id = ?;";
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
        try (Connection connection = JDBCConnectionProvider.getConnection()) {
            String findQuery = "SELECT id, name FROM ylab_trainings.training_types WHERE name = ?;";
            PreparedStatement findStatement = connection.prepareStatement(findQuery);
            findStatement.setString(1, trainingTypeName);

            ResultSet rsFind = findStatement.executeQuery();

            if (rsFind.next()) {
                return Optional.of(new TrainingType(rsFind.getInt("id"), rsFind.getString("name")));
            }

            return Optional.empty();
        }
    }

    @Override
    public List<TrainingType> findAll() throws SQLException {
        try (Connection connection = JDBCConnectionProvider.getConnection()) {
            String findQuery = "SELECT id, name FROM ylab_trainings.training_types;";
            PreparedStatement findStatement = connection.prepareStatement(findQuery);

            ResultSet rsFind = findStatement.executeQuery();

            List<TrainingType> trainingTypes = new ArrayList<>();

            while (rsFind.next()) {
                trainingTypes.add(new TrainingType(rsFind.getInt("id"), rsFind.getString("name")));
            }

            return trainingTypes;
        }
    }

    @Override
    public void save(String trainingTypeName) throws SQLException {
        try (Connection connection = JDBCConnectionProvider.getConnection()) {
            String findQuery = "INSERT INTO ylab_trainings.training_types(name) VALUES (?);";
            PreparedStatement findStatement = connection.prepareStatement(findQuery);
            findStatement.setString(1, trainingTypeName);

            findStatement.execute();
        }
    }
}
