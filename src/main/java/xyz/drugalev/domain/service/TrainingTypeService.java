package xyz.drugalev.domain.service;

import lombok.NonNull;
import xyz.drugalev.domain.entity.TrainingType;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * An interface that defines training types service.
 *
 * @author Drugalev Maxim
 */
public interface TrainingTypeService {
    /**
     * Returns a list of all Training types.
     *
     * @return list of Training types.
     * @throws SQLException the sql exception
     */
    List<TrainingType> findAll() throws SQLException;

    /**
     * Find training type by name.
     *
     * @param trainingTypeName the training type name
     * @return the training type if found, empty otherwise, wrapped in an {@link Optional}
     * @throws SQLException the sql exception
     */
    Optional<TrainingType> find(String trainingTypeName) throws SQLException;

    /**
     * Save training type to repository
     *
     * @param trainingTypeName the training type name
     * @throws SQLException the sql exception
     */
    void save(@NonNull String trainingTypeName) throws SQLException;
}
