package xyz.drugalev.domain.service.impl;

import lombok.NonNull;
import xyz.drugalev.domain.entity.TrainingType;
import xyz.drugalev.domain.repository.TrainingTypeRepository;
import xyz.drugalev.domain.service.TrainingTypeService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * The Training type service implementation.
 */
public class TrainingTypeServiceImpl implements TrainingTypeService {
    private final TrainingTypeRepository trainingTypeRepository;

    /**
     * Instantiates a new Training type service.
     *
     * @param trainingTypeRepository the training type repository
     */
    public TrainingTypeServiceImpl(@NonNull TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Override
    public List<TrainingType> findAll() throws SQLException {
        return trainingTypeRepository.findAll();
    }

    @Override
    public Optional<TrainingType> find(String trainingTypeName) throws SQLException {
        return trainingTypeRepository.find(trainingTypeName);
    }

    @Override
    public void save(@NonNull String trainingTypeName) throws SQLException {
        trainingTypeRepository.save(trainingTypeName);
    }

}
