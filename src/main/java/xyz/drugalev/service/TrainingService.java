package xyz.drugalev.service;

import xyz.drugalev.dto.TrainingDto;
import xyz.drugalev.entity.User;
import xyz.drugalev.exception.TrainingAlreadyExistsException;
import xyz.drugalev.exception.TrainingNotFoundException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface for working with training data.
 */
public interface TrainingService {

    /**
     * Finds all training records for the given user.
     *
     * @param user  the user which making request
     * @return a list of all training records
     * @throws SQLException if there is an error querying the database
     */
    List<TrainingDto> findAll(User user) throws SQLException;

    /**
     * Finds a training record by its ID for the given user.
     *
     * @param user the user which making request
     * @param id   the ID of the training record to find
     * @return the training record with the given ID for the given user
     * @throws SQLException           if there is an error querying the database
     * @throws TrainingNotFoundException if the training record with the given ID does not exist for the given user
     */
    TrainingDto find(User user, Long id) throws SQLException, TrainingNotFoundException;

    /**
     * Finds all training records for the given user.
     *
     * @param user the user to find training records for
     * @return a list of all training records for the given user
     * @throws SQLException if there is an error querying the database
     */
    List<TrainingDto> findAllByUser(User user) throws SQLException;

    /**
     * Finds all training records for the given user between two dates.
     *
     * @param user  the user to find training records for
     * @param start the start date of the range to search
     * @param end   the end date of the range to search
     * @return a list of all training records for the given user between the two dates
     * @throws SQLException if there is an error querying the database
     */
    List<TrainingDto> findByUserBetweenDates(User user, LocalDate start, LocalDate end) throws SQLException;

    /**
     * Saves a new training record for the given user.
     *
     * @param user     the user to save the training record for
     * @param training the training record to save
     * @throws SQLException                if there is an error saving the training record to the database
     * @throws TrainingAlreadyExistsException if a training record with the same type and date already exists for the given user
     */
    void save(User user, TrainingDto training) throws SQLException, TrainingAlreadyExistsException;

    /**
     * Updates an existing training record for the given user.
     *
     * @param user     the user which making request
     * @param training the training record to update
     * @throws SQLException if there is an error updating the training record in the database
     */
    void update(User user, TrainingDto training) throws SQLException;

    /**
     * Deletes a training record for the given user.
     *
     * @param user     the user which making request
     * @param training the training record to delete
     * @throws SQLException if there is an error deleting the training record from the database
     */
    void delete(User user, TrainingDto training) throws SQLException;
}