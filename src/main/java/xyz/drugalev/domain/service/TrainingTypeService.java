package xyz.drugalev.domain.service;

import xyz.drugalev.domain.entity.TrainingType;

import java.util.List;

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
    List<TrainingType> findAll();


    /**
     * Saves given Training type.
     *
     * @param TrainingType Training type to save.
     * @return saved Training type.
     */
    TrainingType save(TrainingType TrainingType);
}
