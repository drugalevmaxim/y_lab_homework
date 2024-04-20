package xyz.drugalev.domain.service.impl;

import lombok.NonNull;
import xyz.drugalev.domain.entity.Training;
import xyz.drugalev.domain.entity.TrainingType;
import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.repository.TrainingRepository;
import xyz.drugalev.domain.service.TrainingService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;

    public TrainingServiceImpl(@NonNull TrainingRepository TrainingRepository) {
        this.trainingRepository = TrainingRepository;
    }

    @Override
    public List<Training> findAll() throws SQLException {
        return trainingRepository.findAll();
    }

    @Override
    public List<Training> findAllByUser(@NonNull User user) throws SQLException {
        return trainingRepository.findAllByUser(user);
    }

    @Override
    public Optional<Training> find(@NonNull User user, @NonNull LocalDate date, @NonNull TrainingType type) throws SQLException {
        return trainingRepository.find(user, type, date);
    }

    @Override
    public List<Training> findByUserBetweenDates(@NonNull User user, @NonNull LocalDate start, @NonNull LocalDate end) throws SQLException {
        return trainingRepository.findByUserBetweenDates(user, start, end);
    }

    public Map<String, Integer> getTrainingsStats(@NonNull User user, @NonNull LocalDate start, @NonNull LocalDate end) throws SQLException {
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
    public void save(@NonNull User performer, @NonNull LocalDate date, @NonNull TrainingType type, int duration, int burnedCalories) throws SQLException {
        trainingRepository.save(performer, date, type, duration, burnedCalories);
    }

    @Override
    public void update(@NonNull Training training, int duration, int burnedCalories) throws SQLException {
        trainingRepository.update(training, duration, burnedCalories);
    }

    @Override
    public void delete(@NonNull Training training) throws SQLException {
        trainingRepository.delete(training);
    }

    @Override
    public void addData(@NonNull Training training, @NonNull String name, int value) throws SQLException {
        trainingRepository.addData(training, name, value);
    }
}
