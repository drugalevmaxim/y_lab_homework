package xyz.drugalev.repository.impl;

import xyz.drugalev.database.JDBCConnection;
import xyz.drugalev.entity.Training;
import xyz.drugalev.entity.TrainingData;
import xyz.drugalev.repository.TrainingDataRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Training data repository implementation.
 */
public class TrainingDataRepositoryImpl implements TrainingDataRepository {

    @Override
    public void save(Training training, TrainingData trainingData) throws SQLException {
        String insertQuery = "INSERT INTO ylab_trainings.training_data(training, name, value) VALUES (?, ?, ?);";
        try (Connection connection = JDBCConnection.getConnection()) {

            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);

            insertStatement.setLong(1, training.getId());
            insertStatement.setString(2, trainingData.getName());
            insertStatement.setInt(3, trainingData.getValue());

            insertStatement.executeUpdate();
        }
    }

    @Override
    public List<TrainingData> find(Training training) throws SQLException {
        String findQuery = "SELECT * FROM ylab_trainings.training_data WHERE training = ?;";
        try (Connection connection = JDBCConnection.getConnection()) {
            try (PreparedStatement findStatement = connection.prepareStatement(findQuery)) {
                findStatement.setInt(1, training.getId());
                try (ResultSet rsFind = findStatement.executeQuery()) {
                    List<TrainingData> trainingsData = new ArrayList<>();

                    while (rsFind.next()) {
                        trainingsData.add(new TrainingData(rsFind.getString("name"), rsFind.getInt("value")));
                    }

                    return trainingsData;
                }
            }
        }
    }

    @Override
    public void update(Training training, TrainingData trainingData) throws SQLException {
        String insertQuery = "UPDATE ylab_trainings.training_data SET value = ? WHERE training = ? AND name = ?;";
        try (Connection connection = JDBCConnection.getConnection()) {
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);

            insertStatement.setInt(1, trainingData.getValue());
            insertStatement.setLong(2, training.getId());
            insertStatement.setString(3, trainingData.getName());

            insertStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Training training, TrainingData trainingData) throws SQLException  {
        String insertQuery = "DELETE FROM ylab_trainings.training_data WHERE training = ? AND name = ?;";
        try (Connection connection = JDBCConnection.getConnection()) {
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);

            insertStatement.setLong(1, training.getId());
            insertStatement.setString(2, trainingData.getName());

            insertStatement.executeUpdate();
        }
    }
}
