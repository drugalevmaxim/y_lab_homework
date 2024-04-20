package xyz.drugalev.usecase.training;


import xyz.drugalev.domain.entity.Training;
import xyz.drugalev.domain.entity.TrainingType;
import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.exception.IllegalDateException;
import xyz.drugalev.domain.exception.NegativeArgumentException;
import xyz.drugalev.domain.service.TrainingService;

import java.sql.SQLException;
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
     * @param user           user which performed trainings.
     * @param date           date when training that was performed.
     * @param trainingType   type of training that was performed.
     * @param duration       duration of training.
     * @param burnedCalories burned calories while training.
     * @return added training.
     * @throws NegativeArgumentException      if duration or burned calories less than 0.
     * @throws IllegalDateException     if date > today.
     */
    public Training add(User user, LocalDate date, TrainingType trainingType, int duration, int burnedCalories) throws IllegalDateException, NegativeArgumentException, SQLException {
        if (duration < 0) {
            throw new NegativeArgumentException("duration less than 0");
        }
        if (burnedCalories < 0) {
            throw new NegativeArgumentException("burned calories less than 0");
        }
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalDateException("date > today");
        }

        trainingService.save(user, date, trainingType, duration, burnedCalories);
        return trainingService.find(user, date, trainingType).get();
    }
}
