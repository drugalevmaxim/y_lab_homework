package xyz.drugalev.usecase.training;

import xyz.drugalev.domain.entity.Training;
import xyz.drugalev.domain.service.TrainingService;

import java.sql.SQLException;

/**
 * UseCase for adding data to training.
 *
 * @author Drugalev Maxim
 */
public class AddTrainingDataUseCase {
    private final TrainingService trainingService;

    /**
     * Default constructor.
     *
     * @param trainingService service that use case works with.
     */
    public AddTrainingDataUseCase(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    /**
     * Add training data to training.
     *
     * @param training training to which training data will be added.
     * @param name     name of added data.
     * @param value    value of added data.
     * @throws SQLException the sql exception
     */
    public void addTrainingData(Training training, String name, int value) throws SQLException {
        trainingService.addData(training, name, value);
    }
}
