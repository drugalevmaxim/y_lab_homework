package xyz.drugalev.ylabtrainingapp.service;

import xyz.drugalev.entity.User;
import xyz.drugalev.ylabtrainingapp.dto.TrainingDto;
import xyz.drugalev.ylabtrainingapp.exception.TrainingAlreadyExistsException;
import xyz.drugalev.ylabtrainingapp.exception.TrainingDoesNotExistException;
import xyz.drugalev.ylabtrainingapp.exception.UserPrivilegeException;

import java.time.LocalDate;
import java.util.List;

/**
 * Training service.
 *
 * @author Drugalev Maxim
 */
public interface TrainingService {
    /**
     * Find all trainings for user.
     *
     * @param user current user
     * @return list of training DTOs
     * @throws UserPrivilegeException if user has no privilege to view trainings
     */
    List<TrainingDto> findAll(User user) throws UserPrivilegeException;

    /**
     * Find training by id.
     *
     * @param user current user
     * @param id   training id
     * @return training DTO
     * @throws TrainingDoesNotExistException if training does not exist
     */
    TrainingDto find(User user, long id) throws TrainingDoesNotExistException;

    /**
     * Find all trainings by user.
     *
     * @param user current user
     * @return list of training DTOs
     */
    List<TrainingDto> findAllByUser(User user);

    /**
     * Find all trainings by user between dates.
     *
     * @param user  current user
     * @param start start date
     * @param end   end date
     * @return list of training DTOs
     */
    List<TrainingDto> findByUserBetweenDates(User user, LocalDate start, LocalDate end);

    /**
     * Save training.
     *
     * @param user     current user
     * @param training training DTO
     * @return saved training DTO
     * @throws TrainingAlreadyExistsException if training already exists
     */
    TrainingDto save(User user, TrainingDto training) throws TrainingAlreadyExistsException;

    /**
     * Update training.
     *
     * @param user     current user
     * @param id       training id
     * @param training training DTO
     * @return updated training DTO
     * @throws TrainingDoesNotExistException  if training does not exist
     * @throws TrainingAlreadyExistsException if training already exists
     */
    TrainingDto update(User user, Long id, TrainingDto training) throws TrainingDoesNotExistException, TrainingAlreadyExistsException;

    /**
     * Delete training.
     *
     * @param user current user
     * @param id   training id
     * @throws TrainingDoesNotExistException if training does not exist
     */
    void delete(User user, Long id) throws TrainingDoesNotExistException;
}