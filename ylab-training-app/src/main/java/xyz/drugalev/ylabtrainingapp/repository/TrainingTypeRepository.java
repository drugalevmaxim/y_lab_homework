package xyz.drugalev.ylabtrainingapp.repository;

import xyz.drugalev.ylabtrainingapp.entity.TrainingType;

import java.util.List;
import java.util.Optional;

/**
 * Training type repository.
 *
 * @author Drugalev Maxim
 */
public interface TrainingTypeRepository {
    /**
     * Save training type.
     *
     * @param trainingType training type
     * @return saved training type
     */
    TrainingType save(TrainingType trainingType);

    /**
     * Find all training types.
     *
     * @return list of training types
     */
    List<TrainingType> findAll();

    /**
     * Find training type by id.
     *
     * @param id training type id
     * @return optional of training type
     */
    Optional<TrainingType> findById(long id);
}