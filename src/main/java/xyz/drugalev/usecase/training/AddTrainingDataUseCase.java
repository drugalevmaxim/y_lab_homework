package xyz.drugalev.usecase.training;

import xyz.drugalev.domain.entity.Training;
import xyz.drugalev.domain.entity.TrainingData;
import xyz.drugalev.domain.service.TrainingService;

/**
 * UseCase for adding data to training
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
     * Add training data to training
     *
     * @param training training to which training data will be added
     * @param trainingData training data to add;
     */
    public void addTrainingData(Training training, TrainingData trainingData)  {
        training.getTrainingData().add(trainingData);
    }
}
