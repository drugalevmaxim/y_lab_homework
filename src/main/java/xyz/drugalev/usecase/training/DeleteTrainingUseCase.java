package xyz.drugalev.usecase.training;

import xyz.drugalev.domain.entity.Training;
import xyz.drugalev.domain.service.TrainingService;

import java.sql.SQLException;

/**
 * UseCase for deleting training.
 *
 * @author Drugalev Maxim
 */
public class DeleteTrainingUseCase {
    private final TrainingService trainingService;

    /**
     * Default constructor.
     *
     * @param trainingService service that use case works with.
     */
    public DeleteTrainingUseCase(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    /**
     * Deletes training.
     *
     * @param training training to delete.
     * @throws SQLException the sql exception
     */
    public void delete(Training training) throws SQLException {
        trainingService.delete(training);
    }
}
