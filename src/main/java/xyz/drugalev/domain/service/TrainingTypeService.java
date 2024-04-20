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
     */
    List<TrainingType> findAll() throws SQLException;

    Optional<TrainingType> find(String trainingTypeName) throws SQLException;

    /**
     * Save training type to repository
     *
     * @param trainingTypeName training type name
     */
    void save(@NonNull String trainingTypeName) throws SQLException;
}
