package xyz.drugalev.domain.repository.impl;

import lombok.NonNull;
import xyz.drugalev.domain.entity.TrainingType;
import xyz.drugalev.domain.repository.TrainingTypeRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Training type repository implementation
 * Stores all data in memory
 *
 * @author Drugalev Maxim
 */
public class TrainingTypeRepositoryImpl implements TrainingTypeRepository {
    private final Set<TrainingType> trainingTypes = new HashSet<>();

    @Override
    public List<TrainingType> findAll() {
        return trainingTypes.stream().toList();
    }

    @Override
    public TrainingType save(@NonNull TrainingType TrainingType) {
        trainingTypes.add(TrainingType);
        return TrainingType;
    }
}
