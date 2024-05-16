package xyz.drugalev.service;

import xyz.drugalev.dto.TrainingDto;
import xyz.drugalev.entity.User;
import xyz.drugalev.exception.TrainingAlreadyExistsException;
import xyz.drugalev.exception.TrainingDoesNotExistsException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface for working with trainings.
 */
public interface TrainingService {
    /**
     * Finds all training records for the given user.
     *
     * @param user  the user which making request
     * @return a list of all training records
     */
    List<TrainingDto> findAll(User user) throws AccessDeniedException;

    /**
     * Finds a training record by its ID for the given user.
     *
     * @param user the user which making request
     * @param id   the ID of the training record to find
     * @return the training record with the given ID for the given user
     * @throws TrainingDoesNotExistsException if the training record with the given ID does not exist for the given user
     */
    TrainingDto find(User user, long id) throws TrainingDoesNotExistsException;

    /**
     * Finds all training records for the given user.
     *
     * @param user the user to find training records for
     * @return a list of all training records for the given user
     */
    List<TrainingDto> findAllByUser(User user);

    /**
     * Finds all training records for the given user between two dates.
     *
     * @param user  the user to find training records for
     * @param start the start date of the range to search
     * @param end   the end date of the range to search
     * @return a list of all training records for the given user between the two dates
     */
    List<TrainingDto> findByUserBetweenDates(User user, LocalDate start, LocalDate end);

    /**
     * Saves a new training record for the given user.
     *
     * @param user     the user to save the training record for
     * @param training the training record to save
     * @throws TrainingAlreadyExistsException if a training record with the same type and date already exists for the given user
     */
    TrainingDto save(User user, TrainingDto training) throws TrainingAlreadyExistsException;

    /**
     * Updates an existing training record for the given user.
     *
     * @param user     the user which making request
     * @param training the training record to update
     */
    TrainingDto update(User user, Long id, TrainingDto training) throws TrainingDoesNotExistsException;

    /**
     * Deletes a training record for the given user.
     *
     * @param user the user which making request
     * @param id   the id of training record to delete
     */
    void delete(User user, Long id) throws TrainingDoesNotExistsException;
}