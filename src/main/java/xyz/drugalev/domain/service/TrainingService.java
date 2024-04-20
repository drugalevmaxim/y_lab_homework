package xyz.drugalev.domain.service;

import lombok.NonNull;
import xyz.drugalev.domain.entity.Training;
import xyz.drugalev.domain.entity.TrainingType;
import xyz.drugalev.domain.entity.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * An interface that defines training service.
 *
 * @author Drugalev Maxim
 */
public interface TrainingService {
    /**
     * Returns a list of all trainings.
     *
     * @return list of all trainings.
     */
    List<Training> findAll() throws SQLException;

    /**
     * Returns a list of all trainings user performed.
     *
     * @param user user which performed trainings.
     * @return list of all trainings user performed.
     */
    List<Training> findAllByUser(@NonNull User user) throws SQLException;

    /**
     * Search for training by given performer, Training type and date.
     *
     * @param user user which performed training.
     * @param date date when training was performed.
     * @param type type of training that was performed.
     * @return training if found, empty otherwise, wrapped in an {@link Optional}.
     */
    Optional<Training> find(@NonNull User user, @NonNull LocalDate date, @NonNull TrainingType type) throws SQLException;

    /**
     * Returns a list of all trainings user performed between given dates.
     *
     * @param user  user which performed trainings.
     * @param start min date of performed trainings.
     * @param end   max date of performed trainings.
     * @return list of all trainings user performed between given dates.
     */
    List<Training> findByUserBetweenDates(@NonNull User user, @NonNull LocalDate start, @NonNull LocalDate end) throws SQLException;

    /**
     * Returns training stats in Map with "Duration" and "Calories" keys
     *
     * @param user  user who performed training.
     * @param start starting date.
     * @param end   ending date.
     * @return Map with "Duration" and "Calories" keys.
     */
    Map<String, Integer> getTrainingsStats(@NonNull User user, @NonNull LocalDate start, @NonNull LocalDate end) throws SQLException;

    void save(@NonNull User performer, @NonNull LocalDate date, @NonNull TrainingType type, int duration, int burnedCalories) throws SQLException;

    /**
     * Updates given training.
     *
     * @param training training to update.
     * @param duration new duration.
     * @param burnedCalories new burned calories.
     */
    void update(@NonNull Training training, int duration, int burnedCalories) throws SQLException;

    /**
     * Deletes a given training.
     *
     * @param training training to delete.
     */
    void delete(@NonNull Training training) throws SQLException;

    /**
     * Adds additional data to training.
     *
     * @param training training to update.
     * @param name name of additional data.
     * @param value value  of additional data.
     */
    void addData(@NonNull Training training, @NonNull String name, int value) throws SQLException;
}
