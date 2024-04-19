package xyz.drugalev.domain.service.impl;

import xyz.drugalev.domain.entity.Training;
import xyz.drugalev.domain.entity.TrainingType;
import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.exception.IllegalDatePeriodException;
import xyz.drugalev.domain.exception.TrainingAlreadyExistsException;
import xyz.drugalev.domain.repository.TrainingRepository;
import xyz.drugalev.domain.service.TrainingService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public Map<String, Integer> getTrainingsStats(User user, LocalDate start, LocalDate end) throws IllegalDatePeriodException {
        Map<String, Integer> stats = new HashMap<>();
        int dur = 0;
        int cal = 0;
        for (Training training : trainingRepository.findByUserBetweenDates(user, start, end)) {
            dur += training.getDuration();
            cal += training.getBurnedCalories();
        }
        stats.put("Duration", dur);
        stats.put("Calories", cal);
        return stats;
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
