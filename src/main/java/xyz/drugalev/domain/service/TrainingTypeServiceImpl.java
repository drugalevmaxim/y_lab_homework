package xyz.drugalev.domain.service;

import xyz.drugalev.domain.entity.TrainingType;
import xyz.drugalev.domain.repository.TrainingTypeRepository;

import java.util.List;

public class TrainingTypeServiceImpl implements TrainingTypeService {
    private final TrainingTypeRepository TrainingTypeRepository;

    public TrainingTypeServiceImpl(TrainingTypeRepository TrainingTypeRepository) {
        this.TrainingTypeRepository = TrainingTypeRepository;
    }

    @Override
    public List<TrainingType> findAll() {
        return TrainingTypeRepository.findAll();
    }

    @Override
    public TrainingType save(TrainingType TrainingType) {
        return TrainingTypeRepository.save(TrainingType);
    }

}
