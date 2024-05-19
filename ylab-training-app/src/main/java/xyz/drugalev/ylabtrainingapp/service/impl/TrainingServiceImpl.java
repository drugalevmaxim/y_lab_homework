package xyz.drugalev.ylabtrainingapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import xyz.drugalev.ylabtrainingapp.dto.TrainingDto;
import xyz.drugalev.ylabtrainingapp.entity.Training;
import xyz.drugalev.entity.User;
import xyz.drugalev.ylabtrainingapp.exception.TrainingAlreadyExistsException;
import xyz.drugalev.ylabtrainingapp.exception.TrainingDoesNotExistException;
import xyz.drugalev.ylabtrainingapp.exception.UserPrivilegeException;
import xyz.drugalev.ylabtrainingapp.mapper.TrainingMapper;
import xyz.drugalev.ylabtrainingapp.repository.TrainingRepository;
import xyz.drugalev.ylabtrainingapp.service.TrainingService;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;
    private final TrainingMapper trainingMapper;

    @Override
    public List<TrainingDto> findAll(User user) throws UserPrivilegeException {
        if (!Objects.equals(user.getRole().getName(), "admin")) {
            throw new UserPrivilegeException("User does not have access to all trainings");
        }
        return trainingMapper.toTrainingDtoList(trainingRepository.findAll());
    }

    @Override
    public TrainingDto find(User user, long id) throws TrainingDoesNotExistException {
        Optional<Training> training = trainingRepository.findById(id);
        if (training.isEmpty() || training.get().getUser().getId() != user.getId()) {
            throw new TrainingDoesNotExistException("Training does not exist");
        }
        return trainingMapper.toTrainingDto(training.get());
    }

    @Override
    public List<TrainingDto> findAllByUser(User user) {
        return trainingMapper.toTrainingDtoList(trainingRepository.findAllByUser(user));
    }

    @Override
    public List<TrainingDto> findByUserBetweenDates(User user, LocalDate start, LocalDate end) {
        return trainingMapper.toTrainingDtoList(trainingRepository.findAllByUserBetweenDates(user, start, end));
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
    public TrainingDto update(User user, Long id, TrainingDto training) throws TrainingDoesNotExistException, TrainingAlreadyExistsException {
        Optional<Training> t = trainingRepository.findById(id);
        if (t.isEmpty() || t.get().getUser().getId() != user.getId()) {
            throw new TrainingDoesNotExistException("Training does not exist");
        }
        Training trainingUpd = trainingMapper.toTraining(training);
        trainingUpd.setId(id);
        trainingUpd.setUser(user);
        try {
            return trainingMapper.toTrainingDto(trainingRepository.update(trainingUpd));
        } catch (DuplicateKeyException e) {
            throw new TrainingAlreadyExistsException("Training already exists");
        }
    }

    @Override
    public void delete(User user, Long id) throws TrainingDoesNotExistException {
        Optional<Training> t = trainingRepository.findById(id);
        if (t.isEmpty() || t.get().getUser().getId() != user.getId()) {
            throw new TrainingDoesNotExistException("Training does not exist");
        }
        trainingRepository.delete(t.get());
    }
}
