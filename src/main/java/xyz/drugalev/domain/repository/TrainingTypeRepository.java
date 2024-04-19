package xyz.drugalev.domain.repository;

import lombok.NonNull;
import xyz.drugalev.domain.entity.TrainingType;

import java.util.List;

/**
 * An interface that defines operations with Training types in repository.
 *
 * @author Drugalev Maxim
 */
public interface TrainingTypeRepository {
    /**
     * Returns a list of all Training types stored in repository.
     *
     * @return list of Training types.
     */
    List<TrainingType> findAll();

    /**
     * Saves given Training type.
     *
     * @param TrainingType Training type to save.
     * @return saved Training type.
     */
    TrainingType save(@NonNull TrainingType TrainingType);
}
