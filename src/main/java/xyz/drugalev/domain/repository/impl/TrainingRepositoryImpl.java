package xyz.drugalev.domain.repository.impl;

import lombok.NonNull;
import xyz.drugalev.domain.entity.Training;
import xyz.drugalev.domain.entity.TrainingType;
import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.exception.IllegalDatePeriodException;
import xyz.drugalev.domain.exception.TrainingAlreadyExistsException;
import xyz.drugalev.domain.repository.TrainingRepository;

import java.time.LocalDate;
import java.util.*;


/**
 * Training repository implementation
 * Stores all data in memory
 *
 * @author Drugalev Maxim
 */
public class TrainingRepositoryImpl implements TrainingRepository {
    private final Map<User, Map<LocalDate, Map<TrainingType, Training>>> trainings = new HashMap<>();
    @Override
    public List<Training> findAll() {
        return trainings.values().stream().map(Map::values).flatMap(Collection::stream).map(Map::values).flatMap(Collection::stream).toList();
    }

    @Override
    public List<Training> findAllByUser(@NonNull User user) {
        return this.trainings.get(user).values().stream().map(Map::values).flatMap(Collection::stream).sorted(Comparator.comparing(Training::getDate).reversed()).toList();
    }

    @Override
    public List<Training> findByUserBetweenDates(@NonNull User user, @NonNull LocalDate start, @NonNull LocalDate end) throws IllegalDatePeriodException {
        if (start.isAfter(end)) {
            throw new IllegalDatePeriodException("start > end");
        }

        return findAllByUser(user).stream().filter(t -> t.getDate().isAfter(start.minusDays(1))
                && t.getDate().isBefore(end.plusDays(1))).toList();
    }

    @Override
    public Optional<Training> find(@NonNull User user, @NonNull TrainingType type, @NonNull LocalDate date) {
        if (!trainings.containsKey(user)
                && !trainings.get(user).containsKey(date)
                && !trainings.get(user).get(date).containsKey(type)) {
            return Optional.empty();
        }
        return Optional.ofNullable(trainings.get(user).get(date).get(type));
    }

    @Override
    public Training save(@NonNull Training training) throws TrainingAlreadyExistsException {
        if (!trainings.containsKey(training.getPerformer())) {
            trainings.put(training.getPerformer(), new HashMap<>());
        }
        if (!trainings.get(training.getPerformer()).containsKey(training.getDate())) {
            trainings.get(training.getPerformer()).put(training.getDate(), new HashMap<>());
        }
        if (!trainings.get(training.getPerformer()).get(training.getDate()).containsKey(training.getTrainingType())) {
            trainings.get(training.getPerformer()).get(training.getDate()).put(training.getTrainingType(), training);
        } else {
            if (trainings.get(training.getPerformer()).get(training.getDate()).get(training.getTrainingType()) != null ) {
                System.out.println(trainings.get(training.getPerformer()).get(training.getDate()).get(training.getTrainingType()));
                throw new TrainingAlreadyExistsException();
            }
        }

        return training;
    }

    @Override
    public void delete(@NonNull Training training) {
        if (!trainings.containsKey(training.getPerformer())) {
            return;
        }
        if (!trainings.get(training.getPerformer()).containsKey(training.getDate())) {
            return;
        }
        if (!trainings.get(training.getPerformer()).get(training.getDate()).containsKey(training.getTrainingType())) {
            return;
        }
        if (trainings.get(training.getPerformer()).get(training.getDate()).get(training.getTrainingType()) != null) {
            trainings.get(training.getPerformer()).get(training.getDate()).remove(training.getTrainingType());
        }
    }
}
