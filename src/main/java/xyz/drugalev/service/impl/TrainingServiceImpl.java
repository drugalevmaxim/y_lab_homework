package xyz.drugalev.service.impl;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import xyz.drugalev.aspect.annotation.Auditable;
import xyz.drugalev.aspect.annotation.LogExecSpeed;
import xyz.drugalev.dto.TrainingDto;
import xyz.drugalev.entity.Training;
import xyz.drugalev.entity.User;
import xyz.drugalev.exception.AccessDeniedException;
import xyz.drugalev.exception.TrainingAlreadyExistsException;
import xyz.drugalev.exception.TrainingNotFoundException;
import xyz.drugalev.mapper.TrainingMapper;
import xyz.drugalev.repository.TrainingRepository;
import xyz.drugalev.service.TrainingService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * The Training service implementation.
 */

@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;
    private final TrainingMapper trainingMapper = Mappers.getMapper(TrainingMapper.class);


    @Override
    @Auditable(action = "findAll(user)")
    public List<TrainingDto> findAll(User user) throws SQLException, AccessDeniedException {
        if (!user.hasPrivilege("OTHER_USERS_TRAININGS")) {
            throw new AccessDeniedException();
        }
        return trainingMapper.toTrainingDtos(trainingRepository.findAll());
    }

    @Override
    @Auditable(action = "find specified training by id")
    @LogExecSpeed
    public TrainingDto find(User user, long id) throws SQLException, TrainingNotFoundException, AccessDeniedException {
        Training training = trainingRepository.find(id).orElseThrow(TrainingNotFoundException::new);
        if (training.getPerformer().getId() != user.getId() && !user.hasPrivilege("OTHER_USERS_TRAININGS")) {
            throw new AccessDeniedException();
        }
        return trainingMapper.toTrainingDto(training);
    }

    @Override
    @Auditable(action = "find all trainings of a user")
    public List<TrainingDto> findAllByUser(User user) throws SQLException {
        return trainingMapper.toTrainingDtos(trainingRepository.findAllByUser(user));
    }

    @Override
    @Auditable(action = "find all trainings of a user between dates")
    public List<TrainingDto> findByUserBetweenDates(User user, LocalDate start, LocalDate end) throws SQLException {
        return trainingMapper.toTrainingDtos(trainingRepository.findByUserBetweenDates(user, start, end));
    }

    @Override
    @Auditable(action = "save new training")
    public void save(User user, TrainingDto training) throws SQLException, TrainingAlreadyExistsException {
        Training trainingToSave = trainingMapper.toTraining(training);
        trainingToSave.setPerformer(user);
        if (trainingRepository.find(trainingToSave.getPerformer(), trainingToSave.getTrainingType(), trainingToSave.getDate()).isPresent()) {
            throw new TrainingAlreadyExistsException();
        }
        trainingRepository.save(trainingToSave);
    }

    @Override
    @Auditable(action = "update training for a user")
    public void update(User user, TrainingDto training) throws SQLException, TrainingNotFoundException {
        if (trainingRepository.find(training.getId()).orElseThrow(TrainingNotFoundException::new).getPerformer().getId() != user.getId()) {
            throw new TrainingNotFoundException();
        }
        Training trainingToUpdate = trainingMapper.toTraining(training);
        trainingRepository.update(trainingToUpdate);
    }

    @Override
    @Auditable(action = "delete training for a user")
    public void delete(User user, TrainingDto training) throws SQLException, TrainingNotFoundException {
        if (trainingRepository.find(training.getId()).orElseThrow(TrainingNotFoundException::new).getPerformer().getId() != user.getId()) {
            throw new TrainingNotFoundException();
        }
        Training trainingToDelete = trainingMapper.toTraining(training);
        trainingRepository.delete(trainingToDelete);
    }
}
