package xyz.drugalev.usecase.training;


import xyz.drugalev.domain.entity.Training;
import xyz.drugalev.domain.entity.TrainingType;
import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.exception.IllegalDatePeriodException;
import xyz.drugalev.domain.exception.NegativeArgumentException;
import xyz.drugalev.domain.exception.TrainingAlreadyExistsException;
import xyz.drugalev.domain.service.TrainingService;

import java.time.LocalDate;

/**
 * UseCase for adding training
 *
 * @author Drugalev Maxim
 */
public class AddTrainingUseCase {
    private final TrainingService trainingService;

    /**
     * Default constructor.
     *
     * @param trainingService service that use case works with.
     */
    public AddTrainingUseCase(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    /**
     * Add training
     *
     * @param user user which performed trainings.
     * @param date date when training that was performed.
     * @param TrainingType type of training that was performed.
     * @param duration duration of training.
     * @param burnedCalories burned calories while training.
     * @return added training.
     * @throws NegativeArgumentException if duration or burned calories less than 0.
     * @throws IllegalDatePeriodException if date > today.
     * @throws TrainingAlreadyExistsException if training already exists.
     */
    public Training add(User user, LocalDate date, TrainingType TrainingType, int duration, int burnedCalories) throws TrainingAlreadyExistsException, IllegalDatePeriodException, NegativeArgumentException {
        if (duration < 0) {
            throw new NegativeArgumentException("duration less than 0");
        }
        if (burnedCalories < 0) {
            throw new NegativeArgumentException("burned calories less than 0");
        }
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalDatePeriodException("date > today");
        }
        return trainingService.save(new Training(user, date, TrainingType, duration, burnedCalories));
    }
}
