package xyz.drugalev.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import xyz.drugalev.dto.TrainingDto;
import xyz.drugalev.entity.Training;
import xyz.drugalev.entity.User;
import xyz.drugalev.exception.TrainingAlreadyExistsException;
import xyz.drugalev.exception.TrainingDoesNotExistsException;
import xyz.drugalev.mapper.TrainingMapper;
import xyz.drugalev.repository.TrainingRepository;
import xyz.drugalev.service.TrainingService;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;
    private final TrainingMapper trainingMapper;

    @Override
    public List<TrainingDto> findAll(User user) throws AccessDeniedException {
        if (!user.hasPrivilege("OTHER_USERS_TRAININGS")) {
            throw new AccessDeniedException("User does not have access to all trainings");
        }
        return trainingMapper.toTrainingDtoList(trainingRepository.findAll());
    }

    @Override
    public TrainingDto find(User user, long id) throws TrainingDoesNotExistsException {
        Optional<Training> training = trainingRepository.find(id);
        if (training.isEmpty() || training.get().getUser().getId() != user.getId()) {
            throw new TrainingDoesNotExistsException("Training does not exist");
        }
        return trainingMapper.toTrainingDto(training.get());
    }

    @Override
    public List<TrainingDto> findAllByUser(User user) {
        return trainingMapper.toTrainingDtoList(trainingRepository.findAllByUser(user));
    }

    @Override
    public List<TrainingDto> findByUserBetweenDates(User user, LocalDate start, LocalDate end) {
        return trainingMapper.toTrainingDtoList(trainingRepository.findByUserBetweenDates(user, start, end));
    }

    @Override
    public TrainingDto save(User user, TrainingDto training) throws TrainingAlreadyExistsException {
        Training t = trainingMapper.toTraining(training);
        t.setUser(user);
        try {
            return trainingMapper.toTrainingDto(trainingRepository.save(t));
        } catch (DuplicateKeyException e) {
            throw new TrainingAlreadyExistsException("Training already exists");
        }
    }

    @Override
    public TrainingDto update(User user, Long id, TrainingDto training) throws TrainingDoesNotExistsException {
        Optional<Training> t = trainingRepository.find(id);
        if (t.isEmpty() || t.get().getUser().getId() != user.getId()) {
            throw new TrainingDoesNotExistsException("Training does not exist");
        }
        Training trainingUpd = trainingMapper.toTraining(training);
        trainingUpd.setId(id);
        trainingUpd.setUser(user);
        return trainingMapper.toTrainingDto(trainingRepository.update(trainingUpd));
    }

    @Override
    public void delete(User user, Long id) throws TrainingDoesNotExistsException {
        Optional<Training> t = trainingRepository.find(id);
        if (t.isEmpty() || t.get().getUser().getId() != user.getId()) {
            throw new TrainingDoesNotExistsException("Training does not exist");
        }
        trainingRepository.delete(t.get());
    }
}
