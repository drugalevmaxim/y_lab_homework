package xyz.drugalev.domain.repository;

import xyz.drugalev.domain.entity.TrainingType;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * The interface Training type repository.
 *
 * @author Drugalev Maxim
 */
public interface TrainingTypeRepository {
    /**
     * Find optional.
     *
     * @param id the id
     * @return the optional
     * @throws SQLException the sql exception
     */
    Optional<TrainingType> find(int id) throws SQLException;

    /**
     * Find optional.
     *
     * @param trainingTypeName the training type name
     * @return the optional
     * @throws SQLException the sql exception
     */
    Optional<TrainingType> find(String trainingTypeName) throws SQLException;

    /**
     * Find all list.
     *
     * @return the list
     * @throws SQLException the sql exception
     */
    List<TrainingType> findAll() throws SQLException;

    /**
     * Save.
     *
     * @param trainingTypeName the training type name
     * @throws SQLException the sql exception
     */
    void save(String trainingTypeName) throws SQLException;
}
