package xyz.drugalev.repository;

import xyz.drugalev.entity.Training;
import xyz.drugalev.entity.TrainingType;
import xyz.drugalev.entity.User;

import java.sql.SQLException;
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
     * @throws SQLException if there is an error retrieving the records from the database
     */
    List<Training> findAll() throws SQLException;

    /**
     * Finds a training record by its ID.
     *
     * @param id the ID of the training record to find
     * @return an optional containing the training record, or an empty optional if the record does not exist
     * @throws SQLException if there is an error querying the database
     */
    Optional<Training> find(long id) throws SQLException;

    /**
     * Finds all training records for a given user.
     *
     * @param user the user to find training records for
     * @return a list of all training records for the given user
     * @throws SQLException if there is an error querying the database
     */
    List<Training> findAllByUser(User user) throws SQLException;

    /**
     * Finds all training records for a given user between two dates.
     *
     * @param user  the user to find training records for
     * @param start the start date of the range to search
     * @param end   the end date of the range to search
     * @return a list of all training records for the given user between the two dates
     * @throws SQLException if there is an error querying the database
     */
    List<Training> findByUserBetweenDates(User user, LocalDate start, LocalDate end) throws SQLException;

    /**
     * Finds a training record for a given user, training type, and date.
     *
     * @param user  the user to find the training record for
     * @param type  the type of training to find the record for
     * @param date  the date of the training to find the record for
     * @return an optional containing the training record, or an empty optional if the record does not exist
     * @throws SQLException if there is an error querying the database
     */
    Optional<Training> find(User user, TrainingType type, LocalDate date) throws SQLException;

    /**
     * Saves a new training record to the database.
     *
     * @param training the training record to save
     * @throws SQLException if there is an error saving the record to the database
     */
    void save(Training training) throws SQLException;

    /**
     * Deletes a training record from the database.
     *
     * @param training the training record to delete
     * @throws SQLException if there is an error deleting the record from the database
     */
    void delete(Training training) throws SQLException;

    /**
     * Updates an existing training record in the database.
     *
     * @param training the training record to update
     * @throws SQLException if there is an error updating the record in the database
     */
    void update(Training training) throws SQLException;
}
