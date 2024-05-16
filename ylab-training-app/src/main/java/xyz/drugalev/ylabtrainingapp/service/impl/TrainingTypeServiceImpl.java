package xyz.drugalev.ylabtrainingapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import xyz.drugalev.ylabtrainingapp.dto.TrainingTypeDto;
import xyz.drugalev.entity.User;
import xyz.drugalev.ylabtrainingapp.exception.TrainingTypeAlreadyExistsException;
import xyz.drugalev.ylabtrainingapp.exception.UserPrivilegeException;
import xyz.drugalev.ylabtrainingapp.mapper.TrainingTypeMapper;
import xyz.drugalev.ylabtrainingapp.repository.TrainingTypeRepository;
import xyz.drugalev.ylabtrainingapp.service.TrainingTypeService;

import java.util.List;
import java.util.Objects;

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
        if (!Objects.equals(user.getRole().getName(), "admin")) {
            throw new UserPrivilegeException("User does not have privilege to add training type");
        }
        try {
            return trainingTypeMapper.toTrainingTypeDto(trainingTypeRepository.save(trainingTypeMapper.toTrainingType(trainingType)));
        } catch (DuplicateKeyException e) {
            throw new TrainingTypeAlreadyExistsException("Training type \"%s\" already exists".formatted(trainingType.getName()));
        }
    }
}
