package xyz.drugalev.repository;

import xyz.drugalev.entity.TrainingType;

import java.sql.SQLException;
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
     * @throws SQLException if there is an error querying the database
     */
    Optional<TrainingType> find(int id) throws SQLException;

    /**
     * Finds a training type by its name.
     *
     * @param trainingTypeName the name of the training type to find
     * @return an optional containing the training type, or an empty optional if the training type does not exist
     * @throws SQLException if there is an error querying the database
     */
    Optional<TrainingType> find(String trainingTypeName) throws SQLException;

    /**
     * Finds all training types from the database.
     *
     * @return a list of all training types
     * @throws SQLException if there is an error retrieving the training types from the database
     */
    List<TrainingType> findAll() throws SQLException;

    /**
     * Saves a new training type to the database.
     *
     * @param trainingTypeName the name of the training type to save
     * @throws SQLException if there is an error saving the training type to the database
     */
    void save(String trainingTypeName) throws SQLException;
}
