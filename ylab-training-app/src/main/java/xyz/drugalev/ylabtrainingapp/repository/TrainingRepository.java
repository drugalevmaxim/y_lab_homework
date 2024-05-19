package xyz.drugalev.ylabtrainingapp.repository;

import xyz.drugalev.entity.User;
import xyz.drugalev.ylabtrainingapp.entity.Training;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Training repository.
 *
 * @author Drugalev Maxim
 */
public interface TrainingRepository {
    /**
     * Find all trainings.
     *
     * @return list of trainings
     */
    List<Training> findAll();

    /**
     * Find training by id.
     *
     * @param id training id
     * @return optional of training
     */
    Optional<Training> findById(long id);

    /**
     * Find all trainings by user.
     *
     * @param user user
     * @return list of trainings
     */
    List<Training> findAllByUser(User user);

    /**
     * Find all trainings by user between dates.
     *
     * @param user user
     * @param start start date
     * @param end end date
     * @return list of trainings
     */
    List<Training> findAllByUserBetweenDates(User user, LocalDate start, LocalDate end);

    /**
     * Save training.
     *
     * @param training training
     * @return saved training
     */
    Training save(Training training);

    /**
     * Delete training.
     *
     * @param training training
     */
    void delete(Training training);

    /**
     * Update training.
     *
     * @param training training
     * @return updated training
     */
    Training update(Training training);
}