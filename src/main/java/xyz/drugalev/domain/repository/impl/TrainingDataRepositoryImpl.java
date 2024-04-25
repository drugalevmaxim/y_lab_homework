package xyz.drugalev.domain.repository.impl;

import xyz.drugalev.config.JDBCConnectionProvider;
import xyz.drugalev.domain.entity.Training;
import xyz.drugalev.domain.entity.TrainingData;
import xyz.drugalev.domain.repository.TrainingDataRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * The Training data repository implementation.
 *
 * @author Drugalev Maxim
 */
public class TrainingDataRepositoryImpl implements TrainingDataRepository {

    @Override
    public void save(Training training, String name, int value) throws SQLException {
        try (Connection connection = JDBCConnectionProvider.getConnection()) {
            String insertQuery = "INSERT INTO ylab_trainings.training_data(name, value) VALUES (?, ?);";

            PreparedStatement insertStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

            insertStatement.setString(1, name);
            insertStatement.setInt(2, value);

            insertStatement.executeUpdate();
            ResultSet rsId = insertStatement.getGeneratedKeys();

            if (rsId.next()) {
                String linkQuery = "INSERT INTO ylab_trainings.training_training_data(training_id, training_data_id) VALUES (?, ?);";
                PreparedStatement linkStatement = connection.prepareStatement(linkQuery);
                linkStatement.setInt(1, training.getId());
                linkStatement.setInt(2, rsId.getInt(1));

                linkStatement.execute();

                training.getTrainingData().clear();
                training.getTrainingData().addAll(find(training));
            }
        }
    }

    @Override
    public List<TrainingData> find(Training training) throws SQLException {
        try (Connection connection = JDBCConnectionProvider.getConnection()) {
            String findQuery = "SELECT * FROM ylab_trainings.training_data WHERE id IN (SELECT training_data_id FROM ylab_trainings.training_training_data WHERE training_id = ?);";
            PreparedStatement findStatement = connection.prepareStatement(findQuery);
            findStatement.setInt(1, training.getId());
            ResultSet rsFind = findStatement.executeQuery();

            List<TrainingData> trainingsData = new ArrayList<>();

            while (rsFind.next()) {
                trainingsData.add(new TrainingData(rsFind.getInt("id"), rsFind.getString("name"), rsFind.getInt("value")));
            }

            return trainingsData;
        }
    }
}
