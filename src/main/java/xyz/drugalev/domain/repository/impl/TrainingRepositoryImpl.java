package xyz.drugalev.domain.repository.impl;

import lombok.NonNull;
import xyz.drugalev.config.JDBCConnectionProvider;
import xyz.drugalev.domain.entity.Training;
import xyz.drugalev.domain.entity.TrainingData;
import xyz.drugalev.domain.entity.TrainingType;
import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.repository.TrainingDataRepository;
import xyz.drugalev.domain.repository.TrainingRepository;
import xyz.drugalev.domain.repository.TrainingTypeRepository;
import xyz.drugalev.domain.repository.UserRepository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The Training repository implementation.
 *
 * @author Drugalev Maxim
 */
public class TrainingRepositoryImpl implements TrainingRepository {

    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainingDataRepository trainingDataRepository;
    private final UserRepository userRepository;

    /**
     * Instantiates a new Training repository.
     *
     * @param trainingTypeRepository the training type repository
     * @param trainingDataRepository the training data repository
     * @param userRepository         the user repository
     */
    public TrainingRepositoryImpl(TrainingTypeRepository trainingTypeRepository, TrainingDataRepository trainingDataRepository, UserRepository userRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
        this.trainingDataRepository = trainingDataRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Training> findAll() throws SQLException {
        try (Connection connection = JDBCConnectionProvider.getConnection()) {
            String findAllQuery = "SELECT id, performer, date, training_type, duration, burned_calories FROM ylab_trainings.trainings ORDER BY date DESC;";
            PreparedStatement findAllStatement = connection.prepareStatement(findAllQuery);

            ResultSet rsAll = findAllStatement.executeQuery();

            return mapTrainingList(rsAll);
        }
    }

    @Override
    public List<Training> findAllByUser(@NonNull User user) throws SQLException {
        try (Connection connection = JDBCConnectionProvider.getConnection()) {
            String findAllQuery = "SELECT id, performer, date, training_type, duration, burned_calories FROM ylab_trainings.trainings WHERE performer = ? ORDER BY date DESC;";
            PreparedStatement findAllStatement = connection.prepareStatement(findAllQuery);
            findAllStatement.setInt(1, user.getId());

            ResultSet rsAll = findAllStatement.executeQuery();

            return mapTrainingList(rsAll);
        }
    }

    @Override
    public List<Training> findByUserBetweenDates(@NonNull User user, @NonNull LocalDate start, @NonNull LocalDate end) throws SQLException {
        try (Connection connection = JDBCConnectionProvider.getConnection()) {
            String findAllQuery = "SELECT id, performer, date, training_type, duration, burned_calories FROM ylab_trainings.trainings WHERE performer = ? AND date BETWEEN ? and ? ORDER BY date DESC;";
            PreparedStatement findAllStatement = connection.prepareStatement(findAllQuery);
            findAllStatement.setInt(1, user.getId());
            findAllStatement.setDate(2, Date.valueOf(start));
            findAllStatement.setDate(3, Date.valueOf(end));

            ResultSet rsAll = findAllStatement.executeQuery();

            return mapTrainingList(rsAll);
        }
    }

    @Override
    public Optional<Training> find(@NonNull User user, @NonNull TrainingType type, @NonNull LocalDate date) throws SQLException {
        try (Connection connection = JDBCConnectionProvider.getConnection()) {
            String findQuery = ("SELECT id, performer, date, training_type, duration, burned_calories FROM ylab_trainings.trainings WHERE performer = ? AND date = ? AND training_type = ?");
            PreparedStatement findStatement = connection.prepareStatement(findQuery);
            findStatement.setInt(1, user.getId());
            findStatement.setDate(2, Date.valueOf(date));
            findStatement.setInt(3, type.getId());

            ResultSet rsFind = findStatement.executeQuery();

            if (rsFind.next()) {
                Optional<User> performer = userRepository.find(rsFind.getInt("performer"));
                Optional<TrainingType> trainingType = trainingTypeRepository.find(rsFind.getInt("training_type"));

                if (performer.isPresent() && trainingType.isPresent()) {
                    Training training = new Training(rsFind.getInt("id"), performer.get(),
                            rsFind.getDate("date").toLocalDate(), trainingType.get(),
                            rsFind.getInt("duration"), rsFind.getInt("burned_calories"));

                    for (TrainingData trainingData : trainingDataRepository.find(training)) {
                        training.getTrainingData().add(trainingData);
                    }
                    return Optional.of(training);
                }
            }

            return Optional.empty();
        }
    }

    @Override
    public void save(User performer, LocalDate date, TrainingType type, int duration, int burnedCalories) throws SQLException {
        try (Connection connection = JDBCConnectionProvider.getConnection()) {
            String saveQuery = "INSERT INTO ylab_trainings.trainings(performer, date, training_type, duration, burned_calories) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement saveStatement = connection.prepareStatement(saveQuery);

            saveStatement.setInt(1, performer.getId());
            saveStatement.setDate(2, Date.valueOf(date));
            saveStatement.setInt(3, type.getId());
            saveStatement.setInt(4, duration);
            saveStatement.setInt(5, burnedCalories);

            saveStatement.execute();
        }
    }

    @Override
    public void delete(@NonNull Training training) throws SQLException {
        try (Connection connection = JDBCConnectionProvider.getConnection()) {
            String deleteQuery = "DELETE FROM ylab_trainings.trainings WHERE id = ?;";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);

            deleteStatement.setInt(1, training.getId());

            deleteStatement.execute();
        }
    }

    @Override
    public void update(@NonNull Training training, int duration, int burnedCalories) throws SQLException {
        try (Connection connection = JDBCConnectionProvider.getConnection()) {
            String updateQuery = "UPDATE ylab_trainings.trainings SET duration=?, burned_calories=? WHERE id = ?;";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

            updateStatement.setInt(1, duration);
            updateStatement.setInt(2, burnedCalories);
            updateStatement.setInt(3, training.getId());

            updateStatement.execute();

            training.setDuration(duration);
            training.setBurnedCalories(burnedCalories);

        }
    }

    @Override
    public void addData(@NonNull Training training, @NonNull String name, int value) throws SQLException {
        trainingDataRepository.save(training, name, value);
    }

    private List<Training> mapTrainingList(ResultSet rsAll) throws SQLException {
        List<Training> trainings = new ArrayList<>();

        while (rsAll.next()) {

            Optional<User> performer = userRepository.find(rsAll.getInt("performer"));
            Optional<TrainingType> trainingType = trainingTypeRepository.find(rsAll.getInt("training_type"));

            if (performer.isPresent() && trainingType.isPresent()) {
                Training training = new Training(rsAll.getInt("id"), performer.get(),
                        rsAll.getDate("date").toLocalDate(), trainingType.get(),
                        rsAll.getInt("duration"), rsAll.getInt("burned_calories"));

                for (TrainingData trainingData : trainingDataRepository.find(training)) {
                    training.getTrainingData().add(trainingData);
                }

                trainings.add(training);
            }
        }
        return trainings;
    }
}
