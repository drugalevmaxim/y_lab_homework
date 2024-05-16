package xyz.drugalev.repository;

import xyz.drugalev.entity.TrainingType;

import java.util.List;
import java.util.Optional;

/**
 * Interface for working with the training_type table in the database.
 */
public interface TrainingTypeRepository {
    /**
     * Finds a training type by its ID.
     *
     * @param id the ID of the training type to find
     * @return an optional containing the training type, or an empty optional if the training type does not exist
     */
    Optional<TrainingType> findById(long id);

    /**
     * Finds a training type by its name.
     *
     * @param trainingTypeName the name of the training type to find
     * @return an optional containing the training type, or an empty optional if the training type does not exist
     */
    Optional<TrainingType> findByName(String trainingTypeName);

    /**
     * Finds all training types from the database.
     *
     * @return a list of all training types
     */
    List<TrainingType> findAll();

    /**
     * Saves a new training type to the database.
     *
     * @param trainingType the training type to save
     */
    TrainingType save(TrainingType trainingType);
}
