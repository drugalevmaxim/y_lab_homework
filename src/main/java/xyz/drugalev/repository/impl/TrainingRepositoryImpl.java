package xyz.drugalev.repository.impl;

import lombok.RequiredArgsConstructor;
import xyz.drugalev.database.JDBCConnection;
import xyz.drugalev.entity.Training;
import xyz.drugalev.entity.TrainingData;
import xyz.drugalev.entity.TrainingType;
import xyz.drugalev.entity.User;
import xyz.drugalev.repository.TrainingDataRepository;
import xyz.drugalev.repository.TrainingRepository;
import xyz.drugalev.repository.TrainingTypeRepository;
import xyz.drugalev.repository.UserRepository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The Training repository implementation.
 *
 * @author Drugalev Maxim
 */
@RequiredArgsConstructor
public class TrainingRepositoryImpl implements TrainingRepository {

    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainingDataRepository trainingDataRepository;
    private final UserRepository userRepository;

    @Override
    public List<Training> findAll() throws SQLException {
        String query = "SELECT * FROM ylab_trainings.trainings ORDER BY date DESC;";
        List<Training> trainings = new ArrayList<>();
        try (Connection connection = JDBCConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet rs = statement.executeQuery()) {
                    for (Optional<Training> training = mapTraining(rs); training.isPresent(); training = mapTraining(rs)) {
                        trainings.add(training.get());
                    }
                    return trainings;
                }
            }
        }
    }

    @Override
    public Optional<Training> find(long id) throws SQLException {
        String query = "SELECT * FROM ylab_trainings.trainings WHERE id = ? ORDER BY date DESC;";
        try (Connection connection = JDBCConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setLong(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    return mapTraining(rs);
                }
            }
        }
    }

    @Override
    public List<Training> findAllByUser(User user) throws SQLException {
        String query = "SELECT * FROM ylab_trainings.trainings WHERE performer = ? ORDER BY date DESC;";
        List<Training> trainings = new ArrayList<>();
        try (Connection connection = JDBCConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setLong(1, user.getId());
                try (ResultSet rs = statement.executeQuery()) {
                    for (Optional<Training> training = mapTraining(rs); training.isPresent(); training = mapTraining(rs)) {
                        trainings.add(training.get());
                    }
                    return trainings;
                }
            }
        }
    }

    @Override
    public List<Training> findByUserBetweenDates(User user, LocalDate start, LocalDate end) throws SQLException {
        String query = "SELECT * FROM ylab_trainings.trainings WHERE performer = ? and date between ? and ? ORDER BY date DESC;";
        List<Training> trainings = new ArrayList<>();
        try (Connection connection = JDBCConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setLong(1, user.getId());
                statement.setDate(2, Date.valueOf(start));
                statement.setDate(3, Date.valueOf(end));
                try (ResultSet rs = statement.executeQuery()) {
                    for (Optional<Training> training = mapTraining(rs); training.isPresent(); training = mapTraining(rs)) {
                        trainings.add(training.get());
                    }
                    return trainings;
                }
            }
        }
    }

    @Override
    public Optional<Training> find(User user, TrainingType type, LocalDate date) throws SQLException {
        String query = "SELECT * FROM ylab_trainings.trainings WHERE performer = ? and date = ? and training_type = ?;";
        try (Connection connection = JDBCConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setLong(1, user.getId());
                statement.setDate(2, Date.valueOf(date));
                statement.setLong(3, type.getId());

                try (ResultSet rs = statement.executeQuery()) {
                    return mapTraining(rs);
                }
            }
        }
    }

    @Override
    public void save(Training training) throws SQLException {
        String query = "INSERT INTO ylab_trainings.trainings (performer, date, training_type, duration, burned_calories) VALUES (?, ?, ?, ?, ?);";
        try (Connection connection = JDBCConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, training.getPerformer().getId());
                statement.setDate(2, Date.valueOf(training.getDate()));
                statement.setLong(3, training.getTrainingType().getId());
                statement.setLong(4, training.getDuration());
                statement.setLong(5, training.getBurnedCalories());

                statement.executeUpdate();

                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        training.setId(resultSet.getInt(1));
                    }

                    for (TrainingData trainingData : training.getTrainingData()) {
                        trainingDataRepository.save(training, trainingData);
                    }
                }
            }
        }
    }

    @Override
    public void delete(Training training) throws SQLException {
        String query = "DELETE FROM ylab_trainings.trainings WHERE id = ?;";
        try (Connection connection = JDBCConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setLong(1, training.getId());
                statement.executeUpdate();
            }
        }
    }

    @Override
    public void update(Training training) throws SQLException {
        String query = "UPDATE ylab_trainings.trainings SET date = ?, training_type = ?, duration = ?, burned_calories = ? WHERE id = ?;";
        try (Connection connection = JDBCConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setDate(1, Date.valueOf(training.getDate()));
                statement.setLong(2, training.getTrainingType().getId());
                statement.setLong(3, training.getDuration());
                statement.setLong(4, training.getBurnedCalories());
                statement.setLong(5, training.getId());

                statement.executeUpdate();

                List<TrainingData> currentTrainingData = trainingDataRepository.find(training);
                List<String> currentTrainingDataNames =  currentTrainingData.stream().map(TrainingData::getName).toList();

                for (TrainingData trainingData : training.getTrainingData()) {
                    if (currentTrainingDataNames.contains(trainingData.getName())) {
                        trainingDataRepository.update(training, trainingData);
                    } else {
                        trainingDataRepository.save(training, trainingData);
                    }
                }

                List<String> trainingDataNames =  training.getTrainingData().stream().map(TrainingData::getName).toList();

                for (TrainingData trainingData : currentTrainingData) {
                    if (!trainingDataNames.contains(trainingData.getName())) {
                        trainingDataRepository.delete(training, trainingData);
                    }
                }
            }
        }
    }

    private Optional<Training> mapTraining(ResultSet rsTraining) throws SQLException {
        if (rsTraining.next()) {
            Optional<TrainingType> trainingType = trainingTypeRepository.find(rsTraining.getInt("training_type"));
            Optional<User> user = userRepository.find(rsTraining.getInt("performer"));
            Training training = new Training();

            training.setId(rsTraining.getInt("id"));
            training.setDate(rsTraining.getDate("date").toLocalDate());
            training.setDuration(rsTraining.getInt("duration"));
            training.setBurnedCalories(rsTraining.getInt("burned_calories"));

            if( trainingType.isPresent() && user.isPresent()) {
                training.setTrainingType(trainingType.get());
                training.setPerformer(user.get());
            }

            trainingDataRepository.find(training).forEach(trainingData -> training.getTrainingData().add(trainingData));

            return Optional.of(training);
        }
        return Optional.empty();
    }
}
