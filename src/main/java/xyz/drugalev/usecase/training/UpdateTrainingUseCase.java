package xyz.drugalev.usecase.training;

import xyz.drugalev.domain.entity.Training;
import xyz.drugalev.domain.exception.NegativeArgumentException;
import xyz.drugalev.domain.service.TrainingService;

/**
 * Use case for getting training stats.
 *
 * @author Drugalev Maxim
 */
public class UpdateTrainingUseCase {
    private final TrainingService trainingService;

    /**
     * Default constructor.
     *
     * @param trainingService service that use case works with.
     */
    public UpdateTrainingUseCase(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    /**
     * Update training.
     *
     * @param training       training to update.
     * @param duration       new training duration.
     * @param burnedCalories new training burned calories.
     * @throws NegativeArgumentException if duration or burned calories less than 0.
     */
    public void update(Training training, int duration, int burnedCalories) throws NegativeArgumentException {
        if (duration < 0) {
            throw new NegativeArgumentException("duration less than 0");
        }
        if (burnedCalories < 0) {
            throw new NegativeArgumentException("burned calories less than 0");
        }

        training.setDuration(duration);
        training.setBurnedCalories(burnedCalories);
    }
}
