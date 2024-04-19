package xyz.drugalev.domain.service;

import lombok.NonNull;
import xyz.drugalev.domain.entity.Training;
import xyz.drugalev.domain.entity.TrainingType;
import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.exception.IllegalDatePeriodException;
import xyz.drugalev.domain.exception.TrainingAlreadyExistsException;

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
    List<Training> findAll();

    /**
     * Returns a list of all trainings user performed.
     *
     * @param user user which performed trainings.
     * @return list of all trainings user performed.
     */
    List<Training> findAllByUser(@NonNull User user);

    /**
     * Search for training by given performer, Training type and date.
     *
     * @param user user which performed training.
     * @param type type of training that was performed.
     * @param date date when training was performed.
     * @return training if found, empty otherwise, wrapped in an {@link Optional}.
     */
    Optional<Training> find(@NonNull User user, TrainingType type, LocalDate date);

    /**
     * Returns a list of all trainings user performed between given dates.
     *
     * @param user  user which performed trainings.
     * @param start min date of performed trainings.
     * @param end   max date of performed trainings.
     * @return list of all trainings user performed between given dates.
     * @throws IllegalDatePeriodException if start > end.
     */
    List<Training> findByUserBetweenDates(User user, LocalDate start, LocalDate end) throws IllegalDatePeriodException;

    /**
     * Returns training stats in Map with "Duration" and "Calories" keys
     *
     * @param user  user who performed training.
     * @param start starting date.
     * @param end   ending date.
     * @return Map with "Duration" and "Calories" keys.
     * @throws IllegalDatePeriodException if start > end.
     */
    Map<String, Integer> getTrainingsStats(User user, LocalDate start, LocalDate end) throws IllegalDatePeriodException;

    /**
     * Saves a given training to repository.
     *
     * @param training training to save.
     * @return saved Training.
     * @throws TrainingAlreadyExistsException if training already exists.
     */
    Training save(Training training) throws TrainingAlreadyExistsException;

    /**
     * Deletes a given training.
     *
     * @param training training to delete.
     */
    void delete(Training training);
}
