package xyz.drugalev.domain.repository;

import xyz.drugalev.domain.entity.Training;
import xyz.drugalev.domain.entity.TrainingData;

import java.sql.SQLException;
import java.util.List;

/**
 * The interface Training data repository.
 *
 * @author Drugalev Maxim
 */
public interface TrainingDataRepository {
    /**
     * Save data to training.
     *
     * @param training the training
     * @param name     the name of data
     * @param value    the value of data
     * @throws SQLException the sql exception
     */
    void save(Training training, String name, int value) throws SQLException;

    /**
     * Find all data for training.
     *
     * @param training the training
     * @return the list of data
     * @throws SQLException the sql exception
     */
    List<TrainingData> find(Training training) throws SQLException;

}
