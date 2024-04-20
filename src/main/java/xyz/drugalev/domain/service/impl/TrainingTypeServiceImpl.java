package xyz.drugalev.domain.service.impl;

import lombok.NonNull;
import xyz.drugalev.domain.entity.TrainingType;
import xyz.drugalev.domain.repository.TrainingTypeRepository;
import xyz.drugalev.domain.service.TrainingTypeService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TrainingTypeServiceImpl implements TrainingTypeService {
    private final TrainingTypeRepository TrainingTypeRepository;

    public TrainingTypeServiceImpl(@NonNull TrainingTypeRepository TrainingTypeRepository) {
        this.TrainingTypeRepository = TrainingTypeRepository;
    }

    @Override
    public List<TrainingType> findAll() throws SQLException {
        return TrainingTypeRepository.findAll();
    }

    @Override
    public Optional<TrainingType> find(String trainingTypeName) throws SQLException {
        return TrainingTypeRepository.find(trainingTypeName);
    }

    @Override
    public void save(@NonNull String trainingTypeName) throws SQLException {
        TrainingTypeRepository.save(trainingTypeName);
    }

}
