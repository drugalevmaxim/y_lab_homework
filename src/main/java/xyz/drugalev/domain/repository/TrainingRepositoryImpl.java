package xyz.drugalev.domain.repository;

import lombok.NonNull;
import xyz.drugalev.domain.entity.Training;
import xyz.drugalev.domain.entity.TrainingType;
import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.exception.IllegalDatePeriodException;
import xyz.drugalev.domain.exception.TrainingAlreadyExistsException;

import java.time.LocalDate;
import java.util.*;


/**
 * Training repository implementation
 * Stores all data in memory
 *
 * @author Drugalev Maxim
 */
public class TrainingRepositoryImpl implements TrainingRepository {
    private final Set<Training> trainings = new HashSet<>();

    @Override
    public List<Training> findAll() {
        return trainings.stream().toList();
    }

    @Override
    public List<Training> findAllByUser(@NonNull User user) {
        return this.trainings.stream().filter(r -> r.getPerformer().equals(user))
                .sorted(Comparator.comparing(Training::getDate).reversed()).toList();
    }

    @Override
    public List<Training> findByUserBetweenDates(@NonNull User user, @NonNull LocalDate start, @NonNull LocalDate end) throws IllegalDatePeriodException {
        if (start.isAfter(end)) {
            throw new IllegalDatePeriodException("start > end");
        }

        return this.trainings.stream().filter(w -> w.getPerformer().equals(user)
                        && w.getDate().isAfter(start.minusDays(1))
                        && w.getDate().isBefore(end.plusDays(1)))
                .sorted(Comparator.comparing(Training::getDate).reversed()).toList();
    }

    @Override
    public Optional<Training> find(@NonNull User user, @NonNull TrainingType type, @NonNull LocalDate date) {
        return this.trainings.stream().filter(w -> w.getPerformer().equals(user)
                && w.getTrainingType().equals(type) && w.getDate().isEqual(date)).findFirst();
    }

    @Override
    public Training save(@NonNull Training training) throws TrainingAlreadyExistsException {
        if (trainings.stream().anyMatch(t -> t.getPerformer().equals(training.getPerformer())
                && t.getDate().equals(training.getDate())
                && t.getTrainingType().equals(training.getTrainingType()))) {
            throw new TrainingAlreadyExistsException();
        }
        trainings.add(training);
        return training;
    }

    @Override
    public void delete(@NonNull Training training) {
        trainings.remove(training);
    }
}
