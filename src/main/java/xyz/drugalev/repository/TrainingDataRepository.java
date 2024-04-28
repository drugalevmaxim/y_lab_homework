package xyz.drugalev.repository;

import xyz.drugalev.entity.Training;
import xyz.drugalev.entity.TrainingData;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface for working with the training_data table in the database.
 */
public interface TrainingDataRepository {
    /**
     * Saves a new training data record to the database.
     *
     * @param training     the training that the data belongs to
     * @param trainingData the training data to save
     * @throws SQLException if there is an error saving the record to the database
     */
    void save(Training training, TrainingData trainingData) throws SQLException;

    /**
     * Finds all training data records for a given training.
     *
     * @param training the training to find data for
     * @return a list of all training data records for the given training
     * @throws SQLException if there is an error retrieving the records from the database
     */
    List<TrainingData> find(Training training) throws SQLException;

    /**
     * Updates an existing training data record in the database.
     *
     * @param training     the training that the data belongs to
     * @param trainingData the training data to update
     * @throws SQLException if there is an error updating the record in the database
     */
    void update(Training training, TrainingData trainingData) throws SQLException;

    /**
     * Deletes an existing training data record from the database.
     *
     * @param training     the training that the data belongs to
     * @param trainingData the training data to delete
     * @throws SQLException if there is an error deleting the record from the database
     */
    void delete(Training training, TrainingData trainingData) throws SQLException;
}