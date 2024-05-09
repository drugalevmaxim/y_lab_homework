package xyz.drugalev.service.impl;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import xyz.drugalev.aspect.annotation.Auditable;
import xyz.drugalev.dto.TrainingTypeDto;
import xyz.drugalev.entity.User;
import xyz.drugalev.exception.AccessDeniedException;
import xyz.drugalev.mapper.TrainingTypeMapper;
import xyz.drugalev.repository.TrainingTypeRepository;
import xyz.drugalev.service.TrainingTypeService;

import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {
    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainingTypeMapper trainingTypeMapper = Mappers.getMapper(TrainingTypeMapper.class);

    @Override
    @Auditable(action = "add training type")
    public void save(User user, TrainingTypeDto trainingTypeDto) throws SQLException, AccessDeniedException {
        if (!user.hasPrivilege("ADD_TRAINING_TYPE")) {
            throw new AccessDeniedException();
        }
        trainingTypeRepository.save(trainingTypeDto.getName());
    }

    @Override
    @Auditable(action = "find all training types")
    public List<TrainingTypeDto> findAll() throws SQLException {
        return trainingTypeMapper.toTrainingTypeDtos(trainingTypeRepository.findAll());
    }
}
