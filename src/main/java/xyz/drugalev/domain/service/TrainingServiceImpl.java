package xyz.drugalev.domain.service;

import xyz.drugalev.domain.entity.Training;
import xyz.drugalev.domain.entity.TrainingType;
import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.exception.IllegalDatePeriodException;
import xyz.drugalev.domain.exception.TrainingAlreadyExistsException;
import xyz.drugalev.domain.repository.TrainingRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;

    public TrainingServiceImpl(TrainingRepository TrainingRepository) {
        this.trainingRepository = TrainingRepository;
    }

    @Override
    public List<Training> findAll() {
        return trainingRepository.findAll();
    }

    @Override
    public List<Training> findAllByUser(User user) {
        return trainingRepository.findAllByUser(user);
    }

    @Override
    public Optional<Training> find(User user, TrainingType type, LocalDate date) {
        return trainingRepository.find(user, type, date);
    }

    @Override
    public List<Training> findByUserBetweenDates(User user, LocalDate start, LocalDate end) throws IllegalDatePeriodException {
        return trainingRepository.findByUserBetweenDates(user, start, end);
    }

    @Override
    public int getTrainingsDuration(User user, LocalDate start, LocalDate end) throws IllegalDatePeriodException {
        return trainingRepository.findByUserBetweenDates(user, start, end).stream().mapToInt(Training::getDuration).sum();
    }

    @Override
    public int getTrainingsBurnedCalories(User user, LocalDate start, LocalDate end) throws IllegalDatePeriodException {
        return trainingRepository.findByUserBetweenDates(user, start, end).stream().mapToInt(Training::getBurnedCalories).sum();
    }

    @Override
    public Training save(Training Training) throws TrainingAlreadyExistsException {
        return trainingRepository.save(Training);
    }

    @Override
    public void delete(Training training) {
        trainingRepository.delete(training);
    }
}
