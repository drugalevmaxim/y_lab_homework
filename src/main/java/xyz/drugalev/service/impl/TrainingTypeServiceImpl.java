package xyz.drugalev.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import xyz.drugalev.dto.TrainingTypeDto;
import xyz.drugalev.entity.User;
import xyz.drugalev.exception.TrainingTypeAlreadyExistsException;
import xyz.drugalev.exception.UserPrivilegeException;
import xyz.drugalev.mapper.TrainingTypeMapper;
import xyz.drugalev.repository.TrainingTypeRepository;
import xyz.drugalev.service.TrainingTypeService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {
    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainingTypeMapper trainingTypeMapper;
    @Override
    public List<TrainingTypeDto> getAll() {
        return trainingTypeMapper.toTrainingTypeDtos(trainingTypeRepository.findAll());
    }

    @Override
    public TrainingTypeDto save(User user, TrainingTypeDto trainingType) throws UserPrivilegeException, TrainingTypeAlreadyExistsException {
        if (!user.hasPrivilege("ADD_TRAINING_TYPE")) {
            throw new UserPrivilegeException("User does not have privilege to add training type");
        } try {
            return trainingTypeMapper.toTrainingTypeDto(trainingTypeRepository.save(trainingTypeMapper.toTrainingType(trainingType)));
        } catch (DuplicateKeyException e) {
            throw new TrainingTypeAlreadyExistsException("Training type \"%s\" already exists".formatted(trainingType.getName()));
        }
    }
}
