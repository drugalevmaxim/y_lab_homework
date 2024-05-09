package xyz.drugalev.repository;

import xyz.drugalev.entity.Training;
import xyz.drugalev.entity.TrainingType;
import xyz.drugalev.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface for working with the training table in the database.
 */
public interface TrainingRepository {
    /**
     * Finds all training records from the database.
     *
     * @return a list of all training records
     */
    List<Training> findAll();

    /**
     * Finds a training record by its ID.
     *
     * @param id the ID of the training record to find
     * @return an optional containing the training record, or an empty optional if the record does not exist
     */
    Optional<Training> find(long id);

    /**
     * Finds all training records for a given user.
     *
     * @param user the user to find training records for
     * @return a list of all training records for the given user
     */
    List<Training> findAllByUser(User user);

    /**
     * Finds all training records for a given user between two dates.
     *
     * @param user  the user to find training records for
     * @param start the start date of the range to search
     * @param end   the end date of the range to search
     * @return a list of all training records for the given user between the two dates
     */
    List<Training> findByUserBetweenDates(User user, LocalDate start, LocalDate end);

    /**
     * Finds a training record for a given user, training type, and date.
     *
     * @param user the user to find the training record for
     * @param type the type of training to find the record for
     * @param date the date of the training to find the record for
     * @return an optional containing the training record, or an empty optional if the record does not exist
     */
    Optional<Training> find(User user, TrainingType type, LocalDate date);

    /**
     * Saves a new training record to the database.
     *
     * @param training the training record to save
     * @return saved training
     */
    Training save(Training training);

    /**
     * Deletes a training record from the database.
     *
     * @param training the training record to delete
     */
    void delete(Training training);

    /**
     * Updates an existing training record in the database.
     *
     * @param training the training record to update
     * @return updated training
     */
    Training update(Training training);
}
